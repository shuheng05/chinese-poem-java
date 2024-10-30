package com.sweet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shuheng
 */
@SpringBootApplication
@EnableScheduling // 开启定时任务
public class ChinesePoemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChinesePoemApplication.class, args);
    }

}
