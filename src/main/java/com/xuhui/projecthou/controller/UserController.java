package com.xuhui.projecthou.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuhui.projecthou.entity.User;
import com.xuhui.projecthou.entity.request.UserLoginRequest;
import com.xuhui.projecthou.entity.request.UserRegisterRequest;
import com.xuhui.projecthou.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.xuhui.projecthou.contant.UserContant.ADMIN_ROLE;
import static com.xuhui.projecthou.contant.UserContant.USER_LOGIN_STATE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest request){
       if (request == null){
           return  null;

       }
        String account = request.getAccount();
        String password = request.getPassword();
        String checkPassword = request.getCheckPassword();
        String planetCode=request.getPlanetCode();
        if (StringUtils.isAnyBlank(account, password, checkPassword,planetCode)){
            return null;
        }
        Long result= userService.UserRegister(account, password, checkPassword,planetCode);
        return result;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest request, HttpServletRequest servletRequest){
        if (request == null){
            return null;
        }
        String account = request.getAccount();
        String password = request.getPassword();
        if (StringUtils.isAnyBlank(account,password)){
            return null;
        }
        User user= userService.doLogin(account, password,servletRequest);
        return user;
    }

    @PostMapping("/logout")
    public Integer userLoginOut(HttpServletRequest request){
        if (request == null){
            return null;
        }
        int result=userService.userLoginOut(request);
        return result;
    }
    @GetMapping("/current")
    public User getCurrent(HttpServletRequest request){
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User current=(User) attribute;
        if (current == null){
            return null;
        }
        Long userId=current.getId();
        //todo 校验用户是否合法
        User byId = userService.getById(userId);
        User safetyUser=userService.getSafetyUser(byId);
        return safetyUser;
    }

    @GetMapping("/search")
    public List<User> searchByName(String username,HttpServletRequest request){
        if (!isAdmin(request)){
           return null;
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList=userService.list(queryWrapper);
        List<User> list= userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return list;
    }

    @PostMapping("/delete")
    public Boolean deleteByName(Long id,HttpServletRequest request){
        if (!isAdmin(request)){
            return null;
        }
        if (id<=0){
            return false;
        }
        boolean b = userService.removeById(id);
        return b;
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //键权 只有管理员可以查询
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User) attribute;
        return user != null && user.getRole() == ADMIN_ROLE;
    }
}

