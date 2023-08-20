package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * description:营销机会查询类
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/12 09:46:47
 */
@Getter
@Setter
public class SaleChanceQuery extends BaseQuery {
    //分页参数
    //条件参数
    private String customerName;
    private String creatMan;
    private Integer state;//分配状态 0=未分配 1=已分配
    //开发状态
    private  String devResult;
    //指派人
    private Integer assignMan;

}
