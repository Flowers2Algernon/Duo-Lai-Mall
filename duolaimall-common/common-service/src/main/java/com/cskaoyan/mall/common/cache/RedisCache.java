package com.cskaoyan.mall.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {

    //给缓存数据添加前缀，以便区分不同的缓存数据
    String prefix() default "cache:";
}
