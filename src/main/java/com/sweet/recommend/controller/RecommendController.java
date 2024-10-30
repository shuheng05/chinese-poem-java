package com.sweet.recommend.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sweet.common.unifyReturnObject.Result;
import com.sweet.common.utils.JwtUtil;
import com.sweet.common.utils.UserIdUtil;
import com.sweet.recommend.protocol.query.RecommendPageQuery;
import com.sweet.recommend.protocol.vo.CommonRecommendPoemsVO;
import com.sweet.recommend.protocol.vo.UserRecommendPoemsVO;
import com.sweet.recommend.service.RecommendService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shuheng
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Resource
    private RecommendService recommendService;

    /**
     * 千人共面 推荐
     * 获取10首最高评分的古诗
     */
    @GetMapping("/getCommonRecommend")
    public Result<List<CommonRecommendPoemsVO>> getCommonRecommend(){
        return recommendService.getTenRecommendPoems();
    }

    @PostMapping("/getUserRecommend")
    public Result<List<UserRecommendPoemsVO>> getUserRecommend(HttpServletRequest request, @RequestBody RecommendPageQuery recommendPageQuery){
        Integer page = recommendPageQuery.getPage();
        Integer limit = recommendPageQuery.getLimit();
        if (page == null || page < 1 || limit ==null || limit < 1) {
            return Result.error("page or limit is illegal\n" + page + "or" + limit );
        }

        // 令牌建议是放在请求头中，获取请求头中令牌
        String token = request.getHeader("token");
        System.out.println(token);
        if (token == null) {
            return recommendService.getUserRecommendPoems(page, limit, false);
        }

        try {
            // 解析令牌获取用户id
            DecodedJWT tokenInfo = JwtUtil.getTokenInfo(token);
            String userId = tokenInfo.getClaim("id").toString();
            System.out.println("用户id" + userId);
            UserIdUtil.setCurrentUserId(userId);
            // 验证令牌
            JwtUtil.verify(token);

        } catch (Exception e) {
            e.printStackTrace();
            return recommendService.getUserRecommendPoems(page, limit, false);
        }

        return recommendService.getUserRecommendPoems(page, limit, true);
    }
}
