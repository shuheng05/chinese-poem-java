package com.sweet.user.service.impl;

import com.sweet.common.unifyReturnObject.Result;
import com.sweet.user.mapper.UserMapper;
import com.sweet.user.protocol.dto.UserFavoritePoemDTO;
import com.sweet.user.protocol.param.UserLogInParam;
import com.sweet.user.protocol.param.UserSignInParam;
import com.sweet.user.protocol.po.User;
import com.sweet.user.protocol.vo.UserFavoritePoemVO;
import com.sweet.user.protocol.vo.UserInfoVO;
import com.sweet.user.protocol.vo.UserLogInVO;
import com.sweet.user.protocol.vo.UserSignInVO;
import com.sweet.user.service.UserService;
import com.sweet.common.utils.JwtUtil;
import com.sweet.common.utils.UserIdUtil;
import com.sweet.common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author shuheng
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public Result<UserLogInVO> login(UserLogInParam userLogInParam) {
        String email = userLogInParam.getEmail();
        String password = userLogInParam.getPassword();
        if (!VerifyUtil.isEmail(email) || !VerifyUtil.isPassword(password)) {
            return Result.error("邮箱或密码格式不正确");
        }
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return Result.error("密码错误");
        }
        System.out.println("用户id"+user.getId());
        Map<String,String> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username",user.getUsername());
        payload.put("email",user.getEmail());


        // 生成JWT令牌
        String token = JwtUtil.createToken(payload);
        UserLogInVO userLogInVO = new UserLogInVO(user.getId(), user.getUsername(), token);
        return Result.success("登录成功", userLogInVO);
    }

    @Override
    public Result<UserSignInVO> signIn(UserSignInParam userSignInParam) {
        String username = userSignInParam.getUsername();
        String email = userSignInParam.getEmail();
        String password = userSignInParam.getPassword();
        String code = userSignInParam.getCode();
        if (username == null || "".equals(username)) {
            return Result.error("用户名不能为空");
        }
        if (!VerifyUtil.isEmail(email) || !VerifyUtil.isPassword(password)) {
            return Result.error("邮箱或密码格式不正确");
        }

        if (code == null || "".equals(code)) {
            return Result.error("验证码不能为空");
        }

        User userByUsername = userMapper.getUserByUsername(username);
        if (userByUsername != null) {
            return Result.error("用户名已存在");
        }

        if (userMapper.getUserByEmail(email) != null) {
            return Result.error("邮箱已存在");
        }



        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String codeRedis = ops.get(email);
        if (codeRedis == null || "".equals(codeRedis)) {
            return Result.error("验证码已过期");
        }
        if (!code.equals(codeRedis)) {
            return Result.error("验证码错误");
        }


        User user = new User(UUID.randomUUID().toString(),username, email, DigestUtils.md5DigestAsHex(password.getBytes()));
        userMapper.insert(user);
        redisTemplate.delete(email);

        Map<String,String> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username",user.getUsername());
        payload.put("email",user.getEmail());


        // 生成JWT令牌
        String token = JwtUtil.createToken(payload);
        UserSignInVO userSignInVO = new UserSignInVO(user.getId(), user.getUsername(), token);
        return Result.success("注册成功", userSignInVO);
    }

    @Override
    public Result<UserInfoVO> getUserInfo() {
        String currentUserId = UserIdUtil.getCurrentUserId();
        UserInfoVO userInfoVO = userMapper.getUserInfoById(currentUserId);
        String email = userInfoVO.getEmail();
        userInfoVO.setEmail(email.substring(0, email.indexOf('@')));
        return Result.success("用户信息", userInfoVO);
    }

    @Override
    public Result<List<UserFavoritePoemVO>> getFavoritePoems(Integer page, Integer limit) {
        String currentUserId = UserIdUtil.getCurrentUserId();
        List<UserFavoritePoemDTO> userFavoritePoemdtos = userMapper.getFavoritePoemsByUserId(currentUserId, (page - 1) * limit, limit);
        List<UserFavoritePoemVO> userFavoritePoemvos = userFavoritePoemdtos
                .stream()
                .map(userFavoritePoemDTO -> new UserFavoritePoemVO(
                        userFavoritePoemDTO.getPoemId(),
                        userFavoritePoemDTO.getPoemTitle(),
                        userFavoritePoemDTO.getPoemAuthor(),
                        // 截取第一句
                        userFavoritePoemDTO.getPoemContent().split("。")[0]
                )).toList();
        return Result.success("用户收藏古诗", userFavoritePoemvos);
    }
}
