package com.pengyou.scheduler;


import com.pengyou.model.entity.Appendix;
import com.pengyou.model.entity.OrderRecord;
import com.pengyou.model.mapper.AppendixMapper;
import com.pengyou.model.mapper.OrderRecordMapper;
import com.pengyou.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务发送邮件
 * Created by Administrator on 2018/9/23.
 */
@Component
public class MailOrderRecordScheduler {

    private static final Logger log= LoggerFactory.getLogger(MailOrderRecordScheduler.class);

    //订单id
    private  Integer recordId=3;

    @Autowired(required = false)
    private OrderRecordMapper orderRecordMapper;

    @Autowired(required = false)
    private AppendixMapper appendixMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    //指定执行时间
    //@Scheduled(cron = "${scheduler.mail.send.cron}")
    public void sendOrderRecordAppendixInfo(){
        OrderRecord record=orderRecordMapper.selectByPrimaryKey(recordId);
        if (record!=null){
            final String subject="定时任务实战之@Scheduled-发送带有模块附件的邮件";
            final String content=String.format("订单记录信息：订单编号=%s 订单类型=%s ",record.getOrderNo(),record.getOrderType());

            //根据recordId查询附件集合
            List<Appendix> appendixList=appendixMapper.selectModuleAppendix("orderRecord",recordId);
            if (appendixList!=null && appendixList.size()>0){
                try {
                    mailService.sendAttachmentMail(subject,content, StringUtils.split(env.getProperty("scheduler.mail.send.to"),","),appendixList);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}













































