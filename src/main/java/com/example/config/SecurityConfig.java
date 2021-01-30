package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception{
    http
      .authorizeRequests()
        .anyRequest().permitAll()
        .and()
      .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/")
        .failureUrl("/login?error=1")
        .permitAll()
        .and()
      .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .permitAll()
        .and()
      .rememberMe()
        .key("uniqueKeyAndSecret")
        .rememberMeParameter("remember-me")
        .rememberMeCookieName("remember-me")
        .tokenValiditySeconds(86400)
        .and()
      .sessionManagement()
        .maximumSessions(1);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }
}