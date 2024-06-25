package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.CategoryHierarchyDTO;
import com.cskaoyan.mall.product.dto.FirstLevelCategoryDTO;
import com.cskaoyan.mall.product.dto.SecondLevelCategoryDTO;
import com.cskaoyan.mall.product.dto.ThirdLevelCategoryDTO;
import com.cskaoyan.mall.product.model.CategoryHierarchy;
import com.cskaoyan.mall.product.model.FirstLevelCategory;
import com.cskaoyan.mall.product.model.SecondLevelCategory;
import com.cskaoyan.mall.product.model.ThirdLevelCategory;
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
public class CategoryConverterImpl implements CategoryConverter {

    @Override
    public FirstLevelCategoryDTO firstLevelCategoryPO2DTO(FirstLevelCategory firstLevelCategory) {
        if ( firstLevelCategory == null ) {
            return null;
        }

        FirstLevelCategoryDTO firstLevelCategoryDTO = new FirstLevelCategoryDTO();

        firstLevelCategoryDTO.setId( firstLevelCategory.getId() );
        firstLevelCategoryDTO.setName( firstLevelCategory.getName() );

        return firstLevelCategoryDTO;
    }

    @Override
    public List<FirstLevelCategoryDTO> firstLevelCategoryPOs2DTOs(List<FirstLevelCategory> firstLevelCategories) {
        if ( firstLevelCategories == null ) {
            return null;
        }

        List<FirstLevelCategoryDTO> list = new ArrayList<FirstLevelCategoryDTO>( firstLevelCategories.size() );
        for ( FirstLevelCategory firstLevelCategory : firstLevelCategories ) {
            list.add( firstLevelCategoryPO2DTO( firstLevelCategory ) );
        }

        return list;
    }

    @Override
    public SecondLevelCategoryDTO secondLevelCategoryPO2DTO(SecondLevelCategory secondLevelCategory) {
        if ( secondLevelCategory == null ) {
            return null;
        }

        SecondLevelCategoryDTO secondLevelCategoryDTO = new SecondLevelCategoryDTO();

        secondLevelCategoryDTO.setId( secondLevelCategory.getId() );
        secondLevelCategoryDTO.setName( secondLevelCategory.getName() );
        secondLevelCategoryDTO.setFirstLevelCategoryId( secondLevelCategory.getFirstLevelCategoryId() );

        return secondLevelCategoryDTO;
    }

    @Override
    public List<SecondLevelCategoryDTO> secondLevelCategoryPOs2DTOs(List<SecondLevelCategory> secondLevelCategories) {
        if ( secondLevelCategories == null ) {
            return null;
        }

        List<SecondLevelCategoryDTO> list = new ArrayList<SecondLevelCategoryDTO>( secondLevelCategories.size() );
        for ( SecondLevelCategory secondLevelCategory : secondLevelCategories ) {
            list.add( secondLevelCategoryPO2DTO( secondLevelCategory ) );
        }

        return list;
    }

    @Override
    public ThirdLevelCategoryDTO thirdLevelCategoryPO2DTO(ThirdLevelCategory thirdLevelCategory) {
        if ( thirdLevelCategory == null ) {
            return null;
        }

        ThirdLevelCategoryDTO thirdLevelCategoryDTO = new ThirdLevelCategoryDTO();

        thirdLevelCategoryDTO.setId( thirdLevelCategory.getId() );
        thirdLevelCategoryDTO.setName( thirdLevelCategory.getName() );
        thirdLevelCategoryDTO.setSecondLevelCategoryId( thirdLevelCategory.getSecondLevelCategoryId() );

        return thirdLevelCategoryDTO;
    }

    @Override
    public List<ThirdLevelCategoryDTO> thirdLevelCategoryPOs2DTOs(List<ThirdLevelCategory> thirdLevelCategories) {
        if ( thirdLevelCategories == null ) {
            return null;
        }

        List<ThirdLevelCategoryDTO> list = new ArrayList<ThirdLevelCategoryDTO>( thirdLevelCategories.size() );
        for ( ThirdLevelCategory thirdLevelCategory : thirdLevelCategories ) {
            list.add( thirdLevelCategoryPO2DTO( thirdLevelCategory ) );
        }

        return list;
    }

    @Override
    public CategoryHierarchyDTO categoryViewPO2DTO(CategoryHierarchy categoryHierarchy) {
        if ( categoryHierarchy == null ) {
            return null;
        }

        CategoryHierarchyDTO categoryHierarchyDTO = new CategoryHierarchyDTO();

        categoryHierarchyDTO.setFirstLevelCategoryId( categoryHierarchy.getFirstLevelCategoryId() );
        categoryHierarchyDTO.setFirstLevelCategoryName( categoryHierarchy.getFirstLevelCategoryName() );
        categoryHierarchyDTO.setSecondLevelCategoryId( categoryHierarchy.getSecondLevelCategoryId() );
        categoryHierarchyDTO.setSecondLevelCategoryName( categoryHierarchy.getSecondLevelCategoryName() );
        categoryHierarchyDTO.setThirdLevelCategoryId( categoryHierarchy.getThirdLevelCategoryId() );
        categoryHierarchyDTO.setThirdLevelCategoryName( categoryHierarchy.getThirdLevelCategoryName() );

        return categoryHierarchyDTO;
    }
}
