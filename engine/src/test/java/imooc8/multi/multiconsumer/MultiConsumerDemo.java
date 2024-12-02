package imooc8.multi.multiconsumer;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkerPool;
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
 * @Description: 多消费者
 */
public class MultiConsumerDemo {
    public static void main(String[] args) {
        //创建disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(new MyEventFactory(),
                1024, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        //创建handler数组
        OrderEventMultiHandler[] orderEventMultiHandlers = new OrderEventMultiHandler[10];
        for (int i = 0; i < orderEventMultiHandlers.length; i++) {
            orderEventMultiHandlers[i] = new OrderEventMultiHandler("name "+i);
        }
        //定义disruptor handler
        disruptor.handleEventsWithWorkerPool(orderEventMultiHandlers);
        //启动
        disruptor.start();
        //生产
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        for (int i = 0; i < 100; i++) {
            orderEventProducer.onDataWithTranslator(String.valueOf(i));
        }
    }
}
