package thirdpart.bus;

import thirdpart.bean.CommonMsg;

public interface IBusSender {
    //启动
    void startup();
    //发布消息
    //通讯数据 必须使用 标准模版
    void publish(CommonMsg commonMsg);

}
