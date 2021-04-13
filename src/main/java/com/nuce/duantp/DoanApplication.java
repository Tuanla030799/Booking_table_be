package com.nuce.duantp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableJpaRepositories
@EnableTransactionManagement
//@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.phamtan.base","com.nuce.duantp"})
public class DoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoanApplication.class, args);

    }


}
