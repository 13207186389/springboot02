package com.pengyou.exception;

/**自定义的一个异常类
 * Created by Administrator on 2018/9/23.
 */
public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(message);
    }
}