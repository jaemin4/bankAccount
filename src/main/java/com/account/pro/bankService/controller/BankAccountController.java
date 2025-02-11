package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.req.BankAccountDepositParam;
import com.account.pro.bankService.controller.req.BankAccountSaveParam;
import com.account.pro.bankService.controller.req.BankAccountTransferParam;
import com.account.pro.bankService.controller.req.BankAccountWithdrawParam;
import com.account.pro.bankService.controller.res.RestResult;
import com.account.pro.bankService.service.BankFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;



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
@Profile("bank-api")
@Slf4j
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankFrontService bankFrontService;

    // log.info("/bank/save : {}", toJson(param));
    // todo: 인터페이스는 꼭 필요할 때 씁시다.
    // todo: retrun DTO 를 지정해서. json 으로.
    @GetMapping("/getAll")  //todo : case 는 일관성을 앞으로 지켜주세요.
    public RestResult getAll(){
        return bankFrontService.getAll();
    }

    @PostMapping("/save")   // todo: DTO 를 정해서 리턴. RestResult<Object>
    public RestResult save(@RequestBody BankAccountSaveParam param){
        log.info("/bank/save : {}", param);

        return bankFrontService.save(param);
    }

    @PostMapping("/deposit")
    public RestResult deposit(@RequestBody BankAccountDepositParam param){
        log.info("/bank/save : {}", param);

        return bankFrontService.deposit(param);
    }

    @PostMapping("/withdraw")
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param){
        log.info("/bank/withdraw : {}", param);

        return bankFrontService.withdraw(param);

    }

    // todo: transfer 로 변경
    @PostMapping("/transform")
    public RestResult transfer(@RequestBody BankAccountTransferParam param){
        log.info("/bank/transfer : {}", param);

        return bankFrontService.transfer(param);

    }


}
