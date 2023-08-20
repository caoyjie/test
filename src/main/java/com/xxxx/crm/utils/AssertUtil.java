package com.xxxx.crm.utils;

import com.xxxx.crm.exceptions.ParamsException;

public class AssertUtil {

    /**
     * 判断flag的真假，如果为真，则抛出ParamsException异常并设置异常信息
     * @param flag
     * @param msg
     */
    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
