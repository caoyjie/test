package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 14:31:53
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //调用service
        UserModel userModel = userService.userLogin(userName, userPwd);
        //设置ResultInfo的result的值，将数据返回给请求
        resultInfo.setResult(userModel);
        /*//try catch捕获service的异常，如果service抛了异常，表示失败，否则成功
        try{
            //调用service
            UserModel userModel=userService.userLogin(userName,userPwd);
            //设置ResultInfo的result的值，将数据返回给请求
            resultInfo.setResult(userModel);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登录失败");
        }*/
        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);
        /*try{
             Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
             userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
         }catch(ParamsException p){
             resultInfo.setCode(p.getCode());
             resultInfo.setMsg(p.getMsg());
             p.printStackTrace();
         }catch (Exception e){
             resultInfo.setCode(500);
             resultInfo.setMsg("修改密码失败");
             e.printStackTrace();
         }*/
        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 查询所有的销售人员
     *
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 分页多条件查询用户列表
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("用户添加成功");
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功");
    }

    /**
     * 打开添加或修改用户的页面
     *
     * @param id 修改的时候用
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request) {
        if (id != null) {
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo", user);
        }
        return "user/add_update";
    }

    /**
     * 用户删除
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("用户删除成功");
    }

}
