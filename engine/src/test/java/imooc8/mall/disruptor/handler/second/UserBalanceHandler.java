package imooc8.mall.disruptor.handler.second;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:用户余额处理
 */

public class UserBalanceHandler implements EventHandler<MallOrderEvent> {
    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getSuccess()){
            System.out.println("用户余额扣除，userId: "+event.getUserId());
        }
    }
}
