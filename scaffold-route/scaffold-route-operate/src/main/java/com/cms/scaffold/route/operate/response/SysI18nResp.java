package com.cms.scaffold.route.operate.response;

import com.cms.scaffold.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: zjh
 * @date: 2019-03-12 19:02
 **/
@Setter
@Getter
public class SysI18nResp extends BaseEntity{
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
