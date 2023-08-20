package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/15 09:25:57
 */
@Getter
@Setter
public class UserQuery extends BaseQuery {
    private String userName;
    private String email;
    private String phone;
}
