package com.wbchao.user.notification;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class UserNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserNotificationApplication.class, args);
    }

}
