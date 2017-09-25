package com.euihwan.study.accounts;

import com.euihwan.study.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping("/")
    public String hello() {
        return "Hello Spring Boot";
    }

    @RequestMapping(value = "/accounts", method = POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create,
                                        BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            //TODO BindingResult 안에 들어있는 에러 정보 사용하기.
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        Account newAccount = service.createAccount(create); //잘됬는지 안됬는지는, 해당 서비스 클래스 내에서 exception으로 해결하면 깔끔.

        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getUsername() + "] 중복 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");
        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getId() + "] 에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");
        return errorResponse;
    }
    // /accounts?age=0&size=20&sort=username&sort=joined,desc
    @RequestMapping(value = "/accounts", method = GET)
    public ResponseEntity getAccounts(Pageable pageable) {
        Page<Account> page = repository.findAll(pageable);
        //TODO stream() vs parallelStream()
        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());

        PageImpl<AccountDto.Response> result = new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{id}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public AccountDto.Response getAccount(@PathVariable Long id){
        Account account = service.getAccount(id);
        return modelMapper.map(account, AccountDto.Response.class);
    }

    //전체 업데이트(업데이트 대상들이 파라미터로 온다고 가정. PUT) vs 부분 업데이트(일부 필드만 업데이트. PATCH)
    @RequestMapping(value = "/accounts/{id}", method = PUT)
    public ResponseEntity updateAccount(@PathVariable Long id,
                                        @RequestBody @Valid AccountDto.Update updateDto,
                                        BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account updateAccount = service.updateAccount(id, updateDto);
        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class)
                , HttpStatus.OK);

        //TODO UPDATE
    }



}