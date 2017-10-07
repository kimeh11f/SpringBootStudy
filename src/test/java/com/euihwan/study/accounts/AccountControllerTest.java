package com.euihwan.study.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class) //테스트 속도 줄이기의 핵심. @TEST마다 애플리케이션 컨텍스트의 재사용.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest {

    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountService service;

    MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Commit
    public void createAccount1() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("whiteship");
        createDto.setPassword("password");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isCreated());
        //result.andExpect((ResultMatcher) jsonPath("$.username", is("whiteship")));
        System.out.println("!!!!!!!!!!!!!" + objectMapper.writeValueAsString(createDto));

    }

    @Test
    public void createAccount2() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("whiteship");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
        //result.andExpect((ResultMatcher) jsonPath("$.username", is("whiteship")));
        System.out.println("!!!!!!!!!!!!!" + objectMapper.writeValueAsString(createDto));
    }

    @Test
    public void createAccount_BadRequest() throws Exception{
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername(" ");
        createDto.setPassword("1234");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

   /* {"content":[{"id":1,"userName":"whiteship","fullName":null,"joined":1505844335035,"updated":1505844335035}],
   "totalElements":1,"last":true,"totalPages":1,"number":0,"size":20,"sort":null,"first":true,"numberOfElements":1}*/
    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("whiteShip");
        createDto.setUsername("password");
        ResultActions result = mockMvc.perform(get("/accounts"));
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void getAccount() throws Exception {
        AccountDto.Create createDto = accountCreateDto();
        Account account = service.createAccount(createDto);
        ResultActions result = mockMvc.perform(get("/accounts/"+ account.getId()));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create createDto = accountCreateDto();
        Account account = service.createAccount(createDto);

        AccountDto.Update updateDto = new AccountDto.Update();
        updateDto.setFullName("Kim Euihwan");
        updateDto.setPassword("pass");

        ResultActions result = mockMvc.perform(put("/accounts/"+account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    private AccountDto.Create accountCreateDto(){
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("whiteShip");
        createDto.setUsername("password");
        return createDto;
    }

    @Test
    public void zdeleteAccount() throws Exception {
        ResultActions result = mockMvc.perform(delete("/accounts/1"));
        result.andDo(print());
        result.andExpect(status().isNoContent());
    }
}