package imooc8.mall.service.order.entity;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public enum OrderStatus {
    PENDING(0,"执行中"),
    SUCCESS(1,"成功"),
    FAILURE(2,"失败")
    ;

    int status;
    String desc;

    OrderStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }
    public static OrderStatus getByStatus(int status){
        for (OrderStatus value : OrderStatus.values()) {
            if(value.status==status){
                return value;
            }
        }
        return null;
    }
}
