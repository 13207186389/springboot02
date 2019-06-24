package com.pengyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "system.entity.variable")//指定配置文件的前缀
public class ConfigEntity implements Serializable {

    private String userName;
    private String password;
    private String signSystem;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignSystem() {
        return signSystem;
    }

    public void setSignSystem(String signSystem) {
        this.signSystem = signSystem;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", signSystem='" + signSystem + '\'' +
                '}';
    }
}
