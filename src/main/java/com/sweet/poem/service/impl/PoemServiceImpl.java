package com.sweet.poem.service.impl;

import com.alibaba.fastjson.JSON;
import com.sweet.common.unifyReturnObject.Result;
import com.sweet.poem.mapper.PoemMapper;
import com.sweet.poem.protocol.vo.*;
import com.sweet.poem.service.PoemService;
import com.sweet.poem.protocol.query.PoemsQuery;
import com.sweet.poem.protocol.dto.PoemDto;
import com.sweet.common.utils.UserIdUtil;
import jakarta.annotation.Resource;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuheng
 */
@Service
public class PoemServiceImpl implements PoemService {
    @Resource
    private PoemMapper poemMapper;


    @Resource
    private RestHighLevelClient client;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * 搜索古诗
     */
    @Override
    public Result<List<PoemDto>> getPagePoems(PoemsQuery poemsQuery) {
        String content = poemsQuery.getContent();
        Integer page = poemsQuery.getPage();
        Integer limit = poemsQuery.getLimit();
        String author = poemsQuery.getAuthor();
        String dynasty = poemsQuery.getDynasty();
        String form = poemsQuery.getForm();
        String type = poemsQuery.getType();

        SearchRequest request = new SearchRequest("poem");
        TermQueryBuilder termQueryBuilder = null;
        MatchQueryBuilder matchQueryBuilder = null;
        if (content != null && !content.isEmpty()) {
            matchQueryBuilder = QueryBuilders.matchQuery("all", content);
        }
        if (author != null && !author.isEmpty()) {
            termQueryBuilder = QueryBuilders.termQuery("author", author);
        } else if (dynasty != null && !dynasty.isEmpty()) {
            termQueryBuilder = QueryBuilders.termQuery("dynasty", dynasty);
        } else if (form != null && !form.isEmpty()) {
            termQueryBuilder = QueryBuilders.termQuery("form", form);
        } else if (type != null && !type.isEmpty()) {
            termQueryBuilder = QueryBuilders.termQuery("type", type);
        }


        if (matchQueryBuilder == null && termQueryBuilder == null) {
            request.source().query(QueryBuilders.matchAllQuery());
        } else if (matchQueryBuilder != null && termQueryBuilder != null) {
            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
            boolQuery.must(matchQueryBuilder);
            boolQuery.filter(termQueryBuilder);
            request.source().query(boolQuery);
        } else if (matchQueryBuilder != null) {
            request.source().query(matchQueryBuilder);
        } else {
            request.source().query(termQueryBuilder);
        }


        request.source().from((page - 1) * limit).size(limit);

        //List<PoemDto> poemDtos = elasticsearchUtil.matchQuery(content, (poemsQuery.getPage() - 1), poemsQuery.getLimit(), PoemDto.class);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // 解析结果
            SearchHits hits = response.getHits();
            SearchHit[] poems = hits.getHits();
            List<PoemDto> poemDtos = new ArrayList<>();
            for (SearchHit poem : poems) {
                String poemString = poem.getSourceAsString();
                PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
                poemDtos.add(poemDto);
            }
            return Result.success("古诗数据搜索", poemDtos);
        } catch (IOException e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 获取自动补全
     */
    @Override
    public Result<List<String>> getSuggestions(String prefix) {
        SearchRequest request = new SearchRequest("poem");
        request.source().suggest(new SuggestBuilder().addSuggestion(
                "mySuggestion",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix(prefix)
                        .skipDuplicates(true)
                        .size(10)
        ));
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Suggest suggest = response.getSuggest();
            CompletionSuggestion completionSuggestion = suggest.getSuggestion("mySuggestion");
            List<String> suggestions = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option : completionSuggestion.getOptions()) {
                String sugggestion = option.getText().string();
                suggestions.add(sugggestion);
            }
            return Result.success("自动补全", suggestions);
        } catch (IOException e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 点赞 / 取消点赞
     */
    @Override
    public Result<PoemLikeVO> likePoem(Integer poemId) {
        System.out.println(poemId);
        String currentUserId = UserIdUtil.getCurrentUserId();
        System.out.println("当前登录用户id:" + currentUserId);
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        String likePoemIdCountKey = "like" + ":" + poemId + ":" + "count";
        String likeUserIdPoemIdKey = "like" + ":" + currentUserId + ":" + poemId;
        String dislikePoemIdCountKey = "dislike" + ":" + poemId + ":" + "count";
        String dislikeUserIdPoemIdKey = "dislike" + ":" + currentUserId + ":" + poemId;

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        Integer isLike = ops.get(likeUserIdPoemIdKey);
        Integer isDisLike = ops.get(dislikeUserIdPoemIdKey);
        // 第一次点赞 / 偶数次点赞
        if ((isLike == null) || isLike == 0) {
            ops.increment(likePoemIdCountKey, 1);
            ops.set(likeUserIdPoemIdKey, 1);
            if (isDisLike != null && isDisLike == 1) {
                ops.decrement(dislikePoemIdCountKey, 1);
                ops.set(dislikeUserIdPoemIdKey, 0);
            }
        } else if (isLike == 1) {  // 取消点赞
            ops.decrement(likePoemIdCountKey, 1);
            ops.set(likeUserIdPoemIdKey, 0);
        }
        Integer likeCountVO = ops.get(likePoemIdCountKey);
        Integer isLikeVO = ops.get(likeUserIdPoemIdKey);
        Integer dislikeCountVO = ops.get(dislikePoemIdCountKey);
        Integer isDisLikeVO = ops.get(dislikeUserIdPoemIdKey);
        PoemLikeVO poemLikeVO = new PoemLikeVO(
                likeCountVO == null ? 0 : likeCountVO,
                isLikeVO == null ? 0 : isLikeVO,
                dislikeCountVO == null ? 0 : dislikeCountVO,
                isDisLikeVO == null ? 0 : isDisLikeVO
        );
        return Result.success((isLikeVO != null && isLikeVO == 1) ? "点赞成功" : "取消点赞", poemLikeVO);
    }

    @Override
    public Result<PoemDislikeVO> dislikePoem(Integer poemId) {
        System.out.println(poemId);
        String currentUserId = UserIdUtil.getCurrentUserId();
        System.out.println("当前登录用户id:" + currentUserId);
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        String likePoemIdCountKey = "like" + ":" + poemId + ":" + "count";
        String likeUserIdPoemIdKey = "like" + ":" + currentUserId + ":" + poemId;
        String dislikePoemIdCountKey = "dislike" + ":" + poemId + ":" + "count";
        String dislikeUserIdPoemIdKey = "dislike" + ":" + currentUserId + ":" + poemId;

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        Integer isLike = ops.get(likeUserIdPoemIdKey);
        Integer isDisLike = ops.get(dislikeUserIdPoemIdKey);
        // 第一次踩 / 偶数次踩
        if ((isDisLike == null) || isDisLike == 0) {
            ops.increment(dislikePoemIdCountKey, 1);
            ops.set(dislikeUserIdPoemIdKey, 1);
            if (isLike != null && isLike == 1) {
                ops.decrement(likePoemIdCountKey, 1);
                ops.set(likeUserIdPoemIdKey, 0);
            }
        } else if (isDisLike == 1) {  // 取消踩
            ops.decrement(dislikePoemIdCountKey, 1);
            ops.set(dislikeUserIdPoemIdKey, 0);
        }


        Integer likeCountVO = ops.get(likePoemIdCountKey);
        Integer isLikeVO = ops.get(likeUserIdPoemIdKey);
        Integer dislikeCountVO = ops.get(dislikePoemIdCountKey);
        Integer isDisLikeVO = ops.get(dislikeUserIdPoemIdKey);
        PoemDislikeVO poemDislikeVO = new PoemDislikeVO(
                likeCountVO == null ? 0 : likeCountVO,
                isLikeVO == null ? 0 : isLikeVO,
                dislikeCountVO == null ? 0 : dislikeCountVO,
                isDisLikeVO == null ? 0 : isDisLikeVO
        );
        return Result.success((isDisLikeVO != null && isDisLikeVO == 1) ? "不喜欢" : "喜欢", poemDislikeVO);
    }

    @Override
    public Result<PoemFavoriteVO> favoritePoem(Integer poemId) {
        System.out.println("古诗id"+poemId);
        String currentUserId = UserIdUtil.getCurrentUserId();
        System.out.println("当前登录用户id:" + currentUserId);
        if (currentUserId == null) {
            return Result.error("请先登录");
        }
        String favoritePoemIdCountKey = "favorite" + ":" + poemId + ":" + "count";
        String favoriteUserIdPoemIdKey = "favorite" + ":" + currentUserId + ":" + poemId;

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
        Integer isFavorite = ops.get(favoriteUserIdPoemIdKey);
        // 第一次收藏 / 偶数次收藏
        if ((isFavorite == null) || isFavorite == 0) {
            ops.increment(favoritePoemIdCountKey, 1);
            ops.set(favoriteUserIdPoemIdKey, 1);
        } else if (isFavorite == 1) {  // 取消收藏
            ops.decrement(favoritePoemIdCountKey, 1);
            ops.set(favoriteUserIdPoemIdKey, 0);
        }
        Integer favoriteCountVO = ops.get(favoritePoemIdCountKey);
        Integer isFavoriteVO = ops.get(favoriteUserIdPoemIdKey);
        PoemFavoriteVO poemFavoriteVO = new PoemFavoriteVO(
                favoriteCountVO == null ? 0 : favoriteCountVO,
                isFavoriteVO == null ? 0 : isFavoriteVO
        );

        // TODO rabbitmq 消息队列
        System.out.println("isfavorite:"+ isFavoriteVO);
        if (isFavoriteVO != null && isFavoriteVO == 1) {
            poemMapper.insertToUserFavoritesPoem(currentUserId, poemId);
        } else if (isFavoriteVO != null && isFavoriteVO == 0) {
            poemMapper.deleteFromUserFavoritesPoem(currentUserId, poemId);
        }

        return Result.success((isFavoriteVO != null && isFavoriteVO == 1) ? "收藏成功" : "取消收藏", poemFavoriteVO);
    }

    @Override
    public Result<PoemIsInitVO> isLikeDislikeFavorite(Integer poemId) {
        String currentUserId = UserIdUtil.getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }
        String likeUserIdPoemIdKey = "like" + ":" + currentUserId + ":" + poemId;
        String dislikeUserIdPoemIdKey = "dislike" + ":" + currentUserId + ":" + poemId;
        String favoriteUserIdPoemIdKey = "favorite" + ":" + currentUserId + ":" + poemId;

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
        Integer isLike = ops.get(likeUserIdPoemIdKey);
        Integer isDisLike = ops.get(dislikeUserIdPoemIdKey);
        Integer isFavorite = ops.get(favoriteUserIdPoemIdKey);

        PoemIsInitVO poemIsInitVO = new PoemIsInitVO(
                isLike == null ? 0 : isLike,
                isDisLike == null ? 0 : isDisLike,
                isFavorite == null ? 0 : isFavorite
        );
        return Result.success("古诗初始是否被该用户点赞踩收藏初始数据", poemIsInitVO);
    }

    @Override
    public Result<PoemCountInitVO> likeDislikeFavoriteCount(Integer poemId) {
        String likePoemIdCountKey = "like" + ":" + poemId + ":" + "count";
        String dislikePoemIdCountKey = "dislike" + ":" + poemId + ":" + "count";
        String favoritePoemIdCountKey = "favorite" + ":" + poemId + ":" + "count";

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
        Integer likeCount = ops.get(likePoemIdCountKey);
        Integer dislikeCount = ops.get(dislikePoemIdCountKey);
        Integer favoriteCount = ops.get(favoritePoemIdCountKey);

        PoemCountInitVO poemCountInitVO = new PoemCountInitVO(
                likeCount == null ? 0 : likeCount,
                dislikeCount == null ? 0 : dislikeCount,
                favoriteCount == null ? 0 : favoriteCount
        );

        return Result.success("古诗初始点赞踩收藏数量", poemCountInitVO);
    }
}


