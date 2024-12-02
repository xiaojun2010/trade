package imooc8;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class OrderEventProducer {
    //需要ringBuffer发送事件
    private final RingBuffer<OrderEvent> ringBuffer;
    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    public static final EventTranslatorOneArg<OrderEvent,String> translatorOneArg = new EventTranslatorOneArg<OrderEvent, String>() {
        @Override
        public void translateTo(OrderEvent orderEvent, long sequence, String data) {
            orderEvent.setId(data);
        }
    };

    public static final EventTranslatorTwoArg <OrderEvent,String,String> trasnlatorTwoAry = new EventTranslatorTwoArg<OrderEvent, String, String>() {
        @Override
        public void translateTo(OrderEvent event, long sequence, String arg0, String arg1) {
            event.setId(arg0);
            event.setUserName(arg1);
        }
    };
    /**
     * 通知disruptor发送事件
     * @param orderId
     */
    public void onData(String orderId) {
        long sequence = ringBuffer.next();
        try{
            OrderEvent orderEvent = ringBuffer.get(sequence);
            orderEvent.setId(orderId);
        }finally {
            ringBuffer.publish(sequence);
        }
    }

    public void onDataWithTranslator(String orderId) {
        ringBuffer.publishEvent(translatorOneArg,orderId);
    }
    public void  onDataWithTranslator2(String orderId,String name) {
        ringBuffer.publishEvent(trasnlatorTwoAry,orderId,name);
    }
}
