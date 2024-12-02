package imooc8;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class DisruptorDemo {
    public static void main(String[] args) throws InterruptedException {
        //创建disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(new MyEventFactory(),
                1024, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        //定义disruptor中的消费者
        disruptor.handleEventsWith(new OrderEventHandler());
        //启动disruptor
        disruptor.start();
        //发送事件
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        for (int i = 0; i < 100; i++) {
//            orderEventProducer.onData(String.valueOf(i));
//            orderEventProducer.onDataWithTranslator(String.valueOf(i));
            orderEventProducer.onDataWithTranslator2(String.valueOf(i),i+"name");
        }
    }
}
