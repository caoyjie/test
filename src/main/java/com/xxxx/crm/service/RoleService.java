package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/15 15:24:39
 */
@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有角色列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     *
     * @param role
     */
    public void addRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null, "角色名称已存在，请重新输入");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败");
    }

    /**
     * 更新角色
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(null == role.getId(), "待更新记录不存在");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null && (!temp.getId().equals(role.getId())), "角色名称已存在，无法更新");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "更新失败");

    }

    /**
     * 删除角色
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(null==roleId,"待删除记录不存在！");
        //查出要删除的记录是哪一条
        Role role=roleMapper.selectByPrimaryKey(roleId);
        //如果没有查到，报错
        AssertUtil.isTrue(role==null,"待删除记录不存在！");
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败！");
    }

}
