package com.wbchao.user.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rabbitmq.properties")
@ConfigurationProperties(prefix = "custom.rabbitmq")
public class RabbitMqConfig {
    private String exchange;
    private String queue;
    private String routekey;

    @Bean
    public MessageConverter rabbitMqMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutekey() {
        return routekey;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setRoutekey(String routekey) {
        this.routekey = routekey;
    }
}
