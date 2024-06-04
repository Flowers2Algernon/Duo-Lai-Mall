package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.PlatformAttributeInfoConverter;
import com.cskaoyan.mall.product.converter.param.PlatformAttributeInfoParamConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.PlatformAttributeValueDTO;
import com.cskaoyan.mall.product.mapper.PlatformAttrInfoMapper;
import com.cskaoyan.mall.product.mapper.PlatformAttrValueMapper;
import com.cskaoyan.mall.product.model.PlatformAttributeInfo;
import com.cskaoyan.mall.product.model.PlatformAttributeValue;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Service
public class PlatformAttributeServiceImpl implements PlatformAttributeService {
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    PlatformAttrValueMapper platformAttrValueMapper;
    @Autowired
    PlatformAttributeInfoConverter platformAttributeInfoConverter;
    @Autowired
    PlatformAttributeInfoParamConverter platformAttributeInfoParamConverter;
    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoList(Long firstLevelCategoryId, Long secondLevelCategoryId, Long thirdLevelCategoryId) {
        //分三种情况，*/0/0, */*/0, */*/*
        List<PlatformAttributeInfo> platformAttributeInfos = platformAttrInfoMapper.selectPlatFormAttrInfoList(firstLevelCategoryId, secondLevelCategoryId, thirdLevelCategoryId);
        List<PlatformAttributeInfoDTO> platformAttributeInfoDTOS = platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttributeInfos);
        return platformAttributeInfoDTOS;

    }

    @Override
    public void savePlatformAttrInfo(PlatformAttributeParam platformAttributeParam) {
        PlatformAttributeInfo platformAttributeInfo = platformAttributeInfoParamConverter.attributeInfoParam2Info(platformAttributeParam);
        if (platformAttributeInfo.getId()!=null){
            //修改
            platformAttrInfoMapper.updateById(platformAttributeInfo);
        }else {
            //新增
            platformAttrInfoMapper.insert(platformAttributeInfo);
        }
        //此时是可以得到新增时platformAttributeInfo的id值的
//        System.out.println(platformAttributeInfo.getId());
        //platformAttrValue采用先删除后新增的方式
        LambdaQueryWrapper<PlatformAttributeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlatformAttributeValue::getAttrId,platformAttributeInfo.getId());
        int delete = platformAttrValueMapper.delete(lambdaQueryWrapper);

        List<PlatformAttributeValue> attrValueList = platformAttributeInfo.getAttrValueList();
        if (!CollectionUtils.isEmpty(attrValueList)){
            //循环遍历
            for (PlatformAttributeValue valueParam : attrValueList) {
                //赋值id
                valueParam.setId(null);
                valueParam.setAttrId(platformAttributeInfo.getId());
                //插入
                platformAttrValueMapper.insert(valueParam);
            }
        }


    }

    @Override
    public List<PlatformAttributeValueDTO> getPlatformAttrInfo(Long attrId) {
        QueryWrapper<PlatformAttributeValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",attrId);
        List<PlatformAttributeValue> platformAttributeValues = platformAttrValueMapper.selectList(wrapper);
        List<PlatformAttributeValueDTO> platformAttributeValueDTOS = platformAttributeInfoConverter.platformAttributeValuePO2DTOs(platformAttributeValues);
        return platformAttributeValueDTOS;
    }
}
