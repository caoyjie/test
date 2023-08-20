package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
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
 * @date 2023/08/12 09:14:10
 */
@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();

        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;

    }

    /**
     * 添加营销机会
     * 1. 参数校验
     * customerName客户名称    非空
     * linkMan联系人           非空
     * linkPhone联系号码       非空，手机号码格式正确
     * 2. 设置相关参数的默认值
     * createMan创建人        当前登录用户名
     * assignMan指派人
     * 如果未设置指派人（默认）
     * state分配状态 （0=未分配，1=已分配）
     * 0 = 未分配
     * assignTime指派时间
     * 设置为null
     * devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     * 0 = 未开发 （默认）
     * 如果设置了指派人
     * state分配状态 （0=未分配，1=已分配）
     * 1 = 已分配
     * assignTime指派时间
     * 系统当前时间
     * devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     * 1 = 开发中
     * isValid是否有效  （0=无效，1=有效）
     * 设置为有效 1= 有效
     * createDate创建时间
     * 默认是系统当前时间
     * updateDate
     * 默认是系统当前时间
     * 3. 执行添加操作，判断受影响的行数
     *
     * @param saleChance
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销机会失败！");
    }

    /**
     * 参数校验
     * customerName客户名称    非空
     * linkMan联系人           非空
     * linkPhone联系号码       非空，手机号码格式正确
     *
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系号码不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "联系号码格式不正确！");
    }

    /**
     * 更新营销机会
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
         //1. 参数校验
        //  营销机会ID  非空，数据库中对应的记录存在
        AssertUtil.isTrue(null == saleChance.getId(), "待更新记录不存在！");
        // 通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断数据库中对应的记录存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

//        2. 设置相关参数的默认值
//           updateDate更新时间  设置为系统当前时间
        saleChance.setUpdateDate(new Date());
//           assignMan指派人
//              原始数据未设置
        if (StringUtils.isBlank(temp.getAssignMan())) {
            // 修改后未设置 不需要操作
//         修改后已设置
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                //assignTime指派时间  设置为系统当前时间
                saleChance.setAssignTime(new Date());
//             分配状态    1=已分配
                saleChance.setState(1);
//             开发状态    1=开发中
                saleChance.setDevResult(1);
            }

        } else {//  原始数据已设置
            //修改后未设置
            if (StringUtils.isBlank(saleChance.getAssignMan())) {
                // assignTime指派时间  设置为null
                saleChance.setAssignTime(null);
//                      分配状态    0=未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
//                      开发状态    0=未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {//修改后已设置
                //判断修改前后是否是同一个指派人
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    //如果不是，则需要更新 assignTime指派时间  设置为系统当前时间
                    saleChance.setUpdateDate(new Date());
                } else {
                    //如果是，设置指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
//     *  3. 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "更新营销机会失败！");

    }

    /**
     * 删除营销机会
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        //判断id是否为kong
        AssertUtil.isTrue(null==ids||ids.length<1,"待删除的记录不存在");
        //执行删除（更新），判断受 影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!=ids.length,"营销机会删除失败！");
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(null==id,"待更新记录不存在");
        SaleChance saleChance=saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==saleChance,"待更新记录不存在");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"开发状态更新失败");
    }
}
