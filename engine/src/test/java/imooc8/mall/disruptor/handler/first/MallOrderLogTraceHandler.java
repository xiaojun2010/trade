package imooc8.mall.disruptor.handler.first;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:记录执行日志，方便排查问题
 */
public class MallOrderLogTraceHandler implements EventHandler<MallOrderEvent> {
    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("用户id为："+event.getUserId()+", 正在下单，下单的商品id为："+event.getItemId()+"订单号为："+event.getOrderId());
    }
}
