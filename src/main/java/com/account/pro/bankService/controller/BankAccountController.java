package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.req.ConReqDepositAccountDto;
import com.account.pro.bankService.controller.req.BankAccountParam;
import com.account.pro.bankService.controller.req.ConReqTransformAccountDto;
import com.account.pro.bankService.controller.req.ConReqWithdrawAccountDto;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.service.BankAccountService;
import com.account.pro.bankService.service.req.SerReqDepositeAccountDto;
import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import com.account.pro.bankService.service.req.SerReqTransformAccountDto;
import com.account.pro.bankService.service.req.SerReqWithdrawAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * todo : json 기반으로 return 값을 변경해주세요.
 * todo : api에 version 을 추가해주세요.
 * todo : 전체적으로 log 를 추가해주세요.
 * todo : application/json 으로 리턴되도록. 어떤 경우라도... 404, 405, 500 등등
 *  - 404, 405 이런거는 별도의 설정이 필요합니다.
 * todo : GlobalExceptionHandler 를 추가해서 모든 예외를 잡아주세요.
 *  - RestError 로 리턴을 해주세요. 400 에러
 *   {id: "server_error", message: "에러메시지"}
 *  - RestResult    200 OK
 *   {data: {"status", true, "status": "200", "message": "성공", ....}}
 */
@Slf4j
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final BankFrontService bankFrontService;

    //resDto 변경 예정
    @GetMapping("/getAll")  //todo : case 는 일관성을 앞으로 지켜주세요.
    public Collection<BankAccount> getAll(){
        return bankAccountService.getAll();
    }

    @PostMapping("/save")   // todo: DTO 를 정해서 리턴. RestResult<Object>
    public ResponseEntity<String> save(@RequestBody BankAccountParam param){

        log.info("/bank/save : {}", toJson(param));

        bankFrontService.save(param);

        // todo: 인터페이스는 꼭 필요할 때 씁시다.
        bankAccountService.save(serReqSaveBankAccountDto);

        // todo: retrun DTO 를 지정해서. json 으로.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("save success");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody ConReqDepositAccountDto dto){
        SerReqDepositeAccountDto serReqDepositeAccountDto = dto.toSerReqDepositeAccountDto();
        bankAccountService.deposit(serReqDepositeAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("deposit success");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody ConReqWithdrawAccountDto dto){
        SerReqWithdrawAccountDto serReqWithdrawAccountDto = dto.toSerReqWithdrawAccountDto();
        bankAccountService.withdraw(serReqWithdrawAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("withdraw success");
    }

    // todo: transfer 로 변경
    @PostMapping("/transform")
    public ResponseEntity<String> transform(@RequestBody ConReqTransformAccountDto dto){
        SerReqTransformAccountDto serReqTransformAccountDto = dto.toSerReqTransformAccountDto();
        bankAccountService.transform(serReqTransformAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("transform success");
    }


}
