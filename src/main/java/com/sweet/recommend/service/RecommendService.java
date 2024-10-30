package com.sweet.recommend.service;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.recommend.protocol.vo.CommonRecommendPoemsVO;
import com.sweet.recommend.protocol.vo.UserRecommendPoemsVO;

import java.util.List;

/**
 * @author shuheng
 */
public interface RecommendService {
    Result<List<CommonRecommendPoemsVO>> getTenRecommendPoems();

    Result<List<UserRecommendPoemsVO>> getUserRecommendPoems(Integer page, Integer limit, boolean isLogin);
}
