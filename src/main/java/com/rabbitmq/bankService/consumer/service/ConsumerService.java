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

    // todo: 네. 좋습니다. 분리하는 잘 되었는데요.
    @RabbitListener(queues = "hello.queue")
    public void receiveLogData(AccessLogEntity accessLogEntity){

        // todo: accessLogEntity 를 모아서 한번에 저장하는 방법도 있을 것 같습니다.
        //  이렇게 하면 DB 성능이 좋아질 수 있습니다. 다시한번 생각해보면 accessLogEntity 가 1건씩 저장될 필요는 없습니다.
        //  다양한 방법들이 있겠지만요. 시간베이스로 모아서 주기적으로 한번에 저장하는 방법도 있고요. 갯수 기반으로 모아서 갯수가 지나면 한번에 저장하는 방법도 있습니다. 둘다 활용하는 방법도 있습니다.
        //  이걸 벌크로 테스트할 때는 Local DB 를 활용해주세요. 알려드린 DB는 제가 쓰고 있는 상용 DB라서 부하가 생기면 안되서요 ^^; 부탁드립니다.

        log.info("bankaccount/consumer/consumerService 메세지: {}", accessLogEntity);
        accessLogEntityRepository.save(accessLogEntity);
        log.info("\n bankaccount/consumer/consumberService/receiveLogData : save");

    }




}
