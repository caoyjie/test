package com.xxxx.crm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * description:定义返回的数据
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 14:59:37
 */
@Getter
@Setter
public class UserModel {
    private String userIdStr;//加密用户Id
    private String userName;
    private String trueName;

}
