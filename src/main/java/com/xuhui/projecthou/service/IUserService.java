package com.xuhui.projecthou.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xuhui.projecthou.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-01
 */
public interface IUserService extends IService<User> {

    /**
     * 用户注册
     * @Param account 用户账号
     * @Param password 密码
     * @return
     */
    Long UserRegister(String account,String password,String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param account
     * @param password
     * @return
     */
    User doLogin(String account, String password, HttpServletRequest request);

    /**
     * 获取当前用户信息
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLoginOut(HttpServletRequest request);
}
