package com.pengyou.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/24.
 */
@Service
public class PoiService {

    private static final Logger log= LoggerFactory.getLogger(PoiService.class);

    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private Environment env;


    public Workbook manageSheet(List<Map<Integer, Object>> dataList,String[] headers, String sheetName){
        //定义sheet的大小,从配置文件中获取
        Integer sheetSize=env.getProperty("poi.product.excel.sheet.size",Integer.class);
        //获得我们商品dataList的集合大小
        int dataTotal=dataList.size();
        //获得我们sheet的数量
        int sheetTotal = (dataTotal%sheetSize==0)? dataTotal/sheetSize : (dataTotal/sheetSize + 1);
        //定义开始和结束下标
        int start=0;
        int end=sheetSize;
        //定义我们截取的List<Map>和workbook
        List<Map<Integer, Object>> subList;
        Workbook wb=new HSSFWorkbook();

        //遍历我们的sheet填数据
        for(int i=1;i<=sheetTotal;i++){
            //获取我们截取的List<Map>
            subList=dataList.subList(start,end);
            ///网sheet中填充数据
            wb=this.fillExcelSheetDataV2(subList,headers,sheetName+"_"+i,wb);

            //填充完后修改截取开始和结束下标
            start += sheetSize;
            end += sheetSize;
            //判断如果结束的下标超过集合的长度就让他等于集合的长度
            if (end>=dataTotal){
                end=dataTotal;
            }
        }
        //返回我们的workbook
        return wb;
    }


    /**
     * 填充数据到excel的sheet中-分sheet实战
     * @param dataList
     * @param headers
     * @param sheetName
     */
    public Workbook fillExcelSheetDataV2(List<Map<Integer, Object>> dataList, String[] headers, String sheetName,Workbook wb){
        Sheet sheet=wb.createSheet(sheetName);

        //TODO：创建sheet的第一行数据-即excel的表头
        Row headerRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        //TODO：从第二行开始塞入真正的数据列表
        int rowIndex=1;
        Row row;
        Object obj;

        for(Map<Integer, Object> rowMap:dataList){
            try {
                row=sheet.createRow(rowIndex++);

                //TODO：遍历表头行-每个key -> 取到实际的value
                for(int i=0;i<headers.length;i++){
                    obj=rowMap.get(i);

                    if (obj==null) {
                        log.debug("--");

                        row.createCell(i).setCellValue("");
                    }else if (obj instanceof Date) {
                        String tempDate=simpleDateFormat.format((Date)obj);
                        row.createCell(i).setCellValue((tempDate==null)?"":tempDate);
                    }else {
                        row.createCell(i).setCellValue(String.valueOf(obj));
                    }
                }
            } catch (Exception e) {
                log.debug("充数据到excel的sheet中 - 分sheet实战 发生异常： ",e.fillInStackTrace());
            }
        }

        return wb;
    }





    /**
     * 填充数据到excel的sheet中
     * @param dataList
     * @param headers
     * @param sheetName
     */
    public Workbook fillExcelSheetData(List<Map<Integer, Object>> dataList, String[] headers, String sheetName){
        //创建一个workbook实例
        Workbook wb=new HSSFWorkbook();
        Sheet sheet=wb.createSheet(sheetName);

        //TODO：创建sheet的第一行数据-即excel的表头
        Row headerRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        //TODO：从第二行开始塞入真正的数据列表
        int rowIndex=1;
        Row row;
        Object obj;

        for(Map<Integer, Object> rowMap:dataList){
            try {
                row=sheet.createRow(rowIndex++);

                //TODO：遍历表头行-每个key -> 取到实际的value
                for(int i=0;i<headers.length;i++){
                    obj=rowMap.get(i);

                    if (obj==null) {
                        row.createCell(i).setCellValue("");
                    }else if (obj instanceof Date) {
                        String tempDate=simpleDateFormat.format((Date)obj);
                        row.createCell(i).setCellValue((tempDate==null)?"":tempDate);
                    }else {
                        row.createCell(i).setCellValue(String.valueOf(obj));
                    }
                }
            } catch (Exception e) {
                log.debug("excel sheet填充数据 发生异常： ",e.fillInStackTrace());
            }
        }

        return wb;
    }

}