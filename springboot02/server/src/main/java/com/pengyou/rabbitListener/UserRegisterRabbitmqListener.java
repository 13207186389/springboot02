package com.pengyou.rabbitListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.pengyou.model.entity.User;
import com.pengyou.rabbitListener.userRegisterMessage.UserRegisterMessage;
import com.pengyou.service.MailService;
import com.pengyou.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRegisterRabbitmqListener {

    private static final Logger log= LoggerFactory.getLogger(UserRegisterRabbitmqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    //指定监听的队列名字,和多消费者还是单消费者  @Payload指定消息流
    @RabbitListener(queues="${rabbitmq.user.register.queue.name}",containerFactory = "singleListenerContainer")
    public void consume(@Payload byte[] msg){
        try {
            //把接收到的字节流转换成对象
            UserRegisterMessage userRegisterMessage=objectMapper.readValue(msg,UserRegisterMessage.class);
            log.info("监听到的消息: {}",userRegisterMessage);
            User user=userRegisterMessage.getUser();
            if (user!=null) {
                //TODO：更新缓存
                userService.updateRedisCache(user.getId());
                //发送邮件给用户
                Map<String, Object> paramsMap = Maps.newHashMap();
                paramsMap.put("userName", user.getUserName());
                paramsMap.put("url", userRegisterMessage.getUrl());
                //得到要发送邮件内容的模板
                String html = mailService.renderFreemarkerTemplate(env.getProperty("mail.template.file.location.register"), paramsMap);
                //发送邮件
                mailService.sendhtmlMail("成功入职通知", html, new String[]{user.getEmail()});
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
