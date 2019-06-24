package com.pengyou.controller;

import com.google.common.base.Strings;
import com.pengyou.dto.AppendixDto;
import com.pengyou.enums.StatusCode;
import com.pengyou.model.entity.Appendix;
import com.pengyou.model.mapper.AppendixMapper;
import com.pengyou.request.AppendixRequest;
import com.pengyou.response.BaseResponse;
import com.pengyou.service.AppendixService;
import com.pengyou.service.WebOperationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * 附件上传controller
 */
@RestController
public class AppendixController {

    private static final Logger log= LoggerFactory.getLogger(AppendixController.class);

    private static final String prefix="appendix";

    @Autowired
    private AppendixService appendixService;

    @Autowired(required = false)
    private AppendixMapper appendixMapper;

    @Autowired
    private Environment env;

    @Autowired
    private WebOperationService webOperationService;


    @RequestMapping(value = prefix+"/upload",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse upload(MultipartHttpServletRequest request){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //获取所属模块名字判断是否为空
            String moduleType=request.getParameter("moduleType");
            if(Strings.isNullOrEmpty(moduleType)){
                return new BaseResponse(StatusCode.Invalid_Params);
            }

//            //获取所属模块ID是否为空
//            String recordId=request.getParameter("recordId");
//            if(Strings.isNullOrEmpty(recordId)){
//                return new BaseResponse(StatusCode.Invalid_Params);
//            }

            //获取文件,判断是否为空
            MultipartFile file =request.getFile("fileName");
            if(file==null){
                return new BaseResponse(StatusCode.Invalid_Params);
            }

            AppendixDto dto=new AppendixDto();
            //设置文件模块信息
            dto.setModuleType(moduleType);

            //TODO:通用上传服务 返回本地路径
            String location=appendixService.uploadFile(file,dto);

            //TODO:保存上传记录
            dto.setLocation(location);

            Integer id = appendixService.saveRecord(file,dto);
            //保存附件的id返回给前端
            response.setData(id);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 更新附件所属模块: 在此之前，你需要先插入模块记录相关信息
     * @param appendixRequest
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/module/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse updateModuleAppendix(@RequestBody @Validated AppendixRequest appendixRequest, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (result.hasErrors()){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            //获取所属模块id
            Integer recordId=appendixRequest.getRecordId();
            //获取附件id,可能很多附件
            String[] appendixIds= StringUtils.split(appendixRequest.getAppendixIds(),",");
            Appendix a;
            for(String aId:appendixIds){
                try {
                    //根据aId查询附件
                    a=appendixMapper.selectByPrimaryKey(Integer.valueOf(aId));
                    a.setRecordId(recordId);
                    //判断查询出来的附件是否不为空
                    if(a!=null){
                        //如果附件不为空则更新附件表上的recordId
                        appendixMapper.updateByPrimaryKey(a);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 下载附件
     * @throws Exception
     */
    @RequestMapping(value = prefix+"/download/{id}",method = RequestMethod.GET)
    public @ResponseBody String downloadAppendix(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception{
        if (id==null || id<=0){
            return null;
        }
        try {
            //TODO：开发通用文件下载服务
            //根据附件id查询附件信息
            Appendix appendix=appendixMapper.selectByPrimaryKey(id);
            if (appendix!=null){
                //获取附件信息的全路径
                String fileLocation=env.getProperty("file.upload.root.url") + appendix.getLocation();
                //获取附件的文件流,交给response,给浏览器解析
                InputStream in=new FileInputStream(fileLocation);
                webOperationService.downloadFile(response,in,appendix.getName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }







}
