package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 10:37:18
 */
@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 系统登录⻚
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /**
     * 系统界⾯欢迎⻚
     *
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 后端管理主⻚⾯
     *
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {
        //取用户对象的用户名，显示在main的右上角
        //获取cookie中的用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        //登录以后的用户姓名始终要显示在页面中，不受页面跳转的影响（数据只显示一次），所以不能设置在请求域中，而是session作用域
        request.getSession().setAttribute("user", user);
        return "main";
    }

}
