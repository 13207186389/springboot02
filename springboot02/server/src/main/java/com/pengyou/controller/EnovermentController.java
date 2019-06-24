package com.pengyou.controller;

import com.pengyou.enums.StatusCode;
import com.pengyou.model.entity.OrderRecord;
import com.pengyou.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/enovment")
public class EnovermentController {

    //用来读取配置文件中的内容
    @Autowired
    private Environment env;

    @Value("${simple.user.id}")
    private String id2;
    @Value("${simple.user.name}")
    private String name2;
    @Value("${simple.user.age}")
    private String age2;

    @GetMapping(value = "/detail/{id}")
    public BaseResponse detail(@PathVariable Integer id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
              Map<String,Object> map=new HashMap();
              map.put("id1",env.getProperty("simple.user.id"));
              map.put("name1",env.getProperty("simple.user.name"));
              map.put("age1",env.getProperty("simple.user.age"));
              map.put("id2",id2);
              map.put("name2",name2);
              map.put("age2",age2);
            response.setData(map);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
