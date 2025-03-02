package com.pro.config;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitmqConfig {

    private final RabbitMqProperties rabbitMQProperties;

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange("bank.exchange");
    }

    @Bean
    Queue queue1(){
        return new Queue("bank.queue1", false);
    }

    @Bean
    Queue queue2(){
        return new Queue("bank.queue2",false);
    }

    @Bean
    Binding bindingQueue1(DirectExchange directExchange, Queue queue1){
        return BindingBuilder.bind(queue1).to(directExchange).with("bank.key1");
    }

    @Bean
    Binding bindingQueue2(DirectExchange directExchange, Queue queue2){
        return BindingBuilder.bind(queue2).to(directExchange).with("bank.key2");
    }

    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMQProperties.getHost());
        connectionFactory.setPort(rabbitMQProperties.getPort());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}
