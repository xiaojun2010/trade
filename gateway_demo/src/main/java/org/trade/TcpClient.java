package org.trade;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;


//主动重连
//按照规定的格式发送TCP数据
@Log4j2
public class TcpClient {

    public static void main(String[] args) {
        new TcpClient().startConn();
    }

    private Vertx vertx;

    public void startConn() {
        vertx = Vertx.vertx();
        vertx.createNetClient().connect(8091, "127.0.0.1",
                new ClientConnHandler()
        );
    }

    private class ClientConnHandler implements Handler<AsyncResult<NetSocket>> {
        @Override
        public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
            if (netSocketAsyncResult.succeeded()) {
                log.info("connect success");
                //发送消息
                NetSocket socket = netSocketAsyncResult.result();

                //关闭链接处理器
                socket.closeHandler(close -> {
                    log.info("connect to remote {} closed", socket.remoteAddress());
                    reConnect();
                });

                //异常处理器
                socket.exceptionHandler(e -> {
                    log.error("error exist", e);
                });

                //发送消息
                byte[] req = "Hello ,I am client ".getBytes();

                int bodyLength = req.length;

                Buffer buffer = Buffer.buffer()
                        .appendInt(bodyLength)
                        .appendBytes(req);
                socket.write(buffer);

            } else {
                log.error("connect failed", netSocketAsyncResult.cause());
                //重连：最好客户端发起重连，毕竟链接到服务端的链接数太多
                //如果服务端断开，客户端也断开，服务端再连接，客户端就无法连接了
                reConnect();
            }
        }

        private void reConnect() {
            vertx.setTimer(1000 * 5, r -> {
                log.info("try reconnect to server to 127.0.0.1:8091 failed");
                vertx.createNetClient()
                        .connect(8091, "127.0.0.1", new ClientConnHandler());
            });
        }
    }
}
