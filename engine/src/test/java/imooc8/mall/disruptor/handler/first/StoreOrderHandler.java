package imooc8.mall.disruptor.handler.first;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;
import imooc8.mall.service.order.OrderService;
import imooc8.mall.service.order.entity.Order;
import imooc8.mall.service.order.entity.OrderStatus;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:存储订单
 */
public class StoreOrderHandler implements EventHandler<MallOrderEvent> {

    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        Order order = new Order(event.getOrderId(), OrderStatus.PENDING.getStatus());
        OrderService.storeOrder(order);
    }
}
