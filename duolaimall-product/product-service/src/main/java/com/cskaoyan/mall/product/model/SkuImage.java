package com.cskaoyan.mall.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.mall.common.model.BaseEntity;
import lombok.Data;

/**
 * <p>
 * SkuImage
 * </p>
 *
 */
@Data
@TableName("sku_image")
public class SkuImage extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// "商品id"
	@TableField("sku_id")
	private Long skuId;

	// "图片名称"
	//todo 此处修改了imgName和imgUrl的exist=false
	@TableField("img_name")
	private String imgName;

	// "图片路径"
	@TableField("img_url")
	private String imgUrl;

	//"商品图片id"
	@TableField("spu_img_id")
	private Long spuImgId;

	//"是否默认"
	@TableField("is_default")
	private String isDefault;

}

