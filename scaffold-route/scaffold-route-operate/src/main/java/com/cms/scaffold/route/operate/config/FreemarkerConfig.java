package com.cms.scaffold.route.operate.config;


import com.cms.scaffold.route.operate.freemarker.TableThDirective;
import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zjh on 2018/4/16.
 */
@org.springframework.context.annotation.Configuration
public class FreemarkerConfig {

    @Resource
    private Configuration configuration;
    @Resource
    private TableThDirective tableThDirective;

    @PostConstruct
    public void setSharedVariable(){
        configuration.setSharedVariable("th",tableThDirective);
        configuration.setSharedVariable("shiro",new ShiroTags());
    }

}
