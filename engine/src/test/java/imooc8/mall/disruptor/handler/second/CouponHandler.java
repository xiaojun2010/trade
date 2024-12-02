package imooc8.mall.disruptor.handler.second;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:优惠券处理
 */
public class CouponHandler implements EventHandler<MallOrderEvent> {
    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getSuccess()){
            System.out.println("扣减优惠券，userId: "+event.getUserId());
        }
    }
}
