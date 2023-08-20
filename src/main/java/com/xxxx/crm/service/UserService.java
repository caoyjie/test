package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 14:31:07
 */
@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName, String userPwd) {
        checkLoginParams(userName, userPwd);
        User user = userMapper.queryUserByName(userName);
        //如果用户不存在就要抛异常了
        AssertUtil.isTrue(user == null, "用户不存在!");
        checkUserPwd(userPwd, user.getUserPwd());
        //返回构建用户对象
        return buildUserInfo(user);
    }

    /**
     * 修改密码
     * 1. 通过用户ID查询用户记录，返回用户对象
     * 2. 参数校验
     * 待更新用户记录是否存在 （用户对象是否为空）
     * 判断原始密码是否为空
     * 判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
     * 判断新密码是否为空
     * 判断新密码是否与原始密码一致 （不允许新密码与原始密码）
     * 判断确认密码是否为空
     * 判断确认密码是否与新密码一致
     * 3. 设置用户的新密码
     * 需要将新密码通过指定算法进行加密（md5加密）
     * 4. 执行更新，判断受影响的行数
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user, "待更新记录不存在！");
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "");
    }

    /**
     * 修改密码的参数校验
     * 判断原始密码是否为空
     * 判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
     * 判断新密码是否为空
     * 判断新密码是否与原始密码一致 （不允许新密码与原始密码）
     * 判断确认密码是否为空
     * 判断确认密码是否与新密码一致
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原始密码不能为空！");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原始密码不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空！");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能与原始密码相同！");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "确认密码不能为空！");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "确认密码与新密码不一致！");
    }

    /**
     * 构建需要返回给客户端的用户对象
     *
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
        //设置加密的用户Id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     *
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //加密客户端传来的密码
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码不正确");
    }

    /**
     * 参数判断
     *
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空!");
    }

    /**
     * 查询所有的销售人员
     *
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户 绑定角色
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        //参数校验
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), null);
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败");

        //用户角色关联的方法
        relationUserRole(user.getId(),user.getRoleIds());

    }

    /**
     * 用户角色关联
     * 将用户和角色进行绑定
     * @param userId 用户id确定是哪一个用户
     * @param roleIds 角色id决定绑定哪一个角色
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //通过用户id查询角色记录
        Integer count=userRoleMapper.countUserRoleByUserId(userId);
        //判断角色记录是否存在
        if (count>0){//如果存在，则删除所有
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败");
        }
        //判断角色记录是否存在，如果有，就添加
        if (StringUtils.isNotBlank(roleIds)){
            //将用户角色设置到集合中，进行批量添加
            List<UserRole> userRoleList=new ArrayList<>();
            String[] roleIdsArray = roleIds.split(",");
            for (String roleId : roleIdsArray) {
                UserRole userRole=new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList)!=userRoleList.size(),"用户角色分配失败");
        }
    }

    /**
     * 更新用户
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        AssertUtil.isTrue(null == user.getId(), "待更新记录不存在");
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在");
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户更新失败");
        //用户角色关联的方法
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 参数校验
     *
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        User temp = userMapper.queryUserByName(userName);
        //如果是修改，2种可能，1.用户名被其他人占用（不能改），2.要修改的就是当前用户本身。
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)), "用户名已存在，请重新输入");
        AssertUtil.isTrue(StringUtils.isBlank(email), "用户邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "用户手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号格式不正确");
    }

    /**
     * 用户删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0, "待删除记录不存在！");
        //执行删除，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "删除失败！");

        //遍历用户id的数组
        for(Integer userId:ids){
            //通过用户id查询对应的角色记录
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            //判断用户角色记录是否存在
            if (count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"删除失败");
            }
        }
    }
}
