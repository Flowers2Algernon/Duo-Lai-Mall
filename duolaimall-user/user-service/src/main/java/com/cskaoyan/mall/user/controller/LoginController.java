package com.cskaoyan.mall.user.controller;

import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.IpUtil;
import com.cskaoyan.mall.user.consts.UserCodeEnum;
import com.cskaoyan.mall.user.consts.UserConstants;
import com.cskaoyan.mall.user.dto.UserLoginDTO;
import com.cskaoyan.mall.user.dto.UserLoginInfoDTO;
import com.cskaoyan.mall.user.query.UserInfoParam;
import com.cskaoyan.mall.user.service.UserService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 登录
     */
    @PostMapping("login")
    public Result<UserLoginDTO> login(@RequestBody UserInfoParam userInfo, HttpServletRequest request) {
        //此处的userInfo是用户传递过来的请求体中的参数
        //首先生成一个随机字符串当作token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //从当前请求中获取ip
        String ipAddress = IpUtil.getIpAddress(request);
        //调用service层方法
        UserLoginDTO userLoginDTO = userService.login(userInfo, ipAddress, token);
        if (userLoginDTO!=null){
            //登录成功
            return Result.ok(userLoginDTO);
        }
        return Result.build(null,UserCodeEnum.USER_LOGIN_CHECK_FAIL);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("logout")
    public Result logout(HttpServletRequest request){

        String tokenStr = request.getHeader(UserConstants.USER_LOGIN_TOKEN_HEADER);

        RBucket<UserLoginInfoDTO> bucket = redissonClient.getBucket(UserConstants.USER_LOGIN_KEY_PREFIX + tokenStr);
        bucket.delete();
        return Result.ok();
    }
}
