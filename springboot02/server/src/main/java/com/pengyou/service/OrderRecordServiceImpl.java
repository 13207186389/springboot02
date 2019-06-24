package com.pengyou.service;

import com.pengyou.model.entity.OrderRecord;
import com.pengyou.model.mapper.OrderRecordMapper;
import com.pengyou.request.OrderRecordInsertUpdateDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderRecordServiceImpl implements OrderRecordService {

    @Autowired(required = false)
    private OrderRecordMapper orderRecordMapper;

    public OrderRecord selectPrimaryKey(Integer id) {

        return orderRecordMapper.selectByPrimaryKey(id);
    }

    public List<OrderRecord> selectAll() {

        return orderRecordMapper.selectAll();
    }

    public void insert(OrderRecordInsertUpdateDto dto) {
        OrderRecord orderRecord=new OrderRecord();
//        orderRecord.setOrderNo(dto.getOrderNo());
//        orderRecord.setOrderType(dto.getOrderType());
        //采用spring提供的工具
        BeanUtils.copyProperties(dto,orderRecord);
        orderRecordMapper.insert(orderRecord);
    }

    public void update(OrderRecord orderRecord,OrderRecordInsertUpdateDto dto) {

        BeanUtils.copyProperties(dto,orderRecord);
        //注意更新时间要变
        orderRecord.setUpdateTime(new Date());
        orderRecordMapper.updateByPrimaryKey(orderRecord);

    }

    public void deleteByPrimaryKey(Integer id) {
        orderRecordMapper.deleteByPrimaryKey(id);
    }
}
