package com.pengyou;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
@MapperScan(basePackages = "com.pengyou.model.mapper")//扫描mapper包,创建代理对象
@ImportResource(locations = "classpath:spring/spring-jdbc.xml")//读取数据源配置文件
public class ServerApplication{




    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
