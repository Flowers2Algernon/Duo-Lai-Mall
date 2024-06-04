package com.cskaoyan.mall.product.converter.param;

import com.cskaoyan.mall.product.model.PlatformAttributeInfo;
import com.cskaoyan.mall.product.model.PlatformAttributeValue;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.query.PlatformAttributeValueParam;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T16:24:12+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class PlatformAttributeInfoParamConverterImpl implements PlatformAttributeInfoParamConverter {

    @Override
    public PlatformAttributeInfo attributeInfoParam2Info(PlatformAttributeParam platformAttributeParam) {
        if ( platformAttributeParam == null ) {
            return null;
        }

        PlatformAttributeInfo platformAttributeInfo = new PlatformAttributeInfo();

        platformAttributeInfo.setId( platformAttributeParam.getId() );
        platformAttributeInfo.setAttrName( platformAttributeParam.getAttrName() );
        platformAttributeInfo.setCategoryId( platformAttributeParam.getCategoryId() );
        platformAttributeInfo.setCategoryLevel( platformAttributeParam.getCategoryLevel() );
        platformAttributeInfo.setAttrValueList( platformAttributeValueParamListToPlatformAttributeValueList( platformAttributeParam.getAttrValueList() ) );

        return platformAttributeInfo;
    }

    @Override
    public PlatformAttributeValue attributeValueParam2AttributeValue(PlatformAttributeValueParam platformAttributeValueParam) {
        if ( platformAttributeValueParam == null ) {
            return null;
        }

        PlatformAttributeValue platformAttributeValue = new PlatformAttributeValue();

        platformAttributeValue.setId( platformAttributeValueParam.getId() );
        platformAttributeValue.setValueName( platformAttributeValueParam.getValueName() );
        platformAttributeValue.setAttrId( platformAttributeValueParam.getAttrId() );

        return platformAttributeValue;
    }

    protected List<PlatformAttributeValue> platformAttributeValueParamListToPlatformAttributeValueList(List<PlatformAttributeValueParam> list) {
        if ( list == null ) {
            return null;
        }

        List<PlatformAttributeValue> list1 = new ArrayList<PlatformAttributeValue>( list.size() );
        for ( PlatformAttributeValueParam platformAttributeValueParam : list ) {
            list1.add( attributeValueParam2AttributeValue( platformAttributeValueParam ) );
        }

        return list1;
    }
}
