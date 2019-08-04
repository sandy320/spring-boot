package com.wbchao.user.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserNotificationApplicationTests {

    @Autowired
    JavaMailSenderImpl sender;

    @Test
    public void testSendMail() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("sandy320@sohu.com");
        mail.setSubject("User register successfully!");
        mail.setText("Welcome, your account is registered successfully");
        mail.setFrom("2308733841@qq.com");

        sender.send(mail);
    }

}
