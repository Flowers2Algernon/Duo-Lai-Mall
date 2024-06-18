package com.cskaoyan.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddressDTO implements Serializable {
    // "用户地址Id"
    Long id;

    // "用户地址"
    private String userAddress;

    // "用户id"
    private Long userId;

    // "收件人"
    private String consignee;

    // "联系方式"
    private String phoneNum;

    // "是否是默认"
    private String isDefault;
}
