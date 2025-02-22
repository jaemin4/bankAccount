package com.rabbitmq.bankService.consumer.service;


import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import com.rabbitmq.bankService.consumer.repository.AccessLogEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final AccessLogEntityRepository accessLogEntityRepository;

    @RabbitListener(queues = "hello.queue")
    public void receiveLogData(AccessLogEntity accessLogEntity){
        log.info("bankaccount/consumer/consumerService 메세지: {}", accessLogEntity);
        accessLogEntityRepository.save(accessLogEntity);
        log.info("\n bankaccount/consumer/consumberService/receiveLogData : save");

    }




}
