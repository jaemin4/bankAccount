package com.pro.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;


    /**
     * 1. Exchange 구성
     * "hello.exchange" 라는 이름으로 Direct Exchange 형태로 구성
     *
     * @return DirectExchange
     */

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange("hello.exchange");
    }

    /**
     * 2. 큐를 구성
     * "hello.queue"라는 이름으로 큐를 구성
     *
     * @return Queue
     */

    @Bean
    Queue queue(){
        return new Queue("hello.queue", false);
    }


    /**
     * 3. 큐와 DirectExchange를 바인딩
     * "hello.key"라는 이름으로 바인딩을 구성
     *
     * @param directExchange
     * @param queue
     * @return Binding
     */

    @Bean
    Binding binding(DirectExchange directExchange, Queue queue){
        return BindingBuilder.bind(queue).to(directExchange).with("hello.key");
    }

    /**
     * 4. RabbitMQ와의 연결을 위한 ConnectionFactory을 구성
     * Application.properties의 RabbitMQ의 사용자 정보를 가져와서 RabbitMQ와의 연결에 필요한 ConnectionFactory를 구성
     *
     * @return ConnectionFactory
     */

    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * 5. 메시지를 전송하고 수신하기 위한 JSON 타입으로 메시지를 변경
     * Jackson2JsonMessageConverter를 사용하여 메시지 변환을 수행 JSON 형식으로 메시지를 전송하고 수신할 수 있다.
     *
     * @return
     */

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 6. 구성한 ConnectionFactory, MessageConverter를 통해 템플릿을 구성
     *
     * @param connectionFactory
     * @param messageConverter
     * @return
     */

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}
