package com.pengyou.service;

import com.pengyou.model.entity.OrderRecord;
import com.pengyou.request.OrderRecordInsertUpdateDto;

import java.util.List;

public interface OrderRecordService {

    public OrderRecord selectPrimaryKey(Integer id);

    public List<OrderRecord> selectAll();

    public void insert(OrderRecordInsertUpdateDto dto);

    public void update(OrderRecord orderRecord,OrderRecordInsertUpdateDto dto);

    public void deleteByPrimaryKey(Integer id);


}
