package com.neuedu.common;

/**
 * @author jyw
 * @date 2019/10/22-19:35
 */
public class ResponseCode {


    // 成功的状态吗
    public static final int SUCCESS = 0;
    // 失败的状态码
    public static final int ERROR = 100;
    // 参数为空状态码
    public static final int PARAM_NOT_NULL = 1;
    // 用户名已存在状态码
    public static final int USERNAME_EXIST = 2;
    // 邮箱已存在状态码
    public static final int EMAIL_EXIST = 3;
    // 用户未登录状态码
    public static final int  NOT_LOGIN= 99;
}
