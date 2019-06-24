package com.pengyou.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/9/23.
 */
@Data
@ToString
public class AppendixRequest {

    @NotNull
    private Integer recordId;

    //TODO：附件记录id-以逗号隔开
    @NotBlank
    private String appendixIds;

}































