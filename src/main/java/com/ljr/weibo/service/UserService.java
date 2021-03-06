package com.ljr.weibo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import com.ljr.weibo.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {


    /**
     * 跟换头像地址
     * @param uid
     * @param iconPath
     * @return
     */
    String updateUserIconByUid(Integer uid,String iconPath);


    /**
     * 查询用户头像
     * @param uid
     * @return
     */
    String findUserIconByUid(Integer uid);

    ResultObj likeUser(Integer userid, Integer likeUserId);

    ResultObj unLikeUser(Integer userid, Integer likeUserId);

    ResultObj removeFan(Integer userid, Integer likeUserId);

    ResultObj saveUser(UserVo userVo);

    DataGridView showFans() throws UserIsNotException;


    DataGridView showIdol() throws UserIsNotException;


    DataGridView showMeIndex() throws UserIsNotException;


    DataGridView showOthersIndex(Integer uid) throws UserIsNotException;

    List<Map<String,Object>> queryByUserName(String username);
}


