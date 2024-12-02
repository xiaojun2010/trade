package imooc8.mall.service.order.entity;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class Order {
    private String orderId;
    /**
     * @see OrderStatus
     */
    private int status;

    public Order(String orderId, int status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
