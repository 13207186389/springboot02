package com.pengyou.controller;


import com.google.common.collect.Maps;
import com.pengyou.enums.StatusCode;
import com.pengyou.exception.GlobalSystemException;
import com.pengyou.exception.NotFoundException;
import com.pengyou.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 全局异常处理机制
 * 类似于spring的aop
 * Created by Administrator on 2018/9/23.
 */
@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionController.class);

    //定义处理的异常类型,并把处理后的异常封装到对象返回JSON串给页面
    @ExceptionHandler(value = GlobalSystemException.class)
    @ResponseBody
    public BaseResponse systemException(Exception e, HttpServletRequest request){
        BaseResponse response=new BaseResponse(StatusCode.Fail);
        Map<String,Object> resMap= Maps.newHashMap();
        resMap.put("uri",request.getRequestURI());
        resMap.put("exp",e.getMessage());

        response.setData(resMap);
        return response;
    }

    //定义这个异常返回404页面
    @ExceptionHandler(value = NotFoundException.class)
    public String notFoundPage(Exception e, HttpServletRequest request){
        log.info("异常信息：{} ",e.getMessage());
        request.setAttribute("errorInfo",e.getMessage());
        return "notFound";
    }


}
































