package com.euihwan.study.security;

import com.euihwan.study.accounts.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl extends User {
    public UserDetailsImpl(Account account){
        super(account.getUsername(), account.getPassword(), authorities(account));
    }

    private static Collection<? extends GrantedAuthority> authorities(Account account){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(account.isAdmin()){
            authorities.add(new SimpleGrantedAuthority(("ROLE_ADMIN")));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(("ROLE_USER")));
        }
        return authorities;
    }
}
