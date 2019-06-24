package com.pengyou.controller;

import com.pengyou.enums.StatusCode;
import com.pengyou.model.entity.OrderRecord;
import com.pengyou.request.OrderRecordInsertUpdateDto;
import com.pengyou.response.BaseResponse;
import com.pengyou.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderRecordController {

    @Autowired(required = false)
    private OrderRecordService orderRecordService;

    @GetMapping(value = "/detail/{id}")
    public BaseResponse detail(@PathVariable Integer id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            OrderRecord orderRecord = orderRecordService.selectPrimaryKey(id);
            response.setData(orderRecord);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @GetMapping(value = "/selectAll")
    public BaseResponse selectAll(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            List<OrderRecord> Orderlist = orderRecordService.selectAll();
            response.setData(Orderlist);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/insert",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse insert(@RequestBody OrderRecordInsertUpdateDto dto){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            orderRecordService.insert(dto);
        }catch (Exception e){
            e.printStackTrace();
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody OrderRecordInsertUpdateDto dto){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //进行更新操作前先查询
            OrderRecord orderRecord = orderRecordService.selectPrimaryKey(dto.getId());

            if(orderRecord==null){
                return new BaseResponse(StatusCode.NotFound);
            }
            orderRecordService.update(orderRecord,dto);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @GetMapping(value = "/delete/{id}")
    public BaseResponse delete(@PathVariable Integer id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //进行删除操作前先查询
            OrderRecord orderRecord = orderRecordService.selectPrimaryKey(id);

            if(orderRecord==null){
                return new BaseResponse(StatusCode.NotFound);
            }
            orderRecordService.deleteByPrimaryKey(id);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }

}
