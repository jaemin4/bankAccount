package com.rabbitmq.bankService.consumer.service;


import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import com.rabbitmq.bankService.consumer.repository.AccessLogEntityRepository;
import com.rabbitmq.bankService.consumer.repository.AccessLogJdbcRepository;
import jakarta.persistence.Access;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final AccessLogEntityRepository accessLogEntityRepository;
    private final List<AccessLogEntity> logBuffer = new ArrayList<>();
    private static final int BATCH_SIZE = 15;
    private final AccessLogJdbcRepository accessLogJdbcRepository;


    @Transactional
    @RabbitListener(queues = "hello.queue")
    public void receiveLogData(AccessLogEntity accessLogEntity){
        log.info("bankaccount/consumer/consumerService 메세지: {}", accessLogEntity.toString());
        logBuffer.add(accessLogEntity);

        if(logBuffer.size() >= BATCH_SIZE){
            accessLogJdbcRepository.saveAll(logBuffer);
            logBuffer.clear();

        }

        log.info("\n bankaccount/consumer/consumberService/receiveLogData : save");

    }







}
