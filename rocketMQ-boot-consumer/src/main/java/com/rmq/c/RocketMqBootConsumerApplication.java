package com.rmq.c;

import com.rmq.c.entity.MsgModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RocketMqBootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketMqBootConsumerApplication.class, args);
    }

}
