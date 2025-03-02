package com.pro.bankService.service;

import com.pro.bankService.controller.request.BankAccountDepositParam;
import com.pro.bankService.controller.request.BankAccountSaveParam;
import com.pro.bankService.controller.request.BankAccountTransferParam;
import com.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.bankService.repository.mybatis.AccountRepository;
import com.pro.bankService.repository.mybatis.BankAccountRepository;
import com.pro.bankService.repository.mybatis.UserRepository;
import com.pro.bankService.repository.mybatis.entity.AccountEntity;
import com.pro.bankService.repository.mybatis.entity.BankAccountEntity;
import com.pro.bankService.repository.mybatis.entity.UserEntity;
import com.pro.response.RestResult;
import com.pro.bankService.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

// todo 실제로 Controller 통해서 테스트가 될수 있도록 연결해서 한번 해보세요.
// todo 아래 각 메소드의 로그상에서 balance 가 변하는 것을 확인할 수 있도록 로그를 추가해주세요. 로그만 보더라도 어떤 유저의 밸런스가 어떻게 변해가는지를 확인할 수 있으면 좋겠습니다.
//  예) success deposit balance: 1000 -> 2000
//  예) success withdrawal balance: 2000 -> 1000
//  기타 다른곳에 필요해 보이는 곳에도 추가로 해보면 좋겠습니다.
// todo 밸런스 히스토리를 남길수 있도록 BalanceLog 테이블을 만들어 주세요. 즉 거래내역을 남기는 일입니다.
@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountRepository.getAll());

        return new RestResult("조회 성공", "true", data);
    }

    @Transactional
    public RestResult save(BankAccountSaveParam param) {
        if(param.getBalance() == null || param.getEmail() == null || param.getPassword() == null || param.getName() == null){
            return new RestResult("회원가입 오류","false");
        }

        String saveBankAccountId = ServiceUtil.createBankAccountId();

        userRepository.save(new UserEntity(
              param.getUser_id(),
              param.getName(),
              param.getEmail(),
              bCryptPasswordEncoder.encode(param.getPassword()),
              param.getRole()
        ));

        AccountEntity accountEntity = new AccountEntity(param.getBalance());
        accountRepository.save(accountEntity);

        bankAccountRepository.save(new BankAccountEntity(
                    saveBankAccountId,
                    accountEntity.getAccount_number(),
                    param.getUser_id()));

        return new RestResult("가입 성공", "true");
    }

    @Transactional
    public RestResult deposit(BankAccountDepositParam param) {

    /*1.요청 값 검증*/
        if(param.getBalance() == null || param.getAccount_number() == null){
            return new RestResult("입금 실패 : 계좌 번호가 없습니다.", "false");
        }
        BalanceLogEntity balanceLog = new BalanceLogEntity();

    /*2.기존 계좌 정보 조회*/
        AccountEntity prevAccount = accountRepository.findById(new AccountEntity(
                param.getAccount_number(),param.getBalance()
        ));
        if (prevAccount == null) {
            throw new IllegalStateException("입금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }


    /*3.현재 실행 메서드 저장*/
        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLog.setClassMethod(Map.of("currentMethod",fullMethodName));

    /*4.이전 계좌 데이터 저장 */
        balanceLog.setPrevData(new LinkedHashMap<>(Map.of(
                "account_number",prevAccount.getAccount_number(),
                "balance", prevAccount.getBalance()
        )));
    /*5.잔액 업데이트*/
        prevAccount.setBalance(prevAccount.getBalance() + param.getBalance());
        accountRepository.updateAccountBalance(prevAccount);

    /*6.업데이트된 계좌 정보 조회 */
        AccountEntity updatedAccount = accountRepository.findById(new AccountEntity(param.getAccount_number()));
        if(updatedAccount == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

    /*7.업데이트된 계좌 데이터 저장*/
        balanceLog.setCurrentData(new LinkedHashMap<>(Map.of(
                "account_number", updatedAccount.getAccount_number(),
                "balance", updatedAccount.getBalance()
        )));

        log.info("      BalanceLog : {}", Objects.toString(balanceLog, "null"));

        return new RestResult("입금 성공", "true");
    }

    @Transactional
    public RestResult withdraw(BankAccountWithdrawParam param) {

    /*1.요청 값 검증*/
        if(param.getBalance() == null || param.getAccount_number() == null){
            return new RestResult("출금 실패 : 계좌 번호가 없습니다.", "false");
        }
        BalanceLogEntity balanceLog = new BalanceLogEntity();

    /*2.기존 계좌 정보 조회*/
        AccountEntity prevAccount = accountRepository.findById(new AccountEntity(
                param.getAccount_number(),param.getBalance()
        ));
        if (prevAccount == null) {
            throw new IllegalStateException("출금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }

        if (prevAccount.getBalance() < param.getBalance()) {
            return new RestResult("출금 실패: 잔액 부족", "false");
        }

    /*3.현재 실행 메서드 저장*/
        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLog.setClassMethod(Map.of("currentMethod",fullMethodName));

    /*4.이전 계좌 데이터 저장 */
        balanceLog.setPrevData(new LinkedHashMap<>(Map.of(
                "account_number",prevAccount.getAccount_number(),
                "balance", prevAccount.getBalance()
        )));

    /*5.잔액 업데이트*/
        prevAccount.setBalance(prevAccount.getBalance() - param.getBalance());
        accountRepository.updateAccountBalance(prevAccount);

    /*6.업데이트된 계좌 정보 조회 */
        AccountEntity updatedAccount = accountRepository.findById(new AccountEntity(param.getAccount_number()));
        if(updatedAccount == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

    /*7.업데이트된 계좌 데이터 저장*/
        balanceLog.setCurrentData(new LinkedHashMap<>(Map.of(
                "account_number", updatedAccount.getAccount_number(),
                "balance", updatedAccount.getBalance()
        )));

        log.info("      BalanceLog : {}", Objects.toString(balanceLog, "null"));

        return new RestResult("출금 성공", "true");

    }

    @Transactional
    public RestResult transfer(BankAccountTransferParam param) {

    /*1.요청 값 검증*/
        if (param.getBalance() == null || param.getFromAccountNumber() == null || param.getToAccountNumber() == null) {
            return new RestResult("이체 실패 : 계좌 번호가 없습니다.", "false");
        }
        if (param.getBalance() <= 0) {
            return new RestResult("이체 실패 : 이체 금액을 확인해주세요.", "false");
        }

        BalanceLogEntity balanceLog = new BalanceLogEntity();

    /*2. 기존 계좌 조회 */
        AccountEntity fromAccountResult = accountRepository.findById(
                new AccountEntity(
                        param.getFromAccountNumber(),
                        param.getBalance()));

        AccountEntity toAccountResult = accountRepository.findById(
                new AccountEntity(
                        param.getToAccountNumber(),
                        param.getBalance()));

        if (fromAccountResult == null || toAccountResult == null) {
            return new RestResult("이체 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        if (toAccountResult.getBalance() < param.getBalance()) {
            return new RestResult("이체 실패: 잔액 부족", "false");
        }

    /*3.현재 실행 메서드 저장*/
        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLog.setClassMethod(Map.of("currentMethod",fullMethodName));


    /*4.이전 계좌 데이터 저장 */
        balanceLog.setPrevData(new LinkedHashMap<>(Map.of(
                "from_account_number",fromAccountResult.getAccount_number(),
                "from_balance",fromAccountResult.getBalance(),
                "to_account_number",toAccountResult.getAccount_number(),
                "to_balance", toAccountResult.getBalance()
        )));

    /*5. 잔액 업데이트 */
        fromAccountResult.setBalance(fromAccountResult.getBalance() - param.getBalance());
        toAccountResult.setBalance(toAccountResult.getBalance() + param.getBalance());

        accountRepository.updateAccountBalance(fromAccountResult);
        accountRepository.updateAccountBalance(toAccountResult);

    /*6.업데이트된 계좌 정보 조회 */
        AccountEntity uptFromAccountResult = accountRepository.findById(fromAccountResult);
        AccountEntity uptToAccountResult = accountRepository.findById(toAccountResult);

        if(uptFromAccountResult == null || uptToAccountResult == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

    /*7.업데이트된 계좌 데이터 저장*/
        balanceLog.setCurrentData(new LinkedHashMap<>(Map.of(
                "from_account_number",uptFromAccountResult.getAccount_number(),
                "from_balance",uptFromAccountResult.getBalance(),
                "to_account_number",uptToAccountResult.getAccount_number(),
                "to_balance", uptToAccountResult.getBalance()
        )));


        log.info("      BalanceLog : {}", Objects.toString(balanceLog, "null"));
        return new RestResult("이체 성공", "true");
    }
}
