package imooc8.multi;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import imooc8.MyEventFactory;
import imooc8.OrderEvent;
import imooc8.OrderEventHandler;
import imooc8.OrderEventProducer;

import java.util.concurrent.*;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: 多生产者模式
 */
public class MultiDemo {
    public static void main(String[] args) throws InterruptedException {
        //创建Disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<>(new MyEventFactory(),1024, Executors.defaultThreadFactory(), ProducerType.MULTI,new YieldingWaitStrategy());
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(10,20,0,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        //定义处理事件
        disruptor.handleEventsWith(new OrderEventHandler());
        //启动
        disruptor.start();

        //生产
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; i < 10; i++) {
            OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
            int finalI1 = i;
            executorService.execute(()->{
                for (int j = 0; j < 10; j++) {
                    orderEventProducer.onDataWithTranslator(finalI1 +"-"+j);
                }
            });
        }
        Thread.sleep(2000);
        disruptor.shutdown();
        executorService.shutdown();
    }
}
