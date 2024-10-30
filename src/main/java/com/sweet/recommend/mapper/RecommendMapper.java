package com.sweet.recommend.mapper;

import com.sweet.recommend.protocol.dto.UserLikeTopThreeAuthor;
import com.sweet.recommend.protocol.dto.UserLikeTopThreeForm;
import com.sweet.recommend.protocol.dto.UserLikeTopThreeType;
import com.sweet.recommend.protocol.vo.CommonRecommendPoemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shuheng
 */
@Mapper
public interface RecommendMapper {
    List<CommonRecommendPoemsVO> getTenRecommendPoems();

    List<UserLikeTopThreeAuthor> getUserLikeTopThreeAuthor(String userId);

    List<UserLikeTopThreeType> getUserLikeTopThreeType(String userId);

    List<UserLikeTopThreeForm> getUserLikeTopThreeForm(String userId);
}

