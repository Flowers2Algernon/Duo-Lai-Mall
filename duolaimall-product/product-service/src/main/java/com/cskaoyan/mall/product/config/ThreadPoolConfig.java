package com.cskaoyan.mall.product.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        //线程池参数的含义
        //1. 核心线程数
        //2. 拥有的最多线程数
        //3. 存活时间单位
        //4. 用于缓存任务的阻塞队列
        //省略： threadFactory: 用于指定创建线程的工厂
        // 省略： handler: 表示当workQueue 已满，且池中线程数达到maximumPoolsize时，线程池拒绝添加新任务时采取的策略
        return new ThreadPoolExecutor(50,500,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10000));
    }
}
