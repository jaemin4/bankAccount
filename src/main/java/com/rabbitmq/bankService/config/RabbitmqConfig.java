package com.rabbitmq.bankService.config;

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

    private final RabbitMqProperties rabbitMqProperties;
    @Bean
    DirectExchange directExchange(){
        return new DirectExchange("bank.exchange");
    }

    @Bean
    Queue queue1(){
        return new Queue("bank.log.access", false);
    }

    @Bean
    Queue queue2(){
        return new Queue("bank.log.deposit",false);
    }

    @Bean
    Queue queue3(){
        return new Queue("bank.log.withdraw",false);
    }

    @Bean
    Queue queue4(){
        return new Queue("bank.log.transfer",false);
    }


    @Bean
    Binding bindingQueue1(DirectExchange directExchange, Queue queue1){
        return BindingBuilder.bind(queue1).to(directExchange).with("bank.log.access");
    }

    @Bean
    Binding bindingQueue2(DirectExchange directExchange, Queue queue2){
        return BindingBuilder.bind(queue2).to(directExchange).with("bank.log.deposit");
    }

    @Bean
    Binding bindingQueue3(DirectExchange directExchange, Queue queue3){
        return BindingBuilder.bind(queue3).to(directExchange).with("bank.log.withdraw");
    }

    @Bean
    Binding bindingQueue4(DirectExchange directExchange, Queue queue4){
        return BindingBuilder.bind(queue4).to(directExchange).with("bank.log.transfer");
    }

    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
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
