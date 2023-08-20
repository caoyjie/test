package com.xxxx.crm.base;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    /**
     * @ModelAttribute
     * 修饰方法
     * ① 该注解修饰的方法会在目标方法调用之前调用
     * ② 该注解修饰的方法可以和目标方法一样使用@RequestParam注解，以及Map
     * 可参考：http://senlt.cn/article/916452976.html
     * @param request
     */
    @ModelAttribute
    public void preHandler(HttpServletRequest request){
        //得到项目的名字，设置到作用域
        request.setAttribute("ctx", request.getContextPath());
    }

    public ResultInfo success(){
        return new ResultInfo();
    }

    /**
     * 把传来的msg设置到ResultInfo中
     * @param msg
     * @return
     */
    public ResultInfo success(String msg){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    /**
     * 把传来的msg和某对象实例设置到ResultInfo中
     * @param msg
     * @param result
     * @return
     */
    public ResultInfo success(String msg,Object result){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        resultInfo.setResult(result);
        return resultInfo;
    }

}
