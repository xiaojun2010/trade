package imooc8.mall.disruptor.config;


import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import imooc8.mall.disruptor.entity.MallOrderEvent;

import java.util.concurrent.Executors;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: Disruptor相关配置
 */
public class DisruptorConfig {
    static Disruptor<MallOrderEvent> disruptor =
            new Disruptor<MallOrderEvent>(MallOrderEvent::new,1024, Executors.defaultThreadFactory(),
            ProducerType.MULTI,new YieldingWaitStrategy());
    static RingBuffer<MallOrderEvent> ringBuffer = disruptor.getRingBuffer();

    public static RingBuffer<MallOrderEvent> getRingBuffer() {
        return ringBuffer;
    }

    public static void setRingBuffer(RingBuffer<MallOrderEvent> ringBuffer) {
        DisruptorConfig.ringBuffer = ringBuffer;
    }

    public static Disruptor<MallOrderEvent> getDisruptor() {
        return disruptor;
    }
}
