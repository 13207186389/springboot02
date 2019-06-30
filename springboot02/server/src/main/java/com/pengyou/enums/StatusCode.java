package com.pengyou.enums;

public enum StatusCode {
    Success(0,"成功"),
    Fail(-1,"失败"),
    NotFound(10010,"不存在"),
    Validate_params(10020,"请求参数不合法"),
    Invalid_Params(10012,"请求参数不合法!"),
    Validate_UserName_Expire(10013,"未在有效时间内验证注册信息，请重新注册!!");


    private Integer code;
    private  String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
