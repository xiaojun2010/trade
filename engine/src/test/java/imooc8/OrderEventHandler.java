package imooc8;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: event的处理器，消费者，handler，实现EventHandler接口，重写onEvent
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        log.info("event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch);
    }

}
