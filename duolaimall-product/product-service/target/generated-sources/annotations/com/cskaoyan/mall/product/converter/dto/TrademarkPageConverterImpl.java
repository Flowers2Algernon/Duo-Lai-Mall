package com.cskaoyan.mall.product.converter.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cskaoyan.mall.product.dto.TrademarkPageDTO;
import com.cskaoyan.mall.product.model.Trademark;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-17T14:49:29+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TrademarkPageConverterImpl implements TrademarkPageConverter {

    @Autowired
    private TrademarkConverter trademarkConverter;

    @Override
    public TrademarkPageDTO tradeMarkPagePO2PageDTO(IPage<Trademark> trademarkPage) {
        if ( trademarkPage == null ) {
            return null;
        }

        TrademarkPageDTO trademarkPageDTO = new TrademarkPageDTO();

        trademarkPageDTO.setRecords( trademarkConverter.trademarkPOs2DTOs( trademarkPage.getRecords() ) );
        trademarkPageDTO.setTotal( (int) trademarkPage.getTotal() );

        return trademarkPageDTO;
    }
}
