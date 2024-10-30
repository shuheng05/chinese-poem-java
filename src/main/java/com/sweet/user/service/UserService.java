package com.sweet.user.service;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.user.protocol.param.UserLogInParam;
import com.sweet.user.protocol.param.UserSignInParam;
import com.sweet.user.protocol.vo.UserFavoritePoemVO;
import com.sweet.user.protocol.vo.UserInfoVO;
import com.sweet.user.protocol.vo.UserLogInVO;
import com.sweet.user.protocol.vo.UserSignInVO;

import java.util.List;

/**
 * @author shuheng
 */
public interface UserService {
    Result<UserLogInVO> login(UserLogInParam userLoginParam);

    Result<UserSignInVO> signIn(UserSignInParam userSignInParam);

    Result<UserInfoVO> getUserInfo();

    Result<List<UserFavoritePoemVO>> getFavoritePoems(Integer page, Integer limit);
}
