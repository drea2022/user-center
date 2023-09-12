package com.xuhui.projecthou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuhui.projecthou.dao.UserMapper;
import com.xuhui.projecthou.entity.User;
import com.xuhui.projecthou.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xuhui.projecthou.contant.UserContant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 盐值
     */
    private static final String SALT="xuhui";


    @Override
    public Long UserRegister(String account, String password,String checkPassword,String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(account,password,checkPassword,planetCode)){
            return (long) -1;
        }
        //账号长度不能低于4
        if(account.length()<4){
            return (long) -1;
        }
        //星球编号长度不能大于5
        if (planetCode.length() > 5){
            return (long) -1;
        }
        //密码长度不能低于8
        if (password.length()<8 || checkPassword.length()<8){
            return (long) -1;
        }
        //账号不能包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(account);
        if(matcher.find()){
            return (long) -1;
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("account",account);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            return (long) -1;
        }
        //星球编号不能重复
         queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
         count = userMapper.selectCount(queryWrapper);
        if(count >0){
            return (long) -1;
        }
        //密码和校验密码相同
        if (!password.equals(checkPassword)){
            return (long) -1;
        }
        //2.加密
        String newPassword= DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //3.插入数据
        User user=new User();
        user.setAccount(account);
        user.setPassword(newPassword);
        user.setPlanetCode(planetCode);
        user.setCreatetime(LocalDateTime.now());
        boolean save = this.save(user);
        if (!save){
            return (long) -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String account, String password, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(account,password)){
            return null;
        }
        //账号长度不能低于4
        if(account.length()<4){
            return null;
        }
        //密码长度不能低于8
        if (password.length()<8 ){
            return null;
        }
        //账号不能包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(account);
        if(matcher.find()){
            return null;
        }
        //2.加密
        String newPassword= DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //查询用户是否存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPassword, newPassword);
        lambdaQueryWrapper.eq(User::getAccount, account);
        User user = userMapper.selectOne(lambdaQueryWrapper);

        //用户不存在
        if (user == null){
            log.info("user login failed,account cannot match password");
            return null;
        }
        //3.用户脱敏
        User safetyUser=getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return safetyUser;
    }

    /**
     * 用户脱敏（隐藏密码）
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser=new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setAccount(originUser.getAccount());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setAvatarurl(originUser.getAvatarurl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setStatus(0);
        safetyUser.setCreatetime(originUser.getCreatetime());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    @Override
    public int   userLoginOut(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}
