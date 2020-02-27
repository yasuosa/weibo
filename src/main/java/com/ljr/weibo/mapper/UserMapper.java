package com.ljr.weibo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljr.weibo.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    void updateUserIconByUid(@Param("uid") Integer uid,@Param("icon") String iconPath);

    String findUserIconByUid(Integer uid);

    /**
     * 查询关系
     * @param userid
     * @param type
     * @return
     */
    List<Integer> queryOtherUserByUidAndType(@Param("id") Integer userid,@Param("type") String type);
}