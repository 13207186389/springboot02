package com.pengyou.listener;


import com.google.common.collect.Maps;
import com.pengyou.listener.event.UserRegisterEvent;
import com.pengyou.model.entity.User;
import com.pengyou.service.MailService;
import com.pengyou.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2018/9/28.
 */
@Component
public class UserRegisterListener implements ApplicationListener<UserRegisterEvent>{

    private static final Logger log= LoggerFactory.getLogger(UserRegisterListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    /**
     * 监听实际处理逻辑
     * @param event
     */
    @Async
    public void onApplicationEvent(UserRegisterEvent event) {
        try {
            User user=event.getUser();
            if (user!=null){
                //TODO：更新缓存
                userService.updateRedisCache(user.getId());
                //发送邮件给用户
                Map<String,Object> paramsMap= Maps.newHashMap();
                paramsMap.put("userName",user.getUserName());
                paramsMap.put("url",event.getUrl());
                //得到要发送邮件内容的模板
                String html=mailService.renderFreemarkerTemplate(env.getProperty("mail.template.file.location.register"),paramsMap);
                //发送邮件
                mailService.sendhtmlMail("成功入职通知",html,new String[]{user.getEmail()});
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}






























