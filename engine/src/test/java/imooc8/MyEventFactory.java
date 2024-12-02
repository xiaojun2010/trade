package imooc8;

import com.lmax.disruptor.EventFactory;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: 需要让disruptor为我们创建时间，实现成EventFactory类，重写newInstance方法
 */
public class MyEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
