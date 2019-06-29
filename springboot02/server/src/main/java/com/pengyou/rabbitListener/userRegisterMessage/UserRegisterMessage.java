package com.pengyou.rabbitListener.userRegisterMessage;


import com.pengyou.model.entity.User;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * 用于rabbitmq模型
 */
public class UserRegisterMessage implements Serializable {

    private User user;

    private String url;

    public UserRegisterMessage() {
    }

    public UserRegisterMessage(User user, String url) {
        this.user = user;
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserRegisterMessage{" +
                "user=" + user +
                ", url='" + url + '\'' +
                '}';
    }
}