package org.trade;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.log4j.Log4j2;



@Log4j2
public class TcpServer {
    public static void main(String[] args) {
        new TcpServer().startServer();
    }

    public void startServer() {
        log.info("start server");

        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new ConnHandler());
        netServer.listen(8091, res -> {
            if (res.succeeded()){
                log.info("server start success at port 8091");
            }else {
                log.error("server start failed",res.cause());
            }
        });


    }

    private class ConnHandler implements Handler<NetSocket> {
        //报文
        //报头[int 报文长度]
        //包体[byte[] 报文内容]
        private static final int PACKET_HEADER_LENGTH = 4;

        @Override
        public void handle(NetSocket netSocket) {
            //1.parser 自定义解析器
            final RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);
            parser.setOutput(new Handler<Buffer>() {
                //    包头[ 包体长度 int + 校验和 byte + src short+ dst short + 消息类型 short + 消息状态 byte + 包编号 long ]
                int bodyLength = -1;
                @Override
                public void handle(Buffer buffer) {
                    if (bodyLength == -1) {
                        //读取包头
                        bodyLength = buffer.getInt(0);
                        parser.fixedSizeMode(bodyLength);
                    }else {
                        //读取包体 数据
                        byte[] bodyBytes = buffer.getBytes();
                        log.info("get msg from client:{} , msg :{}", netSocket.remoteAddress(),new String(bodyBytes));

                        //恢复现场，读取下一个报文
                        parser.fixedSizeMode(PACKET_HEADER_LENGTH);
                        bodyLength = -1;
                    }
                }
            });


            //2.设置解析器
            netSocket.handler(parser);
        }
    }
}