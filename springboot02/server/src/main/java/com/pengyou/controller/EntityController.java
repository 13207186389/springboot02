package com.pengyou.controller;

import com.pengyou.config.ConfigEntity;
import com.pengyou.config.ConfigEntityLombok;
import com.pengyou.enums.StatusCode;
import com.pengyou.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityController {

    @Autowired
    private ConfigEntity configEntity;

    @Autowired
    private ConfigEntityLombok configEntityLombok;

    @GetMapping(value = "/info/{id}")
    public BaseResponse detail(@PathVariable Integer id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            System.out.println(configEntity.toString());
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @GetMapping(value = "/infoLombok/{id}")
    public BaseResponse detailLombok(@PathVariable Integer id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            System.out.println(configEntityLombok.toString());
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}
