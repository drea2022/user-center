package com.xuhui.projecthou.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuhui.projecthou.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
