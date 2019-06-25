package com.pengyou.controller;


import com.pengyou.enums.StatusCode;
import com.pengyou.exception.GlobalSystemException;
import com.pengyou.exception.NotFoundException;
import com.pengyou.model.entity.OrderRecord;
import com.pengyou.model.mapper.OrderRecordMapper;
import com.pengyou.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/9/23.
 */
@RestController
public class TestController {

    private static final Logger log= LoggerFactory.getLogger(TestController.class);

    private static final String prefix="test";

    @Autowired(required = false)
    private OrderRecordMapper orderRecordMapper;



    @RequestMapping(value = prefix+"/exception/advice/{id}",method = RequestMethod.GET)
    public BaseResponse exceptionAdvice(@PathVariable Integer id) throws GlobalSystemException {
        OrderRecord record=orderRecordMapper.selectByPrimaryKey(id);
        if (id==null || id<=0 || record==null){
            throw new GlobalSystemException("请求实体信息不存在");
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        response.setData(record);
        return response;
    }

    @RequestMapping(value = prefix+"/exception/advice/not/found/{id}",method = RequestMethod.GET)
    public BaseResponse exceptionNotFound(@PathVariable Integer id) throws NotFoundException{
        OrderRecord record=orderRecordMapper.selectByPrimaryKey(id);
        if (id==null || id<=0 || record==null){
            throw new NotFoundException("请求实体信息不存在V2");
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        response.setData(record);
        return response;
    }
}






































