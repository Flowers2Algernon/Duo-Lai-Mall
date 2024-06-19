package com.cskaoyan.mall.search.converter;

import com.cskaoyan.mall.search.dto.GoodsDTO;
import com.cskaoyan.mall.search.dto.SearchAttrDTO;
import com.cskaoyan.mall.search.model.Goods;
import com.cskaoyan.mall.search.model.SearchAttr;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T22:08:17+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class GoodsConverterImpl implements GoodsConverter {

    @Override
    public GoodsDTO goodsPO2DTO(Goods goods) {
        if ( goods == null ) {
            return null;
        }

        GoodsDTO goodsDTO = new GoodsDTO();

        goodsDTO.setId( goods.getId() );
        goodsDTO.setDefaultImg( goods.getDefaultImg() );
        goodsDTO.setTitle( goods.getTitle() );
        goodsDTO.setPrice( goods.getPrice() );
        goodsDTO.setTmId( goods.getTmId() );
        goodsDTO.setTmName( goods.getTmName() );
        goodsDTO.setTmLogoUrl( goods.getTmLogoUrl() );
        goodsDTO.setFirstLevelCategoryId( goods.getFirstLevelCategoryId() );
        goodsDTO.setFirstLevelCategoryName( goods.getFirstLevelCategoryName() );
        goodsDTO.setSecondLevelCategoryId( goods.getSecondLevelCategoryId() );
        goodsDTO.setSecondLevelCategoryName( goods.getSecondLevelCategoryName() );
        goodsDTO.setThirdLevelCategoryId( goods.getThirdLevelCategoryId() );
        goodsDTO.setThirdLevelCategoryName( goods.getThirdLevelCategoryName() );
        goodsDTO.setHotScore( goods.getHotScore() );
        goodsDTO.setAttrs( searchAttrListToSearchAttrDTOList( goods.getAttrs() ) );

        return goodsDTO;
    }

    @Override
    public List<GoodsDTO> goodsPOs2DTOs(List<Goods> goods) {
        if ( goods == null ) {
            return null;
        }

        List<GoodsDTO> list = new ArrayList<GoodsDTO>( goods.size() );
        for ( Goods goods1 : goods ) {
            list.add( goodsPO2DTO( goods1 ) );
        }

        return list;
    }

    @Override
    public SearchAttrDTO searchAttrPO2DTO(SearchAttr searchAttr) {
        if ( searchAttr == null ) {
            return null;
        }

        SearchAttrDTO searchAttrDTO = new SearchAttrDTO();

        searchAttrDTO.setAttrId( searchAttr.getAttrId() );
        searchAttrDTO.setAttrValue( searchAttr.getAttrValue() );
        searchAttrDTO.setAttrName( searchAttr.getAttrName() );

        return searchAttrDTO;
    }

    protected List<SearchAttrDTO> searchAttrListToSearchAttrDTOList(List<SearchAttr> list) {
        if ( list == null ) {
            return null;
        }

        List<SearchAttrDTO> list1 = new ArrayList<SearchAttrDTO>( list.size() );
        for ( SearchAttr searchAttr : list ) {
            list1.add( searchAttrPO2DTO( searchAttr ) );
        }

        return list1;
    }
}
