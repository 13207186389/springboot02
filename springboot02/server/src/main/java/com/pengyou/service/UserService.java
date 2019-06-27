package com.pengyou.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.pengyou.listener.event.UserRegisterEvent;
import com.pengyou.model.entity.User;
import com.pengyou.model.mapper.UserMapper;
import com.pengyou.request.EmployeeRequest;
import com.pengyou.util.AESUtil;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static final Logger log= LoggerFactory.getLogger(UserService.class);

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private Environment env;

    //简单的string类型的用StringRedisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //hash散列储存西药用redisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper; //序列化为json格式字符串的工具

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * V1根据用户ID查询用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV1(Integer userId) throws Exception{
        User user=userMapper.selectByPrimaryKey(userId);
        return user;
    }

    /**
     * V2根据用户ID先查询缓存,缓存没有在查数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV2(Integer userId) throws Exception{
        //定义一个Uses 保存查询出来的数据
        User user;
        //先获得key
        String key=String.format(env.getProperty("redis.user.info.key"),userId);
        //判断缓存中有没有key
        if(stringRedisTemplate.hasKey(key)){
            //缓存中存在key,通过key查询
            String value=stringRedisTemplate.opsForValue().get(key);
            //查询出来的json串转化成User对象
            user=objectMapper.readValue(value,User.class);
        }else{
            //当缓存中没有key就查询数据库
            user=userMapper.selectByPrimaryKey(userId);
            //判断当user不为null就把数据库查询出来的user保存到缓存
            if (user!=null){
                stringRedisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(user));
            }
        }

        return user;
    }


    /**
     * V3设置key的生存时间,根据用户ID先查询缓存,缓存没有在查数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV3(Integer userId) throws Exception{
        //定义一个Uses 保存查询出来的数据
        User user;
        //先获得key
        String key=String.format(env.getProperty("redis.user.info.key"),userId);
        //判断缓存中有没有key
        if(stringRedisTemplate.hasKey(key)){
            //缓存中存在key,通过key查询
            String value=stringRedisTemplate.opsForValue().get(key);
            //查询出来的json串转化成User对象
            user=objectMapper.readValue(value,User.class);
        }else{
            //当缓存中没有key就查询数据库
            user=userMapper.selectByPrimaryKey(userId);
            //判断当user不为null就把数据库查询出来的user保存到缓存
            if (user!=null){
                //参数为 key,value,long类型的数字,时间单位(秒)
                stringRedisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(user),env.getProperty("redis.user.info.key.timeout",Long.class), TimeUnit.SECONDS);
            }
        }

        return user;
    }


    /**
     * 为了防止缓存雪崩,防止同一时间的key大量失效,设置key的随机生存时间
     * V4设置key的生存时间,根据用户ID先查询缓存,缓存没有在查数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV4(Integer userId) throws Exception{
        //定义一个Uses 保存查询出来的数据
        User user;
        //先获得key
        String key=String.format(env.getProperty("redis.user.info.key"),userId);
        //判断缓存中有没有key
        if(stringRedisTemplate.hasKey(key)){
            //缓存中存在key,通过key查询
            String value=stringRedisTemplate.opsForValue().get(key);
            //查询出来的json串转化成User对象
            user=objectMapper.readValue(value,User.class);
        }else{
            //当缓存中没有key就查询数据库
            user=userMapper.selectByPrimaryKey(userId);
            //判断当user不为null就把数据库查询出来的user保存到缓存
            if (user!=null){
                //参数为 key,value,long类型的数字,时间单位(秒)
                //随机生成key的生存时间
                Long time=RandomUtils.nextLong(30,50);
                log.info("过期时间设置: {}",time);
                stringRedisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(user),time,TimeUnit.SECONDS);
            }
        }

        return user;
    }


    /**
     * 为了防止缓存穿透,恶意高并发查询不存在的数据,我们也设置key的随机生存时间,单值为null也保存到缓存
     * V5设置key的生存时间,根据用户ID先查询缓存,缓存没有在查数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV5(Integer userId) throws Exception{
        //定义一个Uses 保存查询出来的数据
        User user=null;
        //先获得key
        String key=String.format(env.getProperty("redis.user.info.key"),userId);
        //判断缓存中有没有key
        if(stringRedisTemplate.hasKey(key)){
            //缓存中存在key,通过key查询
            String value=stringRedisTemplate.opsForValue().get(key);
            //判断由于查询出来的可能有空串,所以不能转换成user对象
            if(!Strings.isNullOrEmpty(value)) {
                //查询出来的json串转化成User对象
                user = objectMapper.readValue(value, User.class);
            }
        }else{
            //当缓存中没有key就查询数据库
            user=userMapper.selectByPrimaryKey(userId);
            //随机生成key的生存时间
            Long time=RandomUtils.nextLong(30,50);
            log.info("过期时间设置: {}",time);

            //判断当user不为null就把数据库查询出来的user保存到缓存
            if (user!=null){
                //参数为 key,value,long类型的数字,时间单位(秒)
                stringRedisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(user),time,TimeUnit.SECONDS);
            }else{
                //当查询出来得值为空的时候也保存到数据库,不过值设置为空串
                stringRedisTemplate.opsForValue().set(key,"",time,TimeUnit.SECONDS);
            }
        }

        return user;
    }

    public void updateRedisCache(Integer userId){
        try {
            //先查询用户信息
            User user=userMapper.selectByPrimaryKey(userId);
            if(user!=null){
                //获得key
                String key=String.format(env.getProperty("redis.user.info.key"),userId);
                //获得随机失效时间
                Long time=RandomUtils.nextLong(30,50);
                //将key和user保存到缓存
                stringRedisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(user),time,TimeUnit.SECONDS);

            }

        }catch (Exception e){

        }

    }

    /**
     * V6-hash散列储存,防止key过多不好查询,就用hash散列储存
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV6(Integer userId) throws Exception{
       //得到唯一的大key
        String key=env.getProperty("redis.user.info.hash.key");
        //获得操作hash的ops<大key,小key,对象>
        HashOperations<String,String,User> hashOperations=redisTemplate.opsForHash();
        //定义user用来接收查询到的结果
        User user;
        //判断缓存有没有这个大key和小key,
        if(hashOperations.hasKey(key,String.valueOf(userId))){
            //查询缓存得到user
            user=hashOperations.get(key,String.valueOf(userId));
        }else{
            //查数据库得到user
            user=userMapper.selectByPrimaryKey(userId);
            //判断数据是否为空
            if(user!=null){
                //把数据存入缓存,不存在的时候存入,可以解决分布式锁并发问题
                hashOperations.putIfAbsent(key,String.valueOf(userId),user);
            }
        }
        return user;
    }


    /**
     * V7-hash散列储存,防止key过多不好查询,就用hash散列储存  v6的改进版
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfoV7(Integer userId) throws Exception{
        //得到唯一的大key
        String key=env.getProperty("redis.user.info.hash.key");
        //获得操作hash的ops<大key,小key,对象>
        HashOperations<String,String,String> hashOperations=redisTemplate.opsForHash();
        //定义user用来接收查询到的结果
        User user=null;
        //判断缓存有没有这个大key和小key,
        if(hashOperations.hasKey(key,String.valueOf(userId))){
            //查询缓存得到user
            String value=hashOperations.get(key,String.valueOf(userId));
            //饭序列化成user对象,在反序列化前要判断是否为空
            if(!Strings.isNullOrEmpty(value)) {
                user = objectMapper.readValue(value, User.class);
            }

        }else{
            //查数据库得到user
            user=userMapper.selectByPrimaryKey(userId);
            //判断数据是否为空
            if(user!=null){
                //把数据存入缓存,不存在的时候存入,可以解决分布式锁并发问题
                hashOperations.putIfAbsent(key,String.valueOf(userId),objectMapper.writeValueAsString(user));
            }else{
                //当数据为空的时候存空串
                hashOperations.putIfAbsent(key,String.valueOf(userId),"");
            }
        }
        return user;
    }

    /**
     * 用户注册v1(同步执行)  一个线程执行3个操作 注册,更新缓存
     * @param request
     * @throws Exception
     */
    public void registerV1(EmployeeRequest request) throws Exception{
        //录入注册信息
        User user=new User();
        BeanUtils.copyProperties(request,user);
        //执行注册sql
        userMapper.insertSelective(user);
        //更新缓存
        this.updateRedisCache(user.getId());
        //发送邮件给用户
        Map<String,Object> paramsMap= Maps.newHashMap();
        paramsMap.put("userName",user.getUserName());
        paramsMap.put("url","http://www.baidu.com");
        //得到要发送邮件内容的模板
        String html=mailService.renderFreemarkerTemplate(env.getProperty("mail.template.file.location.register"),paramsMap);
        //发送邮件
        mailService.sendhtmlMail("成功入职通知",html,new String[]{user.getEmail()});
    }

    /**
     * 用户注V2册 利用ApplicationEvebt和Listener实现异步发送邮件和更新缓存
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerV2(EmployeeRequest request) throws Exception{
        //TODO：录入信息
        User user=new User();
        BeanUtils.copyProperties(request,user);
        userMapper.insertSelective(user);

        //TODO：异步发送消息
        UserRegisterEvent event=new UserRegisterEvent(this,user,"http://www.baidu.com");
        publisher.publishEvent(event);
    }


    /**
     * 用户注V3册 利用ApplicationEvebt和Listener实现异步发送邮件和更新缓存,并且发送效验邮箱的地址效验邮箱
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerV3(EmployeeRequest request) throws Exception{
        //TODO：录入信息
        User user=new User();
        BeanUtils.copyProperties(request,user);
        userMapper.insertSelective(user);

        //TODO：异步发送消息
        //获取效验邮箱的接口地址
        String url="http://localhost:9090/user/register/validate?";
        //定义拼接的参数
        //获取当前时间
        Long timestamp=System.currentTimeMillis();
        Map<String,String> dataMap=new HashMap<String, String>();
        dataMap.put("userName",user.getUserName());
        dataMap.put("timestamp",String.valueOf(timestamp));
        //转换成JSON串
        String dataMapStr=objectMapper.writeValueAsString(dataMap);
        //得到加密后的事件
        String dataMapStrEncrypt=AESUtil.encrypt(dataMapStr);
        //由于加密后的字符串中有+号,要把+号也编码
        String dataMapStrEncryptUTF8=URLEncoder.encode(dataMapStrEncrypt,"utf-8");

        log.info("加密后并编码的字符串: {}",dataMapStrEncryptUTF8);

        //拼接参数
        String params=String.format("userName=%s&timestamp=%s&encryptStr=%s",user.getUserName(),timestamp,dataMapStrEncryptUTF8);
        //拼接最终地址
        url=url+params;

        log.info("拼接后的url: {}",url);

        UserRegisterEvent event=new UserRegisterEvent(this,user,url);
        publisher.publishEvent(event);
    }


}
