package com.sweet.user.controller;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.user.protocol.param.UserLogInParam;
import com.sweet.user.protocol.param.UserSignInParam;
import com.sweet.user.protocol.query.UserFavoritePoemsQuery;
import com.sweet.user.protocol.vo.UserFavoritePoemVO;
import com.sweet.user.protocol.vo.UserInfoVO;
import com.sweet.user.protocol.vo.UserLogInVO;
import com.sweet.user.protocol.vo.UserSignInVO;
import com.sweet.user.service.UserService;
import com.sweet.common.utils.MailUtil;
import com.sweet.common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shuheng
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 用户登录
     */
    @PostMapping("/logIn")
    public Result<UserLogInVO> logIn(@RequestBody UserLogInParam userLogInParam) {
        return userService.login(userLogInParam);
    }

    /**
     * 用户注册
     */
    @PostMapping("/signIn")
    public Result<UserSignInVO> signIn(@RequestBody UserSignInParam userSignInParam) {
        return userService.signIn(userSignInParam);
    }

    /**
     * 邮箱注册 发验证码
     */
    @GetMapping("/getCode")
    public Result<String> getCode(@RequestParam String email) {
        if (!VerifyUtil.isEmail(email)) {
            return Result.error("邮箱格式错误");
        }
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String code = ops.get(email);
        if (code != null) {
            return Result.error("验证码重复发送");
        }
        MailUtil mailUtil = new MailUtil(javaMailSender, from);
        String newCode = mailUtil.sendCode(email);
        // 一分钟
        ops.set(email, newCode, 60, TimeUnit.SECONDS);
        return Result.success("验证码发送成功", newCode);
    }

    @PostMapping("/info")
    public Result<UserInfoVO> info() {
        return userService.getUserInfo();
    }

    @PostMapping("/getFavoritePoems")
    public Result<List<UserFavoritePoemVO>> getFavoritePoems(@RequestBody UserFavoritePoemsQuery query){
        Integer page = query.getPage();
        Integer limit = query.getLimit();
        if (page == null || page < 1 || limit ==null || limit < 1) {
            return Result.error("page or limit is illegal\n" + page + "or" + limit );
        }
        return userService.getFavoritePoems(page, limit);
    }
}
