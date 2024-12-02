package imooc8.mall.disruptor.handler.third;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;
import imooc8.mall.service.order.OrderService;
import imooc8.mall.service.order.entity.OrderStatus;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class OrderStatusHandler implements EventHandler<MallOrderEvent> {

    OrderService orderService = new OrderService();
    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getSuccess()){
            //订单更新为成功
            orderService.updateStatus(event.getOrderId(),OrderStatus.SUCCESS);
        }else{
            //订单更新为失败
            orderService.updateStatus(event.getOrderId(),OrderStatus.FAILURE);
        }
    }
}
