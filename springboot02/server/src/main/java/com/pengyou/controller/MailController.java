package com.pengyou.controller;


import com.google.common.collect.Maps;

import com.pengyou.enums.StatusCode;
import com.pengyou.request.MailRequest;
import com.pengyou.response.BaseResponse;
import com.pengyou.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Administrator on 2018/9/22.
 */
@RestController
public class MailController {

    private static final Logger log= LoggerFactory.getLogger(ProductController.class);

    private static final String prefix="mail";

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;


    /**
     * 发送简单文本邮件
     * @param mailRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/send/simple",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse sendSimpleMail(@RequestBody @Validated MailRequest mailRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            log.info("发送简单文本邮件：{} ",mailRequest);


            mailService.sendsimpleMail(mailRequest.getSubject(),mailRequest.getContent(), StringUtils.split(mailRequest.getMailTos(),","));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 发送带附件邮件
     * @param mailRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/send/attachment",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse sendAttachmentMail(@RequestBody @Validated MailRequest mailRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            log.info("发送带附件的邮件：{} ",mailRequest);


            mailService.sendAttachmentMail(mailRequest.getSubject(),mailRequest.getContent(), StringUtils.split(mailRequest.getMailTos(),","));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送html带附件邮件
     * @param mailRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/send/html",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse sendHTMLMail(@RequestBody @Validated MailRequest mailRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            log.info("发送HTML文本邮件：{} ",mailRequest);


            mailService.sendhtmlMail(mailRequest.getSubject(),mailRequest.getContent(), StringUtils.split(mailRequest.getMailTos(),","));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /**
     * thymeleaf模板
     * 发送渲染的html带附件邮件
     * @param mailRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/send/thymeleaf/template",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse sendTemplateThymeleafMail(@RequestBody @Validated MailRequest mailRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            log.info("发送渲染HTML模板的邮件：{} ",mailRequest);

            //创建一个map传入渲染参数
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("content",mailRequest.getContent());
            paramMap.put("mailTos",mailRequest.getMailTos());

            //添加一个渲染方法获得渲染后的html页面
            String html=mailService.renderThymeleafTemplate(env.getProperty("mail.template.file.thymeleaf.location"),paramMap);
            mailService.sendhtmlMail(mailRequest.getSubject(),html,StringUtils.split(mailRequest.getMailTos(),","));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /**
     * freemarker模板
     * 发送渲染的html带附件邮件
     * @param mailRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/send/freemarker/template",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse sendTemplateFreemarkerMail(@RequestBody @Validated MailRequest mailRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            log.info("发送渲染HTML模板的邮件：{} ",mailRequest);

            //创建一个map传入渲染参数
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("content",mailRequest.getContent());
            paramMap.put("mailTos",mailRequest.getMailTos());

            //添加一个渲染方法获得渲染后的html页面
            String html=mailService.renderFreemarkerTemplate(env.getProperty("mail.template.file.freemarker.location"),paramMap);
            mailService.sendhtmlMail(mailRequest.getSubject(),html,StringUtils.split(mailRequest.getMailTos(),","));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }




    /**
     * 发送HTML文本邮件
     * @param mailRequest
     * @param result
     * @return
     */
//    @RequestMapping(value = prefix+"/send/html",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public BaseResponse sendHTMLMail(@RequestBody @Validated MailRequest mailRequest,BindingResult result){
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        try {
//            if (result.hasErrors()){
//                return new BaseResponse(StatusCode.Invalid_Params);
//            }
//            log.info("发送HTML文本邮件：{} ",mailRequest);
//
//
//            mailService.sendHTMLMail(mailRequest.getSubject(),mailRequest.getContent(), StringUtils.split(mailRequest.getMailTos(),","));
//        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//            e.printStackTrace();
//        }
//        return response;
//    }


    /**
     * 渲染HTML模板并发送带模板的邮件
     * @param mailRequest
     * @param result
     * @return
     */
//    @RequestMapping(value = prefix+"/send/template",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public BaseResponse sendTemplateMail(@RequestBody @Validated MailRequest mailRequest,BindingResult result){
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        try {
//            if (result.hasErrors()){
//                return new BaseResponse(StatusCode.Invalid_Params);
//            }
//            log.info("渲染HTML模板并发送带模板的邮件：{} ",mailRequest);
//
//            Map<String,Object> paramMap= Maps.newHashMap();
//            paramMap.put("content",mailRequest.getContent());
//            paramMap.put("mailTos",mailRequest.getMailTos());
//            String html=mailService.renderTemplate(env.getProperty("mail.template.file.location"),paramMap);
//
//            mailService.sendHTMLMail(mailRequest.getSubject(),html, StringUtils.split(mailRequest.getMailTos(),","));
//        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//            e.printStackTrace();
//        }
//        return response;
//    }
}







































