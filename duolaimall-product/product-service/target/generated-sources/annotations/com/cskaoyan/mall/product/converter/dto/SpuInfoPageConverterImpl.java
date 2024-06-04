package com.cskaoyan.mall.product.converter.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.dto.SpuInfoDTO;
import com.cskaoyan.mall.product.dto.SpuInfoPageDTO;
import com.cskaoyan.mall.product.model.SpuInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T16:24:12+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SpuInfoPageConverterImpl implements SpuInfoPageConverter {

    @Autowired
    private SpuInfoConverter spuInfoConverter;

    @Override
    public SpuInfoPageDTO spuInfoPage2PageDTO(Page<SpuInfo> SpuInfoPage) {
        if ( SpuInfoPage == null ) {
            return null;
        }

        SpuInfoPageDTO spuInfoPageDTO = new SpuInfoPageDTO();

        spuInfoPageDTO.setRecords( spuInfoListToSpuInfoDTOList( SpuInfoPage.getRecords() ) );
        spuInfoPageDTO.setTotal( (int) SpuInfoPage.getTotal() );

        return spuInfoPageDTO;
    }

    protected List<SpuInfoDTO> spuInfoListToSpuInfoDTOList(List<SpuInfo> list) {
        if ( list == null ) {
            return null;
        }

        List<SpuInfoDTO> list1 = new ArrayList<SpuInfoDTO>( list.size() );
        for ( SpuInfo spuInfo : list ) {
            list1.add( spuInfoConverter.spuInfoPO2DTO( spuInfo ) );
        }

        return list1;
    }
}
