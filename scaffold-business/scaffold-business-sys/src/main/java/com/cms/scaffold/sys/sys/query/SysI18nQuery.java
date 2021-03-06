package com.cms.scaffold.sys.sys.query;

import com.cms.scaffold.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: zhangjiahengpoping@gmail.com
 * @date: 2019-03-12 19:15
 **/
@Setter
@Getter
public class SysI18nQuery extends BaseEntity{
    /** 模块**/
    private String model;

    /** 名称**/
    private String name;

    /** 内容**/
    private String text;

    /** 中文内容**/
    private String zhCn;

    /** 英文内容**/
    private String enUs;

    /** 印尼内容**/
    private String inId;
}
