package com.nuce.duantp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
//@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.phamtan.base","com.nuce.duantp"})
public class DoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoanApplication.class, args);

    }

    @RequestMapping("/")
    public String test(){
        return "index";
    }
}
