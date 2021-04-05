package com.nuce.duantp.sunshine.scoped;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class UserScopedBean {
    @Bean(name = "userBean")
    @SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public User userBean(){
        return new User();
    }
}
