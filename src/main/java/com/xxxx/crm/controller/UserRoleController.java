package com.xxxx.crm.controller;

import com.xxxx.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/15 20:21:50
 */
@Controller
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;
}
