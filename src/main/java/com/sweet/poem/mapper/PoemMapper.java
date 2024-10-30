package com.sweet.poem.mapper;

import com.sweet.poem.protocol.dto.PoemDto;
import com.sweet.poem.protocol.po.UserDislikePoemPO;
import com.sweet.poem.protocol.po.UserLikePoemPO;
import com.sweet.poem.protocol.query.PoemsQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shuheng
 */
@Mapper
public interface PoemMapper {

    List<PoemDto> selectPage(PoemsQueryDto poemsQueryDto);

    void updateLikeCountById(@Param("poemId") Integer poemId, @Param("likeCount") Integer likeCount);

    void updateDislikeCountById(@Param("poemId") Integer poemId, @Param("dislikeCount") Integer dislikeCount);

    void updateFavoriteCountById(@Param("poemId") Integer poemId, @Param("favoriteCount") Integer favoriteCount);

    void insertToUserLikePoem(@Param("userId") String userId, @Param("poemId") Integer poemId);

    void insertToUserDislikePoem(@Param("userId") String userId, @Param("poemId") Integer poemId);

    void insertToUserFavoritesPoem(@Param("userId") String userId, @Param("poemId") Integer poemId);

    void deleteFromUserlikePoem(@Param("userId") String userId,@Param("poemId") Integer poemId);

    void deleteFromUserDislikePoem(@Param("userId") String userId,@Param("poemId") Integer poemId);

    void deleteFromUserFavoritesPoem(@Param("userId") String userId,@Param("poemId") Integer poemId);

    UserLikePoemPO selectFromUserLikePoem(@Param("userId") String userId,@Param("poemId") Integer poemId);

    UserDislikePoemPO selectFromUserDisikePoem(String userId, Integer integer);
}
