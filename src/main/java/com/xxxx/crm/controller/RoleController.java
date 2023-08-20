package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/15 15:27:59
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查询所有角色列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("queryAllRoles")
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleService.queryAllRoles(userId);
    }

    /**
     * 分页查询条件列表
     *
     * @param roleQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 进入角色管理的页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "role/role";
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleService.addRole(role);
        return success("角色添加成功");
    }

    /**
     * 进入添加角色信息的页面
     *
     * @return
     */
    @RequestMapping("toAddOrUpdateRolePage")
    public  String toAddOrUpdateRolePage(Integer roleId, HttpServletRequest request){
        //在点击数据表格的‘编辑‘的时候会传递一个id，这里用它查出role对象，存到request作用域中，用于修改操作
        //在前台的add_update.ftl页面，隐藏域会存储该role的主键，表单的元素会用el表达式取出其他各种信息，
        //所有以上步骤，是为了在点击确认的时候，拿id和各种信息到数据库里修改
        if (roleId!=null){
         Role role=roleService.selectByPrimaryKey(roleId);
         request.setAttribute("role",role);
        }
        return "role/add_update";
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleService.updateRole(role);
        return success("角色更新成功");
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        return success("角色删除成功");
    }

}
