package com.euihwan.study.accounts;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        //TODO JSON Path
        //result.andExpect(jsonPath("$.username", is("whiteship")));
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
        //TODO JSON Path
        //result.andExpect(jsonPath("$.username", is("whiteship")));
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
}