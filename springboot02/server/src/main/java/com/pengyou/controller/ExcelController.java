package com.pengyou.controller;

import com.pengyou.enums.StatusCode;
import com.pengyou.model.entity.Product;
import com.pengyou.model.mapper.ProductMapper;
import com.pengyou.response.BaseResponse;
import com.pengyou.service.PoiService;
import com.pengyou.service.ProductService;
import com.pengyou.service.WebOperationService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelController {

    private static final Logger log= LoggerFactory.getLogger(ExcelController.class);

    private static final String prefix="excel";

    @Autowired(required = false)
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private Environment env;

    @Autowired
    private PoiService poiService;

    @Autowired
    private WebOperationService webOperationService;


    /**
     * 查询产品信息列表
     * @param name
     * @return
     */
    @RequestMapping(value = prefix+"/product/list",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse list(String name){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            List<Product> products=productMapper.selectAll(name);
            response.setData(products);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 不分sheet,poi导出我们的商品表格
     * @param name
     * @param response
     * @return
     */
    @RequestMapping(value = prefix+"/product/export",method = RequestMethod.GET)
    //这个@ResponseBody 作用是以流的形式返回给浏览器
    public @ResponseBody String export(String name, HttpServletResponse response){
        //定义表格头信息
        String[] headers=new String[]{"名称","单位","单价","库存量","备注","采购日期"};
        //根据名字查询所有产品信息
        List<Product> products=productMapper.selectAll(name);
        try {

            if(products!=null && products.size()>0) {
                //TODO：将产品信息列表封装在map中list->list-map
                List<Map<Integer, Object>> listMap = productService.manageProductList(products);
                //TODO：将list-map塞入真正的excel对应的workbook(相当于excel)并且返回workbook
                Workbook wb = poiService.fillExcelSheetData(listMap, headers, env.getProperty("poi.product.excel.sheet.name"));
                //TODO：利用我们之前的通用下载附件工具,将excel实例(workbook)以流的形式写回浏览器
                webOperationService.downloadExcel(response, wb, env.getProperty("poi.product.excel.file.name"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 分sheet,poi导出我们的商品表格
     * @param name
     * @param response
     * @return
     */
    @RequestMapping(value = prefix+"/product/exportv2",method = RequestMethod.GET)
    //这个@ResponseBody 作用是以流的形式返回给浏览器
    public @ResponseBody String exportv2(String name, HttpServletResponse response){
        //定义表格头信息
        String[] headers=new String[]{"名称","单位","单价","库存量","备注","采购日期"};
        //根据名字查询所有产品信息
        List<Product> products=productMapper.selectAll(name);
        try {

            if(products!=null && products.size()>0) {
                //TODO：将产品信息列表封装在map中list->list-map
                List<Map<Integer, Object>> listMap = productService.manageProductList(products);
                //TODO：v2 分sheet导出实战,将list-map塞入真正的excel对应的workbook(相当于excel)并且返回workbook
                Workbook wb = poiService.manageSheet(listMap, headers, env.getProperty("poi.product.excel.sheet.name"));
                //TODO：利用我们之前的通用下载附件工具,将excel实例(workbook)以流的形式写回浏览器
                webOperationService.downloadExcel(response, wb, env.getProperty("poi.product.excel.file.name"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
