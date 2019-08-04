package com.wbchao.user.listener;

import com.wbchao.user.config.RabbitMqConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqApplicationRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(RabbitMqApplicationRunner.class.getName());

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    /**
     * Create the exchange and queue on Rabbit MQ
     * If already exist, will be no influence
     *
     * @param applicationArguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("Create exchange, queue on Rabbit MQ...");
        String exchangeName = rabbitMqConfig.getExchange();
        String queueName = rabbitMqConfig.getQueue();
        String routekey = rabbitMqConfig.getRoutekey();
        amqpAdmin.declareExchange(new DirectExchange(exchangeName));
        amqpAdmin.declareQueue(new Queue(queueName, true));
        amqpAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routekey, null));

    }
}
