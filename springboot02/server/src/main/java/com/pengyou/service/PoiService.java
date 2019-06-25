package com.pengyou.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    public void manageSheet(List<Map<Integer, Object>> dataList){
//        //TODO：dataList.subList(0,99)
//        //TODO：截取 0-99 总数100 调用 fillExcelSheetData
//        //TODO：截取 100-199 总数 100  调用 fillExcelSheetData
//    }



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