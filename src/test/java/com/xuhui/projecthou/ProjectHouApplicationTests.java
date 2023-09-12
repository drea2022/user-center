package com.xuhui.projecthou;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuhui.projecthou.entity.User;
import com.xuhui.projecthou.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class ProjectHouApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
//        User user=new User();
//        user.setId(0L);
//        user.setAccount("123");
//        user.setUsername("test");
//        user.setAvatarurl("");
//        user.setGender(0);
//        user.setPassword("456");
//        user.setPhone("789");
//        user.setEmail("123");
//        user.setStatus(0);
//        user.setCreatetime(LocalDateTime.now());
//        user.setUpdatetime(LocalDateTime.now());
//        user.setIsdelete(0);
//        boolean save = userService.save(user);
//        //断言
//        Assertions.assertTrue(save);
        String newPass= DigestUtils.md5DigestAsHex(("abc"+"a").getBytes(StandardCharsets.UTF_8));
        System.out.println("result = " + newPass);
    }
    @Test
    void UserRegister(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPassword,"123");
        queryWrapper.eq(User::getAccount, "123");
          /*String account="xuhui";
          String password="12345678";
          String checkPassword="12345678";
        Long aLong = userService.UserRegister(account, password, checkPassword);
        Assertions.assertEquals(-1,aLong);
        account="12";
        Long aLong1 = userService.UserRegister(account, password, checkPassword);
        Assertions.assertEquals(-1,aLong1);
        account="1 23";
        password="456";
        Assertions.assertEquals(-1,aLong1);*/

    }


}
