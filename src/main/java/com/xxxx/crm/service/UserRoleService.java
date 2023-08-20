package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/15 20:20:55
 */
@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}
