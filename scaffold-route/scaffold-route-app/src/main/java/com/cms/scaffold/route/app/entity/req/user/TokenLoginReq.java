package com.cms.scaffold.route.app.entity.req.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IDEA
 *
 *  Date:2019/3/4
 * Time:16:00
 */
@Getter
@Setter
public class TokenLoginReq implements Serializable {

    private String token;
}
