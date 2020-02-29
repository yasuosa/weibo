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

    Integer queryMyLikeByUidAndNid(@Param("uid") Integer userid, @Param("nid") Integer newsid);

    void insertRelationship(@Param("uid") Integer userid,@Param("aid") Integer likeUserId, @Param("type") String relationshipIdol);

    void deleteRelationship(@Param("uid") Integer userid,@Param("aid") Integer likeUserId, @Param("type") String relationshipIdol);

    Integer queryRelationship(@Param("uid") Integer userid,@Param("aid") Integer likeUserId, @Param("type") String relationshipIdol);
}