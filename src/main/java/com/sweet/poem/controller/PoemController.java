package com.sweet.poem.controller;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.poem.protocol.query.PoemsQuery;
import com.sweet.poem.protocol.dto.PoemDto;
import com.sweet.poem.protocol.vo.*;
import com.sweet.poem.service.PoemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shuheng
 */
@RestController
@RequestMapping("/poems")
public class PoemController {
    @Resource
    private PoemService poemService;

    /**
     * 获取诗词列表
     */
    @PostMapping("/get")
    public Result<List<PoemDto>> getPoems(@RequestBody PoemsQuery poemsQuery) {
        Integer page = poemsQuery.getPage();
        Integer limit = poemsQuery.getLimit();
        if (page == null || page < 1 || limit ==null || limit < 1) {
            return Result.error("page or limit is illegal\n" + page + "or" + limit );
        }

        return poemService.getPagePoems(poemsQuery);
    }

    /**
     * 获取搜索建议
     */
    @GetMapping("/getSuggestions/{prefix}")
    public Result<List<String>> getPoem(@PathVariable String prefix) {
        return poemService.getSuggestions(prefix);
    }

    /**
     * 点赞 / 取消点赞
     */
    @PostMapping("/like")
    public Result<PoemLikeVO> likePoem(@RequestParam Integer poemId) {
        return poemService.likePoem(poemId);
    }

    /**
     * 踩 / 取消踩
     */
    @PostMapping("/dislike")
    public Result<PoemDislikeVO> dislikePoem(@RequestParam Integer poemId) {
        return poemService.dislikePoem(poemId);
    }

    /**
     * 收藏 / 取消收藏
     */
    @PostMapping("/favorite")
    public Result<PoemFavoriteVO> favoritePoem(@RequestParam Integer poemId) {
        return poemService.favoritePoem(poemId);
    }

    /**
     * 判断点赞踩收藏状态
     */
    @PostMapping("/isLikeDislikeFavorite")
    public Result<PoemIsInitVO> isLikeDislikeFavorite(@RequestParam Integer poemId) {
        return poemService.isLikeDislikeFavorite(poemId);
    }

    /**
     * 点赞踩收藏数量
     */
    @PostMapping("/likeDislikeFavoriteCount")
    public Result<PoemCountInitVO> likeDislikeFavoriteCount(@RequestParam Integer poemId) {
        return poemService.likeDislikeFavoriteCount(poemId);
    }

}
