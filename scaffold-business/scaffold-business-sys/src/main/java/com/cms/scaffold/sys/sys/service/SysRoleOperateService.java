package com.cms.scaffold.sys.sys.service;

import com.cms.scaffold.sys.sys.domain.SysOperate;
import com.cms.scaffold.sys.sys.domain.SysRoleOperate;
import com.cms.scaffold.common.base.BaseService;

import java.util.List;

public interface SysRoleOperateService extends BaseService<SysRoleOperate> {

    /**
     * 得到角色
     */
    SysRoleOperate selectByOperateId(Long operateId);

    void saveOperateAndRole(SysRoleOperate sysRoleOperate, SysOperate sysOperate);
    /**
     * 根据角色ID查询角色操作员关联表
     * @param id
     * @return
     */
    List<SysRoleOperate> findListByRole(Long id);
}
