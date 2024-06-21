package com.cskaoyan.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.user.mapper.UserAddressMapper;
import com.cskaoyan.mall.user.mapper.UserInfoMapper;
import com.cskaoyan.mall.user.model.UserAddress;
import com.cskaoyan.mall.user.model.UserInfo;
import com.cskaoyan.mall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
        //此处实现根据用户id查找用户地址的具体逻辑
        QueryWrapper<UserAddress> userAddressQueryWrapper = new QueryWrapper<>();
        userAddressQueryWrapper.eq("user_id",userId);
        List<UserAddress> userAddresses = userAddressMapper.selectList(userAddressQueryWrapper);
        return userAddresses;
    }
}
