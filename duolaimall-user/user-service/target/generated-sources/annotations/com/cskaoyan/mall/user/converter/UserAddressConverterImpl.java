package com.cskaoyan.mall.user.converter;

import com.cskaoyan.mall.user.dto.UserAddressDTO;
import com.cskaoyan.mall.user.model.UserAddress;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-18T16:04:58+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class UserAddressConverterImpl implements UserAddressConverter {

    @Override
    public UserAddressDTO userAddressPO2DTO(UserAddress userAddress) {
        if ( userAddress == null ) {
            return null;
        }

        UserAddressDTO userAddressDTO = new UserAddressDTO();

        userAddressDTO.setId( userAddress.getId() );
        userAddressDTO.setUserAddress( userAddress.getUserAddress() );
        userAddressDTO.setUserId( userAddress.getUserId() );
        userAddressDTO.setConsignee( userAddress.getConsignee() );
        userAddressDTO.setPhoneNum( userAddress.getPhoneNum() );
        userAddressDTO.setIsDefault( userAddress.getIsDefault() );

        return userAddressDTO;
    }

    @Override
    public List<UserAddressDTO> userAddressPOs2DTOs(List<UserAddress> userAddresses) {
        if ( userAddresses == null ) {
            return null;
        }

        List<UserAddressDTO> list = new ArrayList<UserAddressDTO>( userAddresses.size() );
        for ( UserAddress userAddress : userAddresses ) {
            list.add( userAddressPO2DTO( userAddress ) );
        }

        return list;
    }
}
