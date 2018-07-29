package com.yeah.bztools.net;

/**
 * 网络访问的一些结果
 */
public class Result {
    protected int ret;
    protected String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static final int UNKNOW_ERROR = -1;
    public static final int SUCCESS = 0;
    public static final int SYSTEM_ERROR = 9; //系统错误
    //protected static final String UNKNOW_ERROR = "未知错误";
    public static final int REG_LOGIN_FAIL = -3;
    public static final int TK_CODE_SESSION_SID = 1;     //sid失效
    public static final int TK_CODE_SESSION_USERINFO = 2;// 获取用户信息失败,
    public static final int TK_CODE_SESSION_TIMEOUT = 8;
    public static final int PASSWORD_ERROR = 1010; //密码错误
    public static final int REQUEST_TIME_OUT = 1011;//请求超时
    public static final int DATA_DEC_FAIL = 1012;//json数据解析错误,返回字段和bean里的字段不匹配

    //图片上传
    public static final int IMAGE_UPLOAD_SUCCESS = 100;//图片上传成功
    public static final int IMAGE_UPLOAD_ERROE = -100;//图片上传失败


    public void setRet(int ret) {
        this.ret = ret;
    }

    public boolean isSuccess() {
        return ret == SUCCESS;
    }




    public boolean isExpired() {
        if (ret == TK_CODE_SESSION_SID) {
        	return true;
        } else if (ret == TK_CODE_SESSION_USERINFO) {
        	return true;
        } else if (ret == TK_CODE_SESSION_TIMEOUT) {
        	return true;
        } else {
        	return false;
        }
    }

    public String getMsg() {
        return msg;
    }
}
