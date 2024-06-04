package com.cskaoyan.mall.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.mall.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * PlatformAttributeValue
 * </p>
 *
 */
@Data
@ToString(callSuper = true)
@TableName("platform_attr_value")
public class PlatformAttributeValue extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// "属性值名称"
	@TableField("value_name")
	private String valueName;

	// "属性id"
	@TableField("attr_id")
	private Long attrId;
}

