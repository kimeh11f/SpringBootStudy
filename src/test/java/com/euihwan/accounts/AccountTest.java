package com.euihwan.accounts;

import com.euihwan.study.accounts.Account;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountTest {
    @Test
    public void getterSetter(){
        Account account = new Account();
        account.setUsername("whiteship");
        account.setPassword("password");
        assertThat(account.getUsername(), is("whiteship"));
    }
}
