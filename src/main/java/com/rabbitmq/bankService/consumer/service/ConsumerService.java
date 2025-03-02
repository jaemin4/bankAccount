package com.rabbitmq.bankService.consumer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.bankService.service.BalanceLogParam;
import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import com.rabbitmq.bankService.consumer.entity.BankBalanceLogEntity;
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

    private static final Integer BATCH_SIZE = 15;
    private final List<AccessLogEntity> listAccessLogEntity = new ArrayList<>();
    private final List<BankBalanceLogEntity> listBankBalanceLogEntity = new ArrayList<>();


    @Transactional
    @RabbitListener(queues = "bank.queue1")
    public void queueOneProccess(AccessLogEntity accessLogEntity) throws JsonProcessingException {
        String fullMethodName = this.getClass().getSimpleName() + "." + new Object() {}.getClass().getEnclosingMethod().getName();
        log.info("{} 메세지 : {}",fullMethodName, objectMapper.writeValueAsString(accessLogEntity));
        listAccessLogEntity.add(accessLogEntity);

        if(listAccessLogEntity.size() >= BATCH_SIZE){
            accessLogJdbcRepository.saveAll(listAccessLogEntity);
            listAccessLogEntity.clear();
        }

        log.info("\n {} : save", fullMethodName);

    }

    @Transactional
    @RabbitListener(queues = "bank.queue2")
    public void queueTwoProccess(BalanceLogParam balanceLogParam) throws JsonProcessingException {
        String fullMethodName = this.getClass().getSimpleName() + "." + new Object() {}.getClass().getEnclosingMethod().getName();
        log.info("{} 메세지 : {}",fullMethodName,objectMapper.writeValueAsString(balanceLogParam));
    }



    @PreDestroy
    public void onShutdown() {
        log.info("Graceful Shutdown 시작 - 버퍼 데이터 저장 중...");

        if (!listAccessLogEntity.isEmpty()) {
            try {
                accessLogJdbcRepository.saveAll(listAccessLogEntity);
                log.info("Graceful Shutdown 버퍼 데이터 저장 성공");
                listAccessLogEntity.clear();
            } catch (Exception e) {
                log.error("Graceful Shutdown 중 데이터 저장 실패: {}", e.getMessage(), e);
            }
        } else {
            log.info("Graceful Shutdown 완료 - 저장할 데이터 없음");
        }
    }






}
