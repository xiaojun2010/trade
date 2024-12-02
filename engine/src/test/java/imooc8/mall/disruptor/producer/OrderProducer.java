package imooc8.mall.disruptor.producer;

import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import imooc8.mall.disruptor.entity.MallOrderEvent;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class OrderProducer {

    private RingBuffer<MallOrderEvent> ringBuffer;
    private static final EventTranslatorThreeArg<MallOrderEvent,Long,String,String> eventTranslatorThreeArg = (mallOrderEvent,sequence,itemId,userId,orderId)->{
        mallOrderEvent.setItemId(itemId);
        mallOrderEvent.setUserId(userId);
        mallOrderEvent.setOrderId(orderId);
    };

    public OrderProducer(RingBuffer<MallOrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(Long itemId, String userId, String orderId){
        ringBuffer.publishEvent(eventTranslatorThreeArg,itemId,userId,orderId);
    }
}
