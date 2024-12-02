package imooc8.multi.multiconsumer;

import com.lmax.disruptor.WorkHandler;
import imooc8.OrderEvent;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: 实现WorkHandler 重写onEvent方法
 */
public class OrderEventMultiHandler  implements WorkHandler<OrderEvent>{
    private String name;

    public OrderEventMultiHandler(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        System.out.println(event+"handler "+name);
    }
}
