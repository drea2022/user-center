package com.xuhui.projecthou.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author xuhui
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String account;

    private String password;

    private String checkPassword;

    private String planetCode;
}
