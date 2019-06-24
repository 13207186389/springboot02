package com.pengyou.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.entity.variable.host")//指定配置文件的前缀
@Data
public class HostEntity {
    private String ip;
    private String name;
}
