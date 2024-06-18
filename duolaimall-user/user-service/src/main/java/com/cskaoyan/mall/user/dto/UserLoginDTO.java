package com.cskaoyan.mall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
      存储用户登录成功后，返回的前端所需的用户信息
 */
@Data
@AllArgsConstructor
// "登录成功响应"
public class UserLoginDTO {
    // "用户名显示在首页"
    String nickName;
    // "用户登录之后的token"
    String token;
}
