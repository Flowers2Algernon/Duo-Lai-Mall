package com.cskaoyan.mall.common.cache;

import com.cskaoyan.mall.common.constant.RedisConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedisCacheAspect {
    @Autowired
    private RedissonClient redissonClient;

    //定义一个环绕通知
    @Around("@annotation(com.cskaoyan.mall.common.cache.RedisCache)")
    public Object gmallCacheAspectMethod(ProceedingJoinPoint point) {

        //定义一个对象
        Object obj = null;
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        RedisCache redisCache = methodSignature.getMethod().getAnnotation(RedisCache.class);
        //获取到注解上的前缀
        String prefix = redisCache.prefix();
        //组成缓存的key，获取方法传递的参数
        String key = prefix + Arrays.asList(point.getArgs()).toString();
        RLock lock = null;
        try {
            //可以通过这个key 获取缓存的数据
            obj = this.redissonClient.getBucket(key).get();
            if (obj != null) {
                //获取到了直接返回
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //加锁
            lock = redissonClient.getLock(key + ":lock");
            lock.lock();
            Object redisData = this.redissonClient.getBucket(key).get();
            //double check
            if (redisData != null) {
                return redisData;
            }

            //执行业务逻辑，直接从数据库中获取数据
            obj = point.proceed(point.getArgs());

            //将结果放入redis中
            obj = putInRedis(obj, key, methodSignature);

            return obj;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            //解锁
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    //将数据放入到redis缓存中
    private Object putInRedis(Object obj, String key, MethodSignature methodSignature) {
        try {
            if (obj == null) {
                //防止缓存穿透
                Class returnType = methodSignature.getReturnType();
                if (returnType.isAssignableFrom(List.class)) {
                    //返回值是List类型
                    obj = new ArrayList<>();
                } else if ((Map.class.equals(returnType))) {
                    obj = new HashMap<>();
                } else {
                    //其他类型
                    Constructor declaredConstructor = returnType.getDeclaredConstructor();
                    declaredConstructor.setAccessible(true);
                    obj = declaredConstructor.newInstance();
                }
                //  将缓存的数据变为 Json 的 字符串,默认值的过期时间是1分钟
                this.redissonClient.getBucket(key).set(obj, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
