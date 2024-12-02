package imooc8.mall.disruptor.handler;

import imooc8.mall.disruptor.config.DisruptorConfig;
import imooc8.mall.disruptor.handler.first.InventoryReduceHandler;
import imooc8.mall.disruptor.handler.first.MallOrderLogTraceHandler;
import imooc8.mall.disruptor.handler.first.StoreOrderHandler;
import imooc8.mall.disruptor.handler.second.CouponHandler;
import imooc8.mall.disruptor.handler.second.UserBalanceHandler;
import imooc8.mall.disruptor.handler.third.OrderStatusHandler;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class OrderDisruptorHandlerConfig {

    static {
        DisruptorConfig.getDisruptor().handleEventsWith(new MallOrderLogTraceHandler(),new InventoryReduceHandler(),new StoreOrderHandler())
                .then(new CouponHandler(),new UserBalanceHandler())
                .then(new OrderStatusHandler());
        DisruptorConfig.getDisruptor().start();
    }

}
