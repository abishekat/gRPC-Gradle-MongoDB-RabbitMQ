package com.listener.rabbitlistener;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQExchangeConfigurations {
    @Bean
    Exchange topicExchange(){
        return ExchangeBuilder.topicExchange("EgExchange")
                .autoDelete()
                .durable(true)
                .internal()
                .build();
    }

}
