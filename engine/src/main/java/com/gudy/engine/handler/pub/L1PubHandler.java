package com.gudy.engine.handler.pub;

import com.gudy.engine.bean.EngineConfig;
import com.gudy.engine.bean.command.RbCmd;
import com.gudy.engine.bean.orderbook.MatchEvent;
import com.gudy.engine.handler.BaseHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.collections.api.tuple.primitive.ShortObjectPair;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ShortObjectHashMap;
import thirdpart.bean.CommonMsg;
import thirdpart.hq.L1MarketData;
import thirdpart.hq.MatchData;
import thirdpart.order.CmdType;

import java.util.List;

import static thirdpart.bean.MsgConstants.MATCH_HQ_DATA;
import static thirdpart.bean.MsgConstants.MATCH_ORDER_DATA;
import static thirdpart.bean.MsgConstants.NORMAL;

/**
 * 行情处理器：
 * 1. 往外广播行情
 * 2. 柜台推过来的有成交的变化的委托，推回去
 */
@Log4j2
@RequiredArgsConstructor
public class L1PubHandler extends BaseHandler {

    public static final int HQ_PUB_RATE = 1000;

    //缓存要发给每个柜台的数据
    //注意：【成交】 +【随着行情】一起发布出去，并不是有一个成交就立马丢出去
    //map : 柜台ID -> 要发给柜台的成交数据 列表
    @NonNull
    private final ShortObjectHashMap<List<MatchData>> matcherEventMap;

    @NonNull
    private EngineConfig config;


    @Override
    public void onEvent(RbCmd cmd, long sequence, boolean endOfBatch) throws Exception {
        final CmdType cmdType = cmd.command;

        if (cmdType == CmdType.NEW_ORDER || cmdType == CmdType.CANCEL_ORDER) {
            for (MatchEvent e : cmd.matchEventList) {
                matcherEventMap.get(e.mid).add(e.copy());
            }
        } else if (cmdType == CmdType.HQ_PUB) {
            //1.五档行情: 给所有人发
            pubMarketData(cmd.marketDataMap);
            //2.给柜台发送MatchData : 给某一个柜台单独发送消息
            pubMatcherData();
        }

    }

    private void pubMatcherData() {
        if (matcherEventMap.size() == 0) {
            return;
        }
        //线上不能有日志打印
        log.info(matcherEventMap);

        try {
            for (ShortObjectPair<List<MatchData>> s : matcherEventMap.keyValuesView()) {
                if (CollectionUtils.isEmpty(s.getTwo())) {
                    continue;
                }
                byte[] serialize = config.getBodyCodec().serialize(s.getTwo().toArray(new MatchData[0]));
                pubData(serialize, s.getOne(), MATCH_ORDER_DATA);

                //清空已发送数据
                s.getTwo().clear();

            }
        } catch (Exception e) {
            log.error(e);
        }

    }
    //规定往地址 -1 发送的,就是五档行情
    public static final short HQ_ADDRESS = -1;

    private void pubMarketData(IntObjectHashMap<L1MarketData> marketDataMap) {
        //线上不能有日志打印
        log.info(marketDataMap);
        byte[] serialize = null;
        try {
            serialize = config.getBodyCodec().serialize(marketDataMap.values().toArray(new L1MarketData[0]));
        } catch (Exception e) {
            log.error(e);
        }

        if (serialize == null) {
            return;
        }

        pubData(serialize, HQ_ADDRESS, MATCH_HQ_DATA);

    }

    private void pubData(byte[] serialize, short dst, short msgType) {
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(serialize.length);
        msg.setChecksum(config.getCs().getChecksum(serialize));
        msg.setMsgSrc(config.getId());
        msg.setMsgDst(dst);
        msg.setMsgType(msgType);
        msg.setStatus(NORMAL);
        msg.setBody(serialize);
        config.getBusSender().publish(msg);
    }
}
