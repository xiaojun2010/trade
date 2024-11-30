package com.gudy.engine.bean.orderbook;

import com.google.common.collect.Lists;
import com.gudy.engine.bean.command.CmdResultCode;
import com.gudy.engine.bean.command.RbCmd;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;
import thirdpart.hq.L1MarketData;
import thirdpart.order.OrderDirection;
import thirdpart.order.OrderStatus;

import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

@Log4j2
@RequiredArgsConstructor
public class GOrderBookImpl implements IOrderBook {

    @NonNull
    private int code;

    //<价格,orderbucket>
    private final NavigableMap<Long, IOrderBucket> sellBuckets = new TreeMap<>();
    private final NavigableMap<Long, IOrderBucket> buyBuckets = new TreeMap<>(Collections.reverseOrder());

    private final LongObjectHashMap<Order> oidMap = new LongObjectHashMap<>();

    @Override
    public CmdResultCode newOrder(RbCmd cmd) {

        //1.判断重复
        if (oidMap.containsKey(cmd.oid)) {
            return CmdResultCode.DUPLICATE_ORDER_ID;
        }

        //2.生成新Order
        //2.1 预撮合
        // S 50 100  买单Buckets >=50 所有OrderBucket
        // B 40 200  卖单Buckets <=40 符合条件
        NavigableMap<Long, IOrderBucket> subMatchBuckets =
                (cmd.direction == OrderDirection.SELL ? buyBuckets : sellBuckets)
                        .headMap(cmd.price, true);
        long tVolume = preMatch(cmd, subMatchBuckets);
        if (tVolume == cmd.volume) {
            return CmdResultCode.SUCCESS;
        }

        final Order order = Order.builder()
                .mid(cmd.mid)
                .uid(cmd.uid)
                .code(cmd.code)
                .direction(cmd.direction)
                .price(cmd.price)
                .volume(cmd.volume)
                .tvolume(tVolume)
                .oid(cmd.oid)
                .timestamp(cmd.timestamp)
                .build();

        if (tVolume == 0) {
            genMatchEvent(cmd, OrderStatus.ORDER_ED);
        } else {
            genMatchEvent(cmd, OrderStatus.PART_TRADE);
        }

        //3.加入orderBucket
        final IOrderBucket bucket = (cmd.direction == OrderDirection.SELL ? sellBuckets : buyBuckets)
                .computeIfAbsent(cmd.price, p -> {
                    final IOrderBucket b = IOrderBucket.create(IOrderBucket.OrderBucketImplType.GUDY);
                    b.setPrice(p);
                    return b;
                });
        bucket.put(order);
        oidMap.put(cmd.oid, order);

        return CmdResultCode.SUCCESS;
    }

    private long preMatch(RbCmd cmd, NavigableMap<Long, IOrderBucket> matchingBuckets) {
        int tVol = 0;
        if (matchingBuckets.size() == 0) {
            return tVol;
        }

        List<Long> emptyBuckets = Lists.newArrayList();
        for (IOrderBucket bucket : matchingBuckets.values()) {

            tVol += bucket.match(cmd.volume - tVol, cmd,
                    order -> oidMap.remove(order.getOid()));

            if (bucket.getTotalVolume() == 0) {
                emptyBuckets.add(bucket.getPrice());
            }

            if (tVol == cmd.volume) {
                break;
            }

        }

        emptyBuckets.forEach(matchingBuckets::remove);

        return tVol;

    }

    /**
     * 生成matchevent
     *
     * @param cmd
     * @param status
     */
    private void genMatchEvent(RbCmd cmd, OrderStatus status) {
        long now = System.currentTimeMillis();
        MatchEvent event = new MatchEvent();
        event.timestamp = now;
        event.mid = cmd.mid;
        event.oid = cmd.oid;
        event.status = status;
        event.volume = 0;
        cmd.matchEventList.add(event);
    }

    @Override
    public CmdResultCode cancelOrder(RbCmd cmd) {
        //1.从缓存中移除委托
        Order order = oidMap.get(cmd.oid);
        if (order == null) {
            return CmdResultCode.INVALID_ORDER_ID;
        }
        oidMap.remove(order.getOid());

        //2.从orderbucket中移除委托
        final NavigableMap<Long, IOrderBucket> buckets =
                order.getDirection() == OrderDirection.SELL ? sellBuckets : buyBuckets;
        IOrderBucket orderBucket = buckets.get(order.getPrice());
        orderBucket.remove(order.getOid());
        if (orderBucket.getTotalVolume() == 0) {
            buckets.remove(order.getPrice());
        }

        //3.发送撤单MatchEvent
        MatchEvent cancelEvent = new MatchEvent();
        cancelEvent.timestamp = System.currentTimeMillis();
        cancelEvent.mid = order.getMid();
        cancelEvent.oid = order.getOid();
        cancelEvent.status = order.getTvolume() == 0 ? OrderStatus.CANCEL_ED : OrderStatus.PART_CANCEL;
        cancelEvent.volume = order.getTvolume() - order.getVolume();
        cmd.matchEventList.add(cancelEvent);


        return CmdResultCode.SUCCESS;
    }

    @Override
    public void fillCode(L1MarketData data) {
        data.code = code;
    }

    @Override
    public void fillSells(int size, L1MarketData data) {
        if (size == 0) {
            data.sellSize = 0;
            return;
        }

        int i = 0;
        for (IOrderBucket bucket : sellBuckets.values()) {
            data.sellPrices[i] = bucket.getPrice();
            data.sellVolumes[i] = bucket.getTotalVolume();
            if (++i == size) {
                break;
            }
        }

        data.sellSize = i;

    }

    @Override
    public void fillBuys(int size, L1MarketData data) {
        if (size == 0) {
            data.buySize = 0;
            return;
        }

        int i = 0;
        for (IOrderBucket bucket : buyBuckets.values()) {
            data.buyPrices[i] = bucket.getPrice();
            data.buyVolumes[i] = bucket.getTotalVolume();
            if (++i == size) {
                break;
            }
        }

        data.buySize = i;
    }

    @Override
    public int limitBuyBucketSize(int maxSize) {
        return Math.min(maxSize, buyBuckets.size());
    }

    @Override
    public int limitSellBucketSize(int maxSize) {
        return Math.min(maxSize, sellBuckets.size());
    }
}
