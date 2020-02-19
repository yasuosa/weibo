package com.ljr.weibo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljr.weibo.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {


    void updateUserIconByUid(@Param("uid") Integer uid,@Param("icon") String iconPath);

    String findUserIconByUid(Integer uid);

}