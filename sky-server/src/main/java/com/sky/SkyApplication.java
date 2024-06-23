package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开启注解方式的缓存功能
@EnableScheduling //开启任务调度
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext factory = SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
