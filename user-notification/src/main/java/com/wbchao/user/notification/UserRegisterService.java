package com.wbchao.user.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserRegisterService {

    Logger logger = LoggerFactory.getLogger(UserRegisterService.class.getName());

    @Autowired
    Environment env;

    @Autowired
    JavaMailSenderImpl sender;

    @Bean
    public MessageConverter rabbitMqMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = "amqpAdmin.direct.queue")
    public void receiveUserRegisterNotification(Map<String, String> info) {
        logger.info("Receive user register notification: " + info.toString());
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(info.get("email"));
        mail.setSubject("User " + info.get("username") + " register successfully!");
        mail.setText("Welcome, " + info.get("username") + "! Your account is registered successfully");
        mail.setFrom(env.getProperty("spring.mail.username"));

        sender.send(mail);
    }
}
