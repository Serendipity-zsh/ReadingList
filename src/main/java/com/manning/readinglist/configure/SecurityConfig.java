package com.manning.readinglist.configure;

import com.manning.readinglist.dao.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReaderRepository readerRepository;

    /**
     * 要求登陆者有READER角色
     * 设置登陆表单路径
     * 定义自定义UserDetailsService
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                   //要求登陆者有READER角色
                  .antMatchers("/").access("hasRole('READER')")
                  .antMatchers("/**").permitAll()
                .and()

                .formLogin()
                   //设置登陆表单路径
                  .loginPage("/login")
                  .failureUrl("/login?error=true");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
          .userDetailsService(new UserDetailsService(){
              @Override
              public UserDetails loadUserByUsername(String username)
                            throws UsernameNotFoundException {
                  return readerRepository.findOne(username);
                    }
                });
    }
}
