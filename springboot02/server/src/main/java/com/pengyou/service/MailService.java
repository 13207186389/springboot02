package com.pengyou.service;


import com.pengyou.model.entity.Appendix;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    private static final Logger log= LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private SpringTemplateEngine templateEngine;


    /**
     * 发送简单文本邮件
     * @param subject
     * @param content
     * @param tos
     */
    public void sendsimpleMail(String subject,String content,String[] tos ){
        //获取发送简单邮件的对象
        SimpleMailMessage message =new SimpleMailMessage();

        //设置邮件发送者
        message.setFrom(env.getProperty("mail.send.from"));
        //设置邮件接收者(可以群发)
        message.setTo(tos);
        //设置发送主题
        message.setSubject(subject);
        //设置发送内容
        message.setText(content);

        //利用发送邮件的工具发送邮件
        mailSender.send(message);


    }

    /**
     * 发送带附件的邮件
     * @param subject
     * @param content
     * @param tos
     */
    public void sendAttachmentMail(String subject, String content, String[] tos) throws Exception {
        //获取发送带附件邮件对象
        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"utf-8");

        //设置邮件发送者
        messageHelper.setFrom(env.getProperty("mail.send.from"));
        //设置邮件接收者(可以群发)
        messageHelper.setTo(tos);
        //设置发送主题
        messageHelper.setSubject(subject);
        //设置发送内容
        messageHelper.setText(content);

        //加入附件
        //注意当编码后的文件名长度如果大于60并且splitLongParameters的值为true - 可以实战测试!
        messageHelper.addAttachment(env.getProperty("mail.send.attachment.one.name"),new File(env.getProperty("mail.send.attachment.one.location")));
        messageHelper.addAttachment(env.getProperty("mail.send.attachment.two.name"),new File(env.getProperty("mail.send.attachment.two.location")));
        messageHelper.addAttachment(env.getProperty("mail.send.attachment.three.name"),new File(env.getProperty("mail.send.attachment.three.location")));

        //利用发送邮件的工具发送邮件
        mailSender.send(message);


    }


    /**
     * 发送HTML的邮件
     * @param subject
     * @param content
     * @param tos
     */
    public void sendhtmlMail(String subject, String content, String[] tos) throws Exception{
        //获取发送带附件邮件对象
        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"utf-8");

        //设置邮件发送者
        messageHelper.setFrom(env.getProperty("mail.send.from"));
        //设置邮件接收者(可以群发)
        messageHelper.setTo(tos);
        //设置发送主题
        messageHelper.setSubject(subject);
        //设置发送内容(这里要设置为ture) content可以写html内容了
        messageHelper.setText(content,true);

        //加入附件
        //注意当编码后的文件名长度如果大于60并且splitLongParameters的值为true - 可以实战测试!
        //messageHelper.addAttachment(env.getProperty("mail.send.attachment.one.name"),new File(env.getProperty("mail.send.attachment.one.location")));


        //利用发送邮件的工具发送邮件
        mailSender.send(message);
    }

    /**
     * thymeleaf模板渲染页面
     * @param templateFile
     * @param paramMap
     * @return
     */
    public String renderThymeleafTemplate(String templateFile, Map<String, Object> paramMap) {
        //创建一个上下文对象
        Context context=new Context(LocaleContextHolder.getLocale());
        //传入要渲染的参数
        context.setVariables(paramMap);
        //调用渲染工具传入渲染上下文和模板名字得到渲染后的html
        String html=templateEngine.process(templateFile,context);

        return html;
    }

    /**
     * freemarker模板渲染页面
     * @param templateFile
     * @param paramMap
     * @return
     */
    public String renderFreemarkerTemplate(String templateFile, Map<String, Object> paramMap) throws Exception {
        //设置freemarker版本
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        //设置去哪里读freemarker文件-目录
        cfg.setClassForTemplateLoading(this.getClass(),"/freemarker");
        //根据你提供模板文件构建模板渲染实例
        Template template = cfg.getTemplate(templateFile);
        //利用freemarker工具去渲染,填入参数,得到渲染后的模板
        String html= FreeMarkerTemplateUtils.processTemplateIntoString(template,paramMap);

        return html;
    }

    /**
     * 改造后的利用定时任务发送邮件的构造方法
     * @param subject 主题
     * @param content 内容
     * @param tos     接收邮件人群
     * @param appendixList 附件列表
     * @throws Exception
     */
    public void sendAttachmentMail(String subject, String content, String[] tos, List<Appendix> appendixList) throws Exception {
        //获取发送带附件邮件对象
        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"utf-8");

        //设置邮件发送者
        messageHelper.setFrom(env.getProperty("mail.send.from"));
        //设置邮件接收者(可以群发)
        messageHelper.setTo(tos);
        //设置发送主题
        messageHelper.setSubject(subject);
        //设置发送内容
        messageHelper.setText(content);

        //遍历附件集合添加附件
        //注意当编码后的文件名长度如果大于60并且splitLongParameters的值为true - 可以实战测试!
        for(Appendix a :appendixList){
            try {
                messageHelper.addAttachment(a.getName(),new File(env.getProperty("file.upload.root.url")+a.getLocation()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //利用发送邮件的工具发送邮件
        mailSender.send(message);
        log.info("定时任务发送带附件文本邮件成功--->");
    }



    //当编码后的文件名长度大于60就要设置splitlongparameters为false
    @PostConstruct
    public void init(){
        System.setProperty("mail.mime.splitlongparameters","false");
    }
}
