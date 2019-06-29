package com.pengyou.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString
public class UserOrderDto implements Serializable{

    @NotBlank
    private String orderNo;

    @NotNull
    private Integer userId;

}