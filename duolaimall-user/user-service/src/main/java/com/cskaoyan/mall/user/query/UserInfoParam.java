package com.cskaoyan.mall.user.query;

import lombok.Data;

@Data
public class UserInfoParam {

    private Long id;

    // "用户名称"
    private String loginName;

    // "用户昵称"
    private String nickName;

    // "用户密码"
    private String passwd;

    // "用户姓名"
    private String name;

    // "手机号"
    private String phoneNum;

    // "邮箱"
    private String email;

    // "头像"
    private String headImg;

    // "用户级别"
    private String userLevel;
}
