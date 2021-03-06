package com.cms.scaffold.sys.sys.ao;

import com.cms.scaffold.sys.sys.domain.SysRoleMenu;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: zjh
 * @date: 2019-03-12 23:53
 **/
@Setter
@Getter
public class SysRoleMenuAO extends SysRoleMenu {
    /**
     * 角色id
     **/
    private Long roleId;

    /**
     * 菜单id
     **/
    private Long menuId;

}
