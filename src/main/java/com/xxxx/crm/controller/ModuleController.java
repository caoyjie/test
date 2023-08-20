package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/16 15:59:54
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有资源列表
     *
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules() {
        return moduleService.queryAllModules();
    }

    /**
     * 进入授权页面
     *
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        //将需要授权的角色id设置到隐藏域中
        //在角色绑定资源的时候，需要id，才能在数据库中设置
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }
}
