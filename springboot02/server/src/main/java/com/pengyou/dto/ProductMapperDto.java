package com.pengyou.dto;



import com.pengyou.request.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/9/16.
 */
public class ProductMapperDto implements RowMapper<Product> {

    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product user=new Product();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setProductNo(rs.getString("product_no"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        user.setUpdateTime(rs.getTimestamp("update_time"));
        return user;
    }
}