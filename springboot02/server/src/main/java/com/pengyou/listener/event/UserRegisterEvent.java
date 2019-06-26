package com.pengyou.listener.event;


import com.pengyou.model.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Administrator on 2018/9/28.
 */
public class UserRegisterEvent extends ApplicationEvent{

    private User user;

    private String url;

    public UserRegisterEvent(Object source, User user, String url) {
        super(source);
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
}