package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.SaleAttributeInfoDTO;
import com.cskaoyan.mall.product.model.SaleAttributeInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-17T14:49:29+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SaleAttributeInfoConverterImpl implements SaleAttributeInfoConverter {

    @Override
    public SaleAttributeInfoDTO saleAttributeInfoPO2DTO(SaleAttributeInfo saleAttributeInfo) {
        if ( saleAttributeInfo == null ) {
            return null;
        }

        SaleAttributeInfoDTO saleAttributeInfoDTO = new SaleAttributeInfoDTO();

        saleAttributeInfoDTO.setId( saleAttributeInfo.getId() );
        saleAttributeInfoDTO.setName( saleAttributeInfo.getName() );

        return saleAttributeInfoDTO;
    }

    @Override
    public List<SaleAttributeInfoDTO> saleAttributeInfoPOs2DTOs(List<SaleAttributeInfo> saleAttributeInfos) {
        if ( saleAttributeInfos == null ) {
            return null;
        }

        List<SaleAttributeInfoDTO> list = new ArrayList<SaleAttributeInfoDTO>( saleAttributeInfos.size() );
        for ( SaleAttributeInfo saleAttributeInfo : saleAttributeInfos ) {
            list.add( saleAttributeInfoPO2DTO( saleAttributeInfo ) );
        }

        return list;
    }
}
