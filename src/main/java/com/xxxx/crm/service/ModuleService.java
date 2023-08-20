package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/16 15:55:43
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

}
