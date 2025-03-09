package com.pro.service.persist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.exception.BankRuntimeException;
import com.pro.model.param.*;
import com.pro.repository.AccountRepository;
import com.pro.repository.BankAccountRepository;
import com.pro.repository.UserRepository;
import com.pro.model.entity.AccountEntity;
import com.pro.model.entity.BankAccountEntity;
import com.pro.model.entity.UserEntity;
import com.pro.model.result.RestResult;
import com.pro.util.ServiceUtil;
import com.pro.util.ValidationChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.pro.util.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountRepository.getAll());

        return new RestResult("전체 조회 성공", "success", data);
    }

    @Transactional
    public void userSave(UserEntity userEntity){
        userRepository.save(userEntity);

        log.info("회원가입 성공 : {}", toJson(userEntity));
    }


    @Transactional
    public void deposit(BankAccountDepositParam param, AccountEntity savedAccount) {
        savedAccount.setBalance(savedAccount.getBalance() + param.getBalance());
        accountRepository.updateAccountBalance(savedAccount);

        log.info("입금 성공 : {}", toJson(savedAccount));
    }

    @Transactional
    public void withdraw(BankAccountWithdrawParam param, AccountEntity savedAccount) {
       savedAccount.setBalance(savedAccount.getBalance() - param.getBalance());
       accountRepository.updateAccountBalance(savedAccount);

       log.info("출금 성공 : {}", toJson(savedAccount));

    }


    @Transactional
    public void transfer(BankAccountTransferParam param, AccountEntity fromSavedAccount, AccountEntity toSavedAccount) {
        fromSavedAccount.setBalance(fromSavedAccount.getBalance() - param.getBalance());
        toSavedAccount.setBalance(toSavedAccount.getBalance() - param.getBalance());

        accountRepository.updateAccountBalance(fromSavedAccount);
        accountRepository.updateAccountBalance(toSavedAccount);

        log.info("이체 성공 : {},{}", toJson(fromSavedAccount),toJson(toSavedAccount));

    }


    public AccountEntity getBankAccount(Long accountNumber) {
        final AccountEntity accountEntity = accountRepository.findById(accountNumber)
                .orElseThrow(()-> new BankRuntimeException("해당 계좌를 찾을 수 없습니다."));

        return accountEntity;
    }


}
