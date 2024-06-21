package com.cskaoyan.mall.promo.converter;

import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.promo.api.dto.SeckillGoodsDTO;
import com.cskaoyan.mall.promo.model.SeckillGoods;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:34:57+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SeckillGoodsConverterImpl implements SeckillGoodsConverter {

    @Override
    public SeckillGoodsDTO convertSeckillGoodsToDTO(SeckillGoods seckillGoods) {
        if ( seckillGoods == null ) {
            return null;
        }

        SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();

        seckillGoodsDTO.setId( seckillGoods.getId() );
        seckillGoodsDTO.setSpuId( seckillGoods.getSpuId() );
        seckillGoodsDTO.setSkuId( seckillGoods.getSkuId() );
        seckillGoodsDTO.setSkuName( seckillGoods.getSkuName() );
        seckillGoodsDTO.setSkuDefaultImg( seckillGoods.getSkuDefaultImg() );
        seckillGoodsDTO.setPrice( seckillGoods.getPrice() );
        seckillGoodsDTO.setCostPrice( seckillGoods.getCostPrice() );
        seckillGoodsDTO.setCheckTime( seckillGoods.getCheckTime() );
        seckillGoodsDTO.setStatus( seckillGoods.getStatus() );
        seckillGoodsDTO.setStartTime( seckillGoods.getStartTime() );
        seckillGoodsDTO.setEndTime( seckillGoods.getEndTime() );
        seckillGoodsDTO.setNum( seckillGoods.getNum() );
        seckillGoodsDTO.setStockCount( seckillGoods.getStockCount() );
        seckillGoodsDTO.setSkuDesc( seckillGoods.getSkuDesc() );

        return seckillGoodsDTO;
    }

    @Override
    public List<SeckillGoodsDTO> convertSeckillGoodsList(List<SeckillGoods> seckillGoodsList) {
        if ( seckillGoodsList == null ) {
            return null;
        }

        List<SeckillGoodsDTO> list = new ArrayList<SeckillGoodsDTO>( seckillGoodsList.size() );
        for ( SeckillGoods seckillGoods : seckillGoodsList ) {
            list.add( convertSeckillGoodsToDTO( seckillGoods ) );
        }

        return list;
    }

    @Override
    public SeckillGoods convertSeckillDTO(SeckillGoodsDTO seckillGoods) {
        if ( seckillGoods == null ) {
            return null;
        }

        SeckillGoods seckillGoods1 = new SeckillGoods();

        seckillGoods1.setId( seckillGoods.getId() );
        seckillGoods1.setSpuId( seckillGoods.getSpuId() );
        seckillGoods1.setSkuId( seckillGoods.getSkuId() );
        seckillGoods1.setSkuName( seckillGoods.getSkuName() );
        seckillGoods1.setSkuDefaultImg( seckillGoods.getSkuDefaultImg() );
        seckillGoods1.setPrice( seckillGoods.getPrice() );
        seckillGoods1.setCostPrice( seckillGoods.getCostPrice() );
        seckillGoods1.setCheckTime( seckillGoods.getCheckTime() );
        seckillGoods1.setStatus( seckillGoods.getStatus() );
        seckillGoods1.setStartTime( seckillGoods.getStartTime() );
        seckillGoods1.setEndTime( seckillGoods.getEndTime() );
        seckillGoods1.setNum( seckillGoods.getNum() );
        seckillGoods1.setStockCount( seckillGoods.getStockCount() );
        seckillGoods1.setSkuDesc( seckillGoods.getSkuDesc() );

        return seckillGoods1;
    }

    @Override
    public OrderDetailDTO secondKillGoodsToOrderDetailDTO(SeckillGoodsDTO seckillGoods, Integer num) {
        if ( seckillGoods == null && num == null ) {
            return null;
        }

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        if ( seckillGoods != null ) {
            orderDetailDTO.setImgUrl( seckillGoods.getSkuDefaultImg() );
            orderDetailDTO.setOrderPrice( seckillGoods.getCostPrice() );
            orderDetailDTO.setSkuId( seckillGoods.getSkuId() );
            orderDetailDTO.setSkuName( seckillGoods.getSkuName() );
        }
        orderDetailDTO.setSkuNum( num );

        return orderDetailDTO;
    }
}
