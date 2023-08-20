package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/14 14:36:40
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询客户开发计划
     *
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<>();

        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;

    }

    /**
     * 添加客户开发计划项数据
     * 1.参数校验
     * 机会id    非空 记录必须存在
     * 计划项内容 非空
     * 计划项时间 非空
     * 2. 参数默认值
     * is_valid  1
     * createDate 系统时间
     * updateDate  系统时间
     * 3.执行添加 判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //1.参数校验
        //机会id    非空 记录必须存在
        checkCusDevPlanParams(cusDevPlan);
//        2. 参数默认值
//        is_valid  1
        cusDevPlan.setIsValid(1);
//        createDate 系统时间
        cusDevPlan.setCreateDate(new Date());
//        updateDate  系统时间
        cusDevPlan.setUpdateDate(new Date());
//        3.执行添加 判断结果
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "计划项数据添加失败");
    }

    /**
     * 更新客户开发项数据
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        //1.参数校验
        //计划项id    非空 记录必须存在
        AssertUtil.isTrue(null == cusDevPlan.getId() || null == cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()), "数据异常，请重试");
        checkCusDevPlanParams(cusDevPlan);
        //2. 参数默认值， 修改时间是系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        //3.执行更新 判断结果
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "计划项更新失败");
    }

    /**
     * 1.参数校验
     * 营销机会id 非空 记录必须存在
     * 计划项内容 非空
     * 计划项时间 非空
     *
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会id 非空 记录必须存在
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(sId == null || saleChanceMapper.selectByPrimaryKey(sId) == null, "数据异常，请重试");

        // 计划项内容 非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划项内容不能为空");
        //计划项时间 非空
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "计划时间不能为空");
    }

    /**
     * 删除计划项
     * 1.判断是否为空，数据存在
     * 2.修改isValid属性
     * 3.执行更新
     *
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        //1.判断是否为空，数据存在
        AssertUtil.isTrue(null == id, "待删除记录不存在");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "计划项数据删除成功");

    }
}
