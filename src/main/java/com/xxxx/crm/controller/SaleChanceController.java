package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/12 09:14:57
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询（分页多条件查询）
     * 如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     *
     * @param saleChanceQuery
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(HttpServletRequest request, SaleChanceQuery saleChanceQuery, Integer flag) {
        //判断flag的值
        if (flag != null && flag == 1) {
            //查询客户开发计划
            //设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //设置分派人（当前登录的用户id），从cookie中拿
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     *
     * @param saleChance
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        String userName = CookieUtil.getCookieValue(request, "userName");
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功");
    }

    /**
     * 更新营销机会
     *
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会更新成功");
    }

    /**
     * 进入添加/修改营销机会数据页面
     *
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        if (saleChanceId != null) {
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 删除营销机会
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteSaleChance(Integer[] ids) {
        //调用service的方法
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功");

    }

    /**
     * 更新营销机会的开发状态
     *
     * @param id
     * @param devResult
     * @return
     */
    @ResponseBody
    @PostMapping("updateSaleChanceDevResult")
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id, devResult);
        return success("开发状态更新成功");
    }
}
