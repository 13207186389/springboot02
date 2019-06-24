package com.pengyou.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/22.
 */
@Data
@ToString
public class MailRequest implements Serializable{

    @NotBlank
    private String mailTos;

    private String subject;

    private String content;

}