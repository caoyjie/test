package com.xxxx.crm.interceptor;

import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description:拦截器
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 22:37:53
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserMapper userMapper;
    /**
     * 拦截用户是否是登录状态
     * 在目标方法（资源）执行前，执行的方法
     * @param request
     * @param response
     * @param handler
     * @return 布尔类型，如果是true，表示目标方法可以执行，否则阻止目标方法执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        if (null==userId||userMapper.selectByPrimaryKey(userId)==null){
            throw new NoLoginException();
        }
        return true;
    }
}
