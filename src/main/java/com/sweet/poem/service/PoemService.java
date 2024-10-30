package com.sweet.poem.service;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.poem.protocol.query.PoemsQuery;
import com.sweet.poem.protocol.dto.PoemDto;
import com.sweet.poem.protocol.vo.*;

import java.util.List;

/**
 * @author shuheng
 */
public interface PoemService {
    Result<List<PoemDto>> getPagePoems(PoemsQuery poemsQuery);

    Result<List<String>> getSuggestions(String prefix);

    Result<PoemLikeVO> likePoem(Integer poemId);

    Result<PoemDislikeVO> dislikePoem(Integer poemId);

    Result<PoemFavoriteVO> favoritePoem(Integer poemId);

    Result<PoemIsInitVO> isLikeDislikeFavorite(Integer poemId);

    Result<PoemCountInitVO> likeDislikeFavoriteCount(Integer poemId);
}
