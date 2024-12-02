package imooc8.mall.controller;


import imooc8.mall.disruptor.config.DisruptorConfig;
import imooc8.mall.disruptor.handler.OrderDisruptorHandlerConfig;
import imooc8.mall.disruptor.producer.OrderProducer;
import imooc8.mall.entity.ResultDto;
import imooc8.mall.service.order.OrderService;
import imooc8.mall.service.order.entity.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class MallController {

    private OrderProducer orderProducer = new OrderProducer(DisruptorConfig.getRingBuffer());

    private ConcurrentMap<String,ResultDto> concurrentMap = new ConcurrentHashMap<>();

    private OrderService orderService = new OrderService();

    private static DisruptorConfig disruptorConfig = new DisruptorConfig();

    private static OrderDisruptorHandlerConfig orderDisruptorHandlerConfig = new OrderDisruptorHandlerConfig();

    @Test
    public void testTakeOrder() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(50,50,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));
        CountDownLatch countDownLatch = new CountDownLatch(50);
        CountDownLatch countDownLatch1 = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            executorService.execute(()->{
                ResultDto resultDto = takeOrder(1L, String.valueOf(finalI));
                concurrentMap.put(resultDto.getOrderId(),resultDto);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        concurrentMap.forEach((k,v)->{
            executorService.execute(()->{
                while (true){
                    OrderStatus orderStatus = this.getOrderStatus(k);
                    if(orderStatus!=OrderStatus.PENDING){
                        System.out.println("orderId: "+k+" orderStatus: "+orderStatus);
                        countDownLatch1.countDown();
                        break;
                    }
                }
            });
        });
        countDownLatch1.await();
        System.out.println("-----");
    }
    /**
     * 客户请求下单
     * @return ResultDto
     */
    public ResultDto takeOrder(Long itemId, String userId){
        if(Objects.isNull(itemId)||itemId<0){
            throw new RuntimeException("itemId不合法");
        }
        if(StringUtils.isEmpty(userId)){
            throw new RuntimeException("userId不合法");
        }
        ResultDto resultDto = new ResultDto(itemId,userId);
        orderProducer.onData(itemId,userId,resultDto.getOrderId());
        return resultDto;
    }

    public OrderStatus getOrderStatus(String orderId){
        return orderService.getOrderStatus(orderId);
    }
}
