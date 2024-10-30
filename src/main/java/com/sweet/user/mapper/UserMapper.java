package com.sweet.user.mapper;

import com.sweet.user.protocol.dto.UserFavoritePoemDTO;
import com.sweet.user.protocol.po.User;
import com.sweet.user.protocol.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shuheng
 */
@Mapper
public interface UserMapper {
    /**
     * 根据邮箱查询用户
     */
    User getUserByEmail(String email);

    User getUserByUsername(String username);

    void insert(User user);

    List<UserFavoritePoemDTO> getFavoritePoemsByUserId(@Param("userId") String userId, @Param("from") Integer from, @Param("size") Integer size);

    UserInfoVO getUserInfoById(String userId);
}
