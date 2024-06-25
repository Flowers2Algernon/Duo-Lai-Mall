package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.TrademarkParam;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-25T22:28:58+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TrademarkConverterImpl implements TrademarkConverter {

    @Override
    public TrademarkDTO trademarkPO2DTO(Trademark trademark) {
        if ( trademark == null ) {
            return null;
        }

        TrademarkDTO trademarkDTO = new TrademarkDTO();

        trademarkDTO.setId( trademark.getId() );
        trademarkDTO.setTmName( trademark.getTmName() );
        trademarkDTO.setLogoUrl( trademark.getLogoUrl() );

        return trademarkDTO;
    }

    @Override
    public List<TrademarkDTO> trademarkPOs2DTOs(List<Trademark> trademarks) {
        if ( trademarks == null ) {
            return null;
        }

        List<TrademarkDTO> list = new ArrayList<TrademarkDTO>( trademarks.size() );
        for ( Trademark trademark : trademarks ) {
            list.add( trademarkPO2DTO( trademark ) );
        }

        return list;
    }

    @Override
    public Trademark trademarkParam2Trademark(TrademarkParam trademarkParam) {
        if ( trademarkParam == null ) {
            return null;
        }

        Trademark trademark = new Trademark();

        trademark.setId( trademarkParam.getId() );
        trademark.setTmName( trademarkParam.getTmName() );
        trademark.setLogoUrl( trademarkParam.getLogoUrl() );

        return trademark;
    }
}
