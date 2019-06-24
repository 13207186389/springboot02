package com.pengyou.service;

import com.pengyou.dto.AppendixDto;
import com.pengyou.model.entity.Appendix;
import com.pengyou.model.mapper.AppendixMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AppendixService {

    private static final Logger log= LoggerFactory.getLogger(AppendixService.class);

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private AppendixMapper appendixMapper;

    /**
     * 通用文件上传
     * @param file
     * @param dto
     * @return
     */
    public String uploadFile(MultipartFile file, AppendixDto dto) throws Exception{
        //判断文件是否为空
        if(file==null){
            throw new RuntimeException("附件为空");
        }
        //获取文件名加后缀
        String fileName=file.getOriginalFilename();
        //从获取的fileName中获取后缀
        String suffix= StringUtils.substring(fileName,fileName.lastIndexOf("."));

        //TODO:获取文件保存的位置
        //以当前年月日来建立文件夹
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        String dateDirectory=dateFormat.format(new Date());
        //获取服务器上保存附件的文件夹根路径(一般在配置文件中获取)
        String rootUrl=env.getProperty("file.upload.root.url");
        //拼接最终文件保存文件夹路径  根路径+模块名+日期文件夹
        String endUrl=rootUrl+ File.separator+dto.getModuleType()+File.separator+dateDirectory+File.separator;
        //获取路径文件夹
        File endFile=new File(endUrl);
        //判断文件夹是否存在
        if(!endFile.exists()){
            //如果不存在就创建
            endFile.mkdirs();
        }

        //TODO:定义附件上传后的名字
        dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        String destFileName=dateFormat.format(new Date())+suffix;
        //定义文件全路径名
        File destFile=new File(endUrl+ File.separator + destFileName);
        //上传附件
        file.transferTo(destFile);

        //获取文件上传后再跟目录下的路径
        String location=File.separator+dto.getModuleType()+File.separator+dateDirectory+File.separator+destFileName;


        return location;

    }


    /**
     * 保存上传附件记录
     * @param file
     * @param dto
     */
    public Integer saveRecord(MultipartFile file, AppendixDto dto) throws Exception{
        Appendix entity=new Appendix();
        BeanUtils.copyProperties(dto,entity);

        entity.setName(file.getOriginalFilename());
        entity.setSize(file.getSize());

        appendixMapper.insertSelective(entity);

        return entity.getId();
    }




}
