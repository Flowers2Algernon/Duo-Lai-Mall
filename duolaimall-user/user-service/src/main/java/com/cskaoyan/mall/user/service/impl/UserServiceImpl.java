package com.cskaoyan.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.user.consts.UserConstants;
import com.cskaoyan.mall.user.dto.UserLoginDTO;
import com.cskaoyan.mall.user.dto.UserLoginInfoDTO;
import com.cskaoyan.mall.user.mapper.UserInfoMapper;
import com.cskaoyan.mall.user.model.UserInfo;
import com.cskaoyan.mall.user.query.UserInfoParam;
import com.cskaoyan.mall.user.service.UserService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

    // 调用mapper 层
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public UserLoginDTO login(UserInfoParam userInfo, String ip, String token) {

       //select * from userInfo where username = ? and passwd = ?
        //密码是MD5密文存储
        String passwd = userInfo.getPasswd();
        String newPasswd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getLoginName,userInfo.getLoginName());
        queryWrapper.eq(UserInfo::getPasswd,newPasswd);
        UserInfo info = userInfoMapper.selectOne(queryWrapper);
        if (info==null){
            //没有查询到
            return null;
        }
        //代码运行到此处，表示info非空，在数据库中找到了
        //向redis中保存用户登录信息
        UserLoginInfoDTO userLoginInfoDTO = new UserLoginInfoDTO();
        //登录用户Id
        userLoginInfoDTO.setUserId(info.getId().toString());
        //登录用户的ip
        userLoginInfoDTO.setIp(ip);
        //获取存储登录会话的桶
        RBucket<Object> bucket = redissonClient.getBucket(UserConstants.USER_LOGIN_KEY_PREFIX + token);
        //向桶中存储用户id和ip地址，相当于保存登录会话
        bucket.set(userLoginInfoDTO,UserConstants.USERKEY_TIMEOUT,TimeUnit.SECONDS);
        return new UserLoginDTO(info.getNickName(),token);
    }
}
