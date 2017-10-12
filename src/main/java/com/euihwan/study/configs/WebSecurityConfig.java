package com.euihwan.study.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }//userDetailsService와 passwordEncoder를 AuthenticationManagerBuilder에 세팅.

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        /*csrf는 기본으로 켜있는데, 켜있으면, POST, PUT 같은 GET 이외 요청에서 CSRF토큰값이 같이 들어온다.
        그 값을 SECURITY가 검증하는데, 안들어오거나 잘못된 값이 들어오면 SPRING SECURITY가 처리해주지 않는다.
        현재는 csrf값을 안보내주고 있기 때문에, 꺼둠.*/
        http.httpBasic();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/accounts/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/accounts/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("USER")
                .anyRequest().permitAll();
    }

    //ROLE Hieracy


}
