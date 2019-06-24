package com.pengyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "system.entity.variable")//指定配置文件的前缀
@Data
public class ConfigEntityLombok implements Serializable {

    private String userName;
    private String password;
    private String signSystem;
    private HostEntity host;

    private List<String> strings=new ArrayList<String>();



}
