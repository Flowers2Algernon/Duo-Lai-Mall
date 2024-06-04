package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.TrademarkConverter;
import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.dto.TrademarkPageDTO;
import com.cskaoyan.mall.product.mapper.TrademarkMapper;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.TrademarkParam;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrademarkServiceImpl implements TrademarkService {
    @Autowired
    TrademarkMapper trademarkMapper;
    @Autowired
    TrademarkConverter trademarkConverter;

    @Override
    public TrademarkDTO getTrademarkByTmId(Long tmId) {
        QueryWrapper<Trademark> wrapper = new QueryWrapper<>();
        wrapper.eq("id",tmId);
        Trademark trademark = trademarkMapper.selectOne(wrapper);
        TrademarkDTO trademarkDTO = trademarkConverter.trademarkPO2DTO(trademark);
        return trademarkDTO;
    }

    @Override
    public TrademarkPageDTO getPage(Page<Trademark> pageParam) {
        List<Trademark> trademarks = trademarkMapper.selectList(null);
        Page<Trademark> trademarkPage = trademarkMapper.selectPage(pageParam, null);
        //总条数的获取也可以使用如下方式
//        trademarkPage.getTotal();
        List<Trademark> records = trademarkPage.getRecords();
        List<TrademarkDTO> trademarkDTOS = trademarkConverter.trademarkPOs2DTOs(records);
        //返回参数
        TrademarkPageDTO trademarkPageDTO = new TrademarkPageDTO();
        trademarkPageDTO.setTotal(trademarks.size());
        trademarkPageDTO.setRecords(trademarkDTOS);
        return trademarkPageDTO;
    }

    @Override
    public void save(TrademarkParam trademarkParam) {
        Trademark trademark = trademarkConverter.trademarkParam2Trademark(trademarkParam);
        trademarkMapper.insert(trademark);
    }

    @Override
    public void updateById(TrademarkParam trademarkParam) {
        Trademark trademark = trademarkConverter.trademarkParam2Trademark(trademarkParam);
        trademarkMapper.updateById(trademark);
    }

    @Override
    public void removeById(Long id) {
        QueryWrapper<Trademark> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        trademarkMapper.delete(wrapper);
    }
}
