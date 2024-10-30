package com.sweet.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.sweet.common.unifyReturnObject.Result;
import com.sweet.common.utils.UserIdUtil;
import com.sweet.recommend.mapper.RecommendMapper;
import com.sweet.recommend.protocol.dto.UserLikeTopThreeAuthor;
import com.sweet.recommend.protocol.dto.UserLikeTopThreeForm;
import com.sweet.recommend.protocol.dto.UserLikeTopThreeType;
import com.sweet.recommend.protocol.vo.CommonRecommendPoemsVO;
import com.sweet.recommend.protocol.vo.UserRecommendPoemsVO;
import com.sweet.recommend.service.RecommendService;
import jakarta.annotation.Resource;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuheng
 */
@Service
public class RecommendServiceImpl implements RecommendService {
    @Resource
    private RecommendMapper recommendMapper;

    @Resource
    private RestHighLevelClient client;

    @Override
    public Result<List<CommonRecommendPoemsVO>> getTenRecommendPoems() {
        List<CommonRecommendPoemsVO> commonRecommendPoemvos = recommendMapper.getTenRecommendPoems();
        return Result.success("首最高评分古诗", commonRecommendPoemvos);
    }

    @Override
    public Result<List<UserRecommendPoemsVO>> getUserRecommendPoems(Integer page, Integer limit, boolean isLogin) {
        if (isLogin) {
            String currentUserId = UserIdUtil.getCurrentUserId();
            currentUserId = currentUserId.substring(1, currentUserId.length() - 1);
            List<UserLikeTopThreeAuthor> userLikeTopThreeAuthor = recommendMapper.getUserLikeTopThreeAuthor(currentUserId);
            List<UserLikeTopThreeType> userLikeTopThreeType = recommendMapper.getUserLikeTopThreeType(currentUserId);
            List<UserLikeTopThreeForm> userLikeTopThreeForm = recommendMapper.getUserLikeTopThreeForm(currentUserId);

            SearchRequest request = new SearchRequest("poem");
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctions = new ArrayList<>();


            float totalAuthorCount = 0.0f;

            for (UserLikeTopThreeAuthor author : userLikeTopThreeAuthor) {
                totalAuthorCount += author.getAuthorCount();
            }

            List<Float> authorWeights = new ArrayList<>();
            for (UserLikeTopThreeAuthor author : userLikeTopThreeAuthor) {
                authorWeights.add(author.getAuthorCount() / totalAuthorCount);
            }
            // 为作者偏好添加权重
            for (int i = 0; i < userLikeTopThreeAuthor.size() && i < authorWeights.size(); i++) {
                filterFunctions.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("author", userLikeTopThreeAuthor.get(i).getAuthorName()),
                        ScoreFunctionBuilders.weightFactorFunction(authorWeights.get(i))
                ));
            }



            // 为类型偏好添加权重
            float totalTypeCount = 0.0f;

            for (UserLikeTopThreeType type : userLikeTopThreeType) {
                totalTypeCount += type.getTypeCount();
            }

            List<Float> typeWeights = new ArrayList<>();
            for (UserLikeTopThreeType type : userLikeTopThreeType) {
                typeWeights.add(type.getTypeCount() / totalTypeCount);
            }
            for (int i = 0; i < userLikeTopThreeType.size() && i < typeWeights.size(); i++) {
                filterFunctions.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("type", userLikeTopThreeType.get(i).getPoemType()),
                        ScoreFunctionBuilders.weightFactorFunction(typeWeights.get(i))
                ));
            }


            // 为形式偏好添加权重
            float totalFormCount = 0.0f;

            for (UserLikeTopThreeForm form : userLikeTopThreeForm) {
                totalFormCount += form.getFormCount();
            }

            List<Float> formWeights = new ArrayList<>();
            for (UserLikeTopThreeForm form : userLikeTopThreeForm) {
                formWeights.add(form.getFormCount() / totalFormCount);
            }
            for (int i = 0; i < userLikeTopThreeForm.size() && i < formWeights.size(); i++) {
                filterFunctions.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("form", userLikeTopThreeForm.get(i).getPoemForm()),
                        ScoreFunctionBuilders.weightFactorFunction(formWeights.get(i))
                ));
            }


            // 创建 FunctionScoreQueryBuilder
            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(
                    QueryBuilders.matchAllQuery(),
                    filterFunctions.toArray(new FunctionScoreQueryBuilder.FilterFunctionBuilder[0])
            );

            // 设置得分模式为sum，即将所有函数得分相加
            functionScoreQuery.scoreMode(FunctionScoreQuery.ScoreMode.SUM);

            // 设置提升模式为sum，即将基础得分与函数得分相加
            functionScoreQuery.boostMode(CombineFunction.SUM);

            request.source().from((page - 1) * limit).size(limit);
            request.source().query(functionScoreQuery);
            SearchResponse response = null;
            try {
                response = client.search(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 解析结果
            SearchHits hits = response.getHits();
            long total = hits.getTotalHits().value;
            System.out.println(total);
            SearchHit[] poems = hits.getHits();
            List<UserRecommendPoemsVO> userRecommendPoemsvos = new ArrayList<>();
            for (SearchHit poem : poems) {
                String poemString = poem.getSourceAsString();
                UserRecommendPoemsVO userRecommendPoemsVO = JSON.parseObject(poemString, UserRecommendPoemsVO.class);
                userRecommendPoemsvos.add(userRecommendPoemsVO);
            }


            return Result.success("用户推荐古诗", userRecommendPoemsvos);
        }



        SearchRequest request = new SearchRequest("poem");
        request.source().query(QueryBuilders.matchAllQuery());
        request.source().from((page - 1) * limit).size(limit);

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 解析结果
        SearchHits hits = response.getHits();
        SearchHit[] poems = hits.getHits();
        List<UserRecommendPoemsVO> userRecommendPoemsvos = new ArrayList<>();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            UserRecommendPoemsVO userRecommendPoemsVO = JSON.parseObject(poemString, UserRecommendPoemsVO.class);
            userRecommendPoemsvos.add(userRecommendPoemsVO);
        }

        return Result.success("用户未登录，默认推荐古诗", userRecommendPoemsvos);
    }
}
