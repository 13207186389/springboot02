package com.pengyou.service;


import com.google.common.collect.Maps;
import com.pengyou.model.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/24.
 */
@Service
public class ProductService {

    private static final Logger log= LoggerFactory.getLogger(ProductService.class);

    /**
     * 处理产品信息列表 转化为list-map->以用于后续塞入excel的sheet中
     * @param products
     * @return
     */
    public List<Map<Integer, Object>> manageProductList( List<Product> products){
        //TODO：excel表头--"名称","单位","单价","库存量","备注","采购日期"

        List<Map<Integer, Object>> listMap=new LinkedList<Map<Integer, Object>>();

        Map<Integer,Object> rowMap;
        for (Product p:products){
            rowMap= Maps.newHashMap();

            rowMap.put(0,p.getName());
            rowMap.put(1,p.getUnit());
            rowMap.put(2,p.getPrice());
            rowMap.put(3,p.getStock());
            rowMap.put(4,p.getRemark());
            rowMap.put(5,p.getPurchaseDate());

            listMap.add(rowMap);
        }

        return listMap;
    }

}


































