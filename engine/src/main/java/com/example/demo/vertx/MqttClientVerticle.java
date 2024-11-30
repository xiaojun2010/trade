package com.example.demo.vertx;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MqttClientVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MqttClientVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        MqttClientOptions options = new MqttClientOptions().setUsername("admin").setPassword("admin")
                .setAutoKeepAlive(true);

        MqttClient mqttClient = MqttClient.create(vertx, options);

        mqttClient.connect(1883, "127.0.0.1", ar -> {
            if (ar.succeeded()) {
                System.out.println("Connected to the MQTT broker");

                // 订阅主题
                mqttClient.subscribe("/cmd/A99", 0, subAck -> {
                    if (subAck.succeeded()) {
                        System.out.println("Subscribed to topic");
                    } else {
                        System.err.println("Failed to subscribe to topic");
                    }
                }).publishHandler(s -> {
                    System.out.println("There are new message in topic: " + s.topicName());
                    System.out.println("Content(as string) of the message: " + s.payload().toString());
                    System.out.println("QoS: " + s.qosLevel());
                });

                // 发布消息
                mqttClient.publish("/dev/response", Buffer.buffer("Hello, MQTT!"), MqttQoS.AT_LEAST_ONCE, false, false, pubAck -> {
                    if (pubAck.succeeded()) {
                        System.out.println("Message published");
                    } else {
                        System.err.println("Failed to publish message");
                    }
                });
            } else {
                System.err.println("Failed to connect to the MQTT broker");
            }
        });


    }

}