package com.cms.scaffold.route.app.entity.req.user;

import lombok.Getter;
import lombok.Setter;

/**
 * Created with IDEA
 *
 *  Date:2019/2/22
 * Time:15:21
 */
@Getter
@Setter
public class UserLoginReq {

    private String mobilePhone;

    private String password;


}
