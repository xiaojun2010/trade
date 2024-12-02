package imooc8.mall.disruptor.entity;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: disruptor的事件类，-> 下单事件
 */
public class MallOrderEvent {

    //商品id
    private Long itemId;
    //用户id
    private String userId;
    //订单id
    private String orderId;
    //订单是否下单成功
    private Boolean success;

    public MallOrderEvent() {
    }

    public MallOrderEvent(Long itemId, String userId, String orderId) {
        this.itemId = itemId;
        this.userId = userId;
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
