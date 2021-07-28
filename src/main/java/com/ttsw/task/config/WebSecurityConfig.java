package com.ttsw.task.config;

import com.ttsw.task.Filter.JwtFilter;
import com.ttsw.task.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors();
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/user/create").permitAll()
//                .antMatchers("/user/login").permitAll()
//                .antMatchers("/user/getByEmail").fullyAuthenticated()
//                .antMatchers("/user/getByEmail").fullyAuthenticated()
//                .antMatchers("/user/getByUsername").fullyAuthenticated()
//                .antMatchers("/user/getById").fullyAuthenticated()
//                .antMatchers("/user/update").fullyAuthenticated()
//                .antMatchers("/user/updateAdmin").hasRole("ADMIN")
//                .antMatchers("/user/delete").hasRole("ADMIN")
//                .antMatchers("/user/verifyAccount").permitAll()
//                .antMatchers("user/getAll").fullyAuthenticated()
//                .antMatchers("user/getPageable").permitAll()
//                //.antMatchers("/offer/*").fullyAuthenticated()
//                .and()
//                .httpBasic();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/user/login").permitAll()
                .and().addFilterBefore(new JwtFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/login");
        web.ignoring().antMatchers("/user/create");
        web.ignoring().antMatchers("/user/verifyAccount");
        web.ignoring().antMatchers("/user/resetPassword");
        web.ignoring().antMatchers("/user/getByToken");
        web.ignoring().antMatchers("/user/setNewPassword");
    }
}
