package com.gudy.gateway;

import com.gudy.gateway.bean.GatewayConfig;
import lombok.extern.log4j.Log4j2;
import thirdpart.checksum.ByteCheckSum;
import thirdpart.codec.BodyCodec;

import java.io.FileInputStream;
import java.io.InputStream;

@Log4j2
public class GatewayStartup {
    public static void main(String[] args) throws Exception {
        String configFileName = "gateway.xml";

        GatewayConfig config = new GatewayConfig();

        //输入流
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir")
                    + "\\" + configFileName);
            log.info("gateway.xml exist in jar path");
        } catch (Exception e) {
            inputStream = GatewayStartup.class.getResourceAsStream("/" + configFileName);
            log.info("gateway.xml exist in jar file");
        }

        config.initConfig(inputStream);
//        config.initConfig(GatewayStartup.class.getResource("/").getPath()
//                + configFileName);
        config.setCs(new ByteCheckSum());
        config.setBodyCodec(new BodyCodec());
        config.startup();
    }
}
