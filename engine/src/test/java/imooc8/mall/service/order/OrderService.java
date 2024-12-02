package imooc8.mall.service.order;

import imooc8.mall.service.order.entity.Order;
import imooc8.mall.service.order.entity.OrderStatus;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:订单服务
 */
public class OrderService {
    public static ConcurrentMap<String,Order> concurrentMap = new ConcurrentHashMap<>();

    /**
     * 存储订单
     * @param order
     */
    public static boolean storeOrder(Order order) {
        if(Objects.isNull(order)){
            return false;
        }
        concurrentMap.putIfAbsent(order.getOrderId(),order);
        return true;
    }

    public void updateStatus(String orderId, OrderStatus orderStatus) {
        Order order = concurrentMap.get(orderId);
        if(Objects.isNull(order)){
            return;
        }
        order.setStatus(orderStatus.getStatus());
    }

    public OrderStatus getOrderStatus(String orderId) {
        Order order = concurrentMap.get(orderId);
        if(Objects.isNull(order)){
            return OrderStatus.FAILURE;
        }
        return OrderStatus.getByStatus(order.getStatus());
    }
}
