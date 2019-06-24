package com.pengyou.controller;


import com.google.common.base.Strings;
import com.pengyou.enums.StatusCode;
import com.pengyou.request.UserRequest;
import com.pengyou.response.BaseResponse;



import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ValidatorController {

    private static final String prefix="validate";

    /**
     *新增
     * @return
     */
    @RequestMapping(value = prefix+"/insert",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse detail(@RequestBody @Validated UserRequest userRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //如果有错误
            if(result.hasErrors()){
                response=new BaseResponse(StatusCode.Validate_params);
            }

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = prefix+"/insertv2",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse detailv2(@RequestBody @Validated UserRequest userRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //如果有错误
            if(result.hasErrors()){
                response=new BaseResponse(StatusCode.Validate_params);
            }

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}
