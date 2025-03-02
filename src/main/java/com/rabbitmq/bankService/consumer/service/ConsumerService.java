package com.rabbitmq.bankService.consumer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import com.rabbitmq.bankService.consumer.repository.AccessLogEntityRepository;
import com.rabbitmq.bankService.consumer.repository.AccessLogJdbcRepository;
import jakarta.annotation.PreDestroy;
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
    private final AccessLogJdbcRepository accessLogJdbcRepository;
    private final ObjectMapper objectMapper;

    private final List<AccessLogEntity> logBuffer = new ArrayList<>();
    private static final int BATCH_SIZE = 15;

    @Transactional
    @RabbitListener(queues = "hello.queue")
    public void receiveLogData(AccessLogEntity accessLogEntity) throws JsonProcessingException {
        log.info("bankaccount/consumer/consumerService 메세지: {}", objectMapper.writeValueAsString(accessLogEntity));
        logBuffer.add(accessLogEntity);

        // todo 사이즈가 15개 이상이면 저장하게되는데요. 만약에 14개 이하일 때 서버를 내리게 되면 버퍼에 들어가 있던게 어떻게 될까요 ?
        //  그래서 Gracefully shutdown 로직을 구현해야 합니다.
        //  shutdown 시에는 현재 버퍼에 있는 데이터를 모두 저장하고 종료해야 합니다.
        if(logBuffer.size() >= BATCH_SIZE){
            accessLogJdbcRepository.saveAll(logBuffer);
            logBuffer.clear();
        }

        log.info("\n bankaccount/consumer/consumberService/receiveLogData : save");

    }


    @PreDestroy
    public void onShutdown() {
        log.info("Graceful Shutdown 시작 - 버퍼 데이터 저장 중...");

        if (!logBuffer.isEmpty()) {
            try {
                accessLogJdbcRepository.saveAll(logBuffer);
                log.info("Graceful Shutdown 버퍼 데이터 저장 성공");
                logBuffer.clear();
            } catch (Exception e) {
                log.error("Graceful Shutdown 중 데이터 저장 실패: {}", e.getMessage(), e);
            }
        } else {
            log.info("Graceful Shutdown 완료 - 저장할 데이터 없음");
        }
    }



}
