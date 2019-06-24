package com.pengyou.controller;


import com.pengyou.dto.ProductMapperDto;
import com.pengyou.enums.StatusCode;
import com.pengyou.request.EntityPostDto;
import com.pengyou.request.Product;
import com.pengyou.request.ProductDto;
import com.pengyou.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/9/16.
 */
@RestController
public class ProductController {

    private static final Logger log= LoggerFactory.getLogger(ProductController.class);

    private static final String prefix="product";

    //利用JdbcTemplate获取第二个数据源,用他来执行sql语句
    @Resource(name = "productJdbcTemplate")
    private JdbcTemplate productJdbcTemplate;


    /**
     * 查询列表
     * @return
     */
    @RequestMapping(value = prefix+"/list",method = RequestMethod.GET)
    public BaseResponse detail(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            final String sql="SELECT id,`name`,product_no,create_time,update_time FROM product";
            List<Product> dataList=productJdbcTemplate.query(sql, new ProductMapperDto());
            log.info("查询信息：{} ",dataList);

            response.setData(dataList);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 新增
     * @return
     */
    @RequestMapping(value = prefix+"/insert",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse insert(@RequestBody final ProductDto dto){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("前端传输数据： {} ",dto);

            final String sql="INSERT INTO product(`name`,product_no) VALUES(?,?)";
            int res=productJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1,dto.getName());
                    preparedStatement.setString(2,dto.getProductNo());
                }
            });

            log.info("新增的结果： {}  ",res);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 修改
     * @return
     */
    @RequestMapping(value = prefix+"/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody final ProductDto dto){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("接收到前端数据： {} ",dto);
            if (dto.getId()==null || dto.getId()<=0){
                return new BaseResponse(StatusCode.NotFound);
            }

            final String sql="UPDATE product SET `name`=?,product_no=? WHERE id=?";
            int res=productJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1,dto.getName());
                    preparedStatement.setString(2,dto.getProductNo());
                    preparedStatement.setInt(3,dto.getId());
                }
            });

            log.info("操作结果： {} ",res);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 删除
     * @return
     */
    @RequestMapping(value = prefix+"/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse delete(@RequestBody final EntityPostDto dto){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("接收到前端信息： {} ",dto);
            if (dto.getId()==null || dto.getId()<=0){
                return new BaseResponse(StatusCode.NotFound);
            }

            final String sql="DELETE FROM product WHERE id=?";
            int res=productJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setInt(1,dto.getId());
                }
            });
            log.info("操作结果：{} ",res);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}






















