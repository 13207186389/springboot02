package com.pengyou.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengyou.enums.StatusCode;
import com.pengyou.model.entity.User;
import com.pengyou.model.mapper.UserMapper;
import com.pengyou.request.EmployeeRequest;
import com.pengyou.response.BaseResponse;
import com.pengyou.service.UserService;
import com.pengyou.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log= LoggerFactory.getLogger(UserController.class);

    private static final String prefix="user";

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = prefix+"/getUserInfo/{userId}",method = RequestMethod.GET)
    public BaseResponse getUserInfo(@PathVariable("userId") Integer userId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        if (userId<=0){
            return new BaseResponse(StatusCode.Invalid_Params);
        }
        try {
            //TODO:直接查数据库V1
            //User user=userService.getUserInfoV1(userId);
            //TODO:先查缓存再查数据库V2
            //User user=userService.getUserInfoV2(userId);
            //TODO:先查缓存,设置key过期时间V3
            //User user=userService.getUserInfoV3(userId);
            //TODO:防止缓存雪崩:先查缓存,防止key过期时间同时失效,设置随机的key过期时间
            //User user=userService.getUserInfoV4(userId);
            //TODO:防止缓存穿透:高并发访问数据库不存在的数据,我们也把key保存到缓存,值设为空
            //User user=userService.getUserInfoV5(userId);
            //TODO:防止key过多,用hash散列方式储存
            //User user=userService.getUserInfoV6(userId);
            //TODO:hash散列储存的改进版
            User user=userService.getUserInfoV7(userId);

            if(user==null){
                return new BaseResponse(StatusCode.Invalid_Params);
            }
            response.setData(user);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = prefix+"/insert/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse insertUpdate(@RequestBody @Validated EmployeeRequest employeeRequest, BindingResult bindingResult){
        //校验参数
        if(bindingResult.hasErrors()){
            return new BaseResponse(StatusCode.Invalid_Params);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);

        try {
            //判断有没有id
            if(employeeRequest.getId()!=null&&employeeRequest.getId()>0){
                //TODO:有id代表新增update
                //根据id查询出用户
                User entity=userMapper.selectByPrimaryKey(employeeRequest.getId());
                //先判断更新的用户是否存在
                if(entity==null){
                    //如果用户信息为null
                    return new BaseResponse(StatusCode.NotFound);
                }
                BeanUtils.copyProperties(employeeRequest,entity);
                //设置更新时间
                entity.setUpdateTime(new Date());
                //TODO:更新到数据库
                userMapper.updateByPrimaryKey(entity);
                //TODO:更新缓存
                userService.updateRedisCache(entity.getId());

            }else{
                //没有id代表修改insert
                User user=new User();
                BeanUtils.copyProperties(employeeRequest,user);
                //TODO:往数据库新增用户信息,并且要在mapper中返回id
                userMapper.insertSelective(user);
                //TODO:更新缓存
                userService.updateRedisCache(user.getId());
            }


        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 用户注册
     * @param employeeRequest
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = prefix+"/register",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse register(@RequestBody @Validated EmployeeRequest employeeRequest,BindingResult bindingResult){
        //效验参数
        if(bindingResult.hasErrors()){
            return new BaseResponse(StatusCode.Invalid_Params);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
        //TODO:V1一个线程执行注册,更新缓存,发送邮件
            //userService.registerV1(employeeRequest);
        //TODO:V2异步执行注册执行注册,更新缓存,发送邮件
            //userService.registerV2(employeeRequest);
        //TODO:V3异步执行注册执行注册,更新缓存,发送邮件
            userService.registerV3(employeeRequest);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
            e.printStackTrace();
        }

        return response;
    }

    @RequestMapping(value = prefix+"/register/validate",method = RequestMethod.GET)
    public BaseResponse validateMail(@RequestParam String userName,@RequestParam String timestamp,@RequestParam String encryptStr){

        try {
            //解密
            String resStr=AESUtil.decrypt(encryptStr);
            //反序列化
            Map<String,String> resMap=objectMapper.readValue(resStr, Map.class);
            //验证参数
            String userNameDecrypt=resMap.get("userName");
            String timeStampDecrypt=resMap.get("timestamp");
            if (userName.equals(userNameDecrypt) && timestamp.equals(timeStampDecrypt)){
                return new BaseResponse(StatusCode.Success);
            }else{
                return new BaseResponse(StatusCode.Fail);
            }


        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse(StatusCode.Fail);
        }

    }



}
