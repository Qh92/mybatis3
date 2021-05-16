package com.qinh.entity;

/**
 * 希望数据库保存的是100,200等状态码，而不是默认 0,1索引或者枚举的名称
 *
 * @author Qh
 * @version 1.0
 * @date 2021-05-15-23:58
 */
public enum EmpStatus {
    LOGIN(100,"用户登录"),LOGOUT(200,"用户登出"),REMOVE(300,"用户不存在");

    private Integer code;
    private String message;

    EmpStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //按照状态码返回枚举对象
    public static EmpStatus getEmpStatusByCode(Integer code){
        switch (code){
            case 100:
                return LOGIN;
            case 200:
                return LOGOUT;
            case 300:
                return REMOVE;
            default:
                return LOGOUT;
        }
    }
}
