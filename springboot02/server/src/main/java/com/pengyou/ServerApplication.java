package com.pengyou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.pengyou.model.mapper")//扫描mapper包,创建代理对象
@ImportResource(locations = "classpath:spring/spring-jdbc.xml")//读取数据源配置文件
@EnableScheduling//允许任务调度(开启定时任务)
@EnableAsync//开始异步处理注册,更新缓存,发送邮件
public class ServerApplication{




    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
