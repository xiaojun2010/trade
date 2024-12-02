package imooc8.pipeline;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import imooc8.MyEventFactory;
import imooc8.OrderEvent;
import imooc8.OrderEventProducer;

import java.util.concurrent.Executors;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class Pipeliner1 {
    public static void main(String[] args) {
        //创建disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(new MyEventFactory(),
                1024, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        //定义disruptor中的消费者
        //handler1 handler2 -> handler3
        EventHandler<OrderEvent> eventEventHandler1 = (event,sequence,endOfBatch)->{
            System.out.println("handler1执行了");
            Thread.sleep(2000);
        };
        EventHandler<OrderEvent> eventEventHandler2 = (event,sequence,endOfBatch)->{
            System.out.println("handler2执行了");
            Thread.sleep(2000);
        };

        EventHandler<OrderEvent> eventEventHandler3 = (event,sequence,endOfBatch)->{
            System.out.println("handler3执行了");
            Thread.sleep(2000);
        };
        disruptor.handleEventsWith(eventEventHandler1,eventEventHandler2)
                .then(eventEventHandler3);

        //启动disruptor
        disruptor.start();
        //发送事件
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        orderEventProducer.onDataWithTranslator("TestOrderId");
    }
}
