package imooc8.mall.entity;

import java.util.UUID;

/**
 * 购买商品的请求
 */
public class ResultDto {

    /**
     * 商品id
     */
    private Long itemId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 订单id
     */
    private String orderId;

    public ResultDto(Long itemId, String userId) {
        this.itemId = itemId;
        this.userId = userId;
        this.orderId = UUID.randomUUID().toString();
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
}
