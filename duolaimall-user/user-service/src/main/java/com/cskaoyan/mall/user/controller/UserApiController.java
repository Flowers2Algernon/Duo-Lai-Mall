package com.cskaoyan.mall.user.controller;

import com.cskaoyan.mall.user.converter.UserAddressConverter;
import com.cskaoyan.mall.user.dto.UserAddressDTO;
import com.cskaoyan.mall.user.model.UserAddress;
import com.cskaoyan.mall.user.service.UserAddressService;
import com.cskaoyan.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserApiController {
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    UserAddressConverter userAddressConverter;

    @GetMapping("api/user/inner/findUserAddressListByUserId/{userId}")
    public List<UserAddressDTO> findAddressByUserId(@PathVariable("userId") String userId){
        //根据userId获取对应的用户地址
        List<UserAddress> userAddressListByUserId = userAddressService.findUserAddressListByUserId(userId);
        return userAddressConverter.userAddressPOs2DTOs(userAddressListByUserId);
    }
}
