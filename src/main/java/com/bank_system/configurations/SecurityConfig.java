package com.bank_system.configurations;

import com.bank_system.services.MyUserDetailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private MyUserDetailServices userDetailServices;
    @Autowired
    public SecurityConfig(MyUserDetailServices userDetailServices) {
        this.userDetailServices = userDetailServices;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailServices);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/login","/register","/home","/")
                        .permitAll().requestMatchers("/admin/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers("/deposit/**").hasAnyAuthority("AGENT","ADMIN")
                        .anyRequest()
                        .authenticated());
        http.formLogin(login -> login.loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard",true)
                .permitAll());
        http.sessionManagement(session->session
                .invalidSessionUrl("/login?timeout")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired"));
        http.logout(logout-> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll());
        http.requiresChannel(channel->channel.anyRequest().requiresSecure());
        http.headers(header->header.cacheControl(cache->cache.disable()));
        return http.build();
    }
}
