package imooc8;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description: disruptor的ringBuffer中实际存放的数据，这个数据是由开发者自己定义的
 */
public class OrderEvent {
    private String id;//作为订单服务
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
