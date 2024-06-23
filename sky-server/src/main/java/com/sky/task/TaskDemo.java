package com.sky.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义一个任务类
 *
 * @Author itcast
 * @Create 2024/6/23
 **/
@Component
public class TaskDemo {

    //@Scheduled(cron = "0/5 * * * * ?") //每隔5秒执行一次
    public void myTask(){
        System.out.println("自定义的任务执行了" + LocalDateTime.now());
    }

}
