package com.wbchao.user;

import com.wbchao.user.bean.User;
import com.wbchao.user.dao.UserDao;
import com.wbchao.user.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTests {

    Logger log = LoggerFactory.getLogger(UserApplicationTests.class.getSimpleName());
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDao userDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void testUserRepository() {
        //        User user =new User();
        //        user.setId(UUID.randomUUID().toString());
        //        user.setUsername("wbchao");
        //        user.setAge(22);
        //        user.setEmail("abc@sohu.com");
        //        user.setActive(true);
        //
        //        userRepository.index(user);


        userRepository.findAll()
                      .forEach(u -> {
                          log.info("============================");
                          log.info(u.toString());
                          u.setActive(true);
                          userRepository.save(u);
                          userRepository.delete(u.getId());
                      });

        log.info("After: " + userRepository.findUsersByActiveIsTrue()
                                           .size());
    }

    @Test
    public void testUserDao() {
        User user = new User();
        user.setUsername("wbchao");
        user.setAge(22);
        user.setEmail("abc@sohu.com");

        userDao.addUser(user);
        user = userDao.findUsersByName("wb")
                      .get(0);
        log.info("===========================");
        log.info(user.toString());

        log.info("==========delete=============");
        userDao.deleteUser(user.getId());
        user = userDao.findUsersByName("wb")
                      .get(0);
        log.info(user.toString());
        userRepository.delete(userRepository.findAll());
    }

    @Test
    public void testFindUser() {
        List<User> users = userDao.findUsersByName("f");
        log.info("=============" + users.size() + "==========");
    }

    @Test
    public void testSendMsgToRabbitMq() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "wbchao");
        map.put("email", "s01@sohu.com");

        rabbitTemplate.convertAndSend("exchange.direct", "user.register", map);
    }

    @Test
    public void testReceiveMsgToRabbitMq() {
        Object o = rabbitTemplate.receiveAndConvert("user.register");
        log.info(o.getClass()
                  .getName());
        log.info(o.toString());
    }

    @Test
    public void testAmqpAdmin() {
        String exchangeName = "amqpAdmin.direct.exchange";
        String queueName = "amqpAdmin.queue";
        String routeKey = "user.register";
        amqpAdmin.declareExchange(new DirectExchange(exchangeName));
        amqpAdmin.declareQueue(new Queue(queueName, true));
        amqpAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routeKey, null));
        amqpAdmin.deleteQueue(queueName);
        amqpAdmin.deleteExchange(exchangeName);
    }
}
