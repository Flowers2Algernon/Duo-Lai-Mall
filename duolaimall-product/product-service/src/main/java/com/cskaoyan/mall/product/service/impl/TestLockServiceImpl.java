package com.cskaoyan.mall.product.service.impl;

import com.cskaoyan.mall.product.service.TestLockService;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TestLockServiceImpl implements TestLockService {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void incrWithLock() {
        //synoriginalMethod();

        //解决进程挂了时其他线程无法解锁导致卡死的问题--设置单个锁的过期时间
        //aboutTimeInfluence();

        //解决不同进程之间能随便解锁的问题--设置锁的value值为uuid,并要求解锁的线程和其对应的锁具有相同的uuid
        //uuidMethodLock();

        //使用Redisson实现的分布式锁--以达到判断和释放锁是一个原子操作
        RLock redisLock = redissonClient.getLock("lock:number");
        try {
            //加锁，失败会自动在这里阻塞
            redisLock.lock();
            //此时加锁成功，代码执行到下方
            RBucket<Integer> bucket = redissonClient.getBucket("number");
            //获取key为number的value值
            Integer number = bucket.get();
            number++;
            bucket.set(number);
        }finally {
            //释放锁
            redisLock.unlock();
        }
    }

    private void uuidMethodLock() {
        RBucket<String> bucket = redissonClient.getBucket("lock:number");
        String uuid = UUID.randomUUID().toString();
        //trySet方法等价于SETNX，设置锁对应的值
        boolean exists = bucket.trySet(uuid, 3, TimeUnit.SECONDS);
        if (!exists){
            //此时说明已经加锁，稍后重试
            try {
                Thread.sleep(10);
                incrWithLock();
                return;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //加锁成功的情况
        try {
            RBucket<Integer> bucket1 = redissonClient.getBucket("number");
            int number = bucket1.get();
            number++;
            bucket1.set(number);
        }finally {
            if (uuid.equals(bucket.get())){
                //说明是加锁的线程在释放锁，可以正确释放
                bucket.delete();
            }
        }
    }

    private void aboutTimeInfluence() {
        //在操作Redis中的数据前先加锁，lock:number对应的值可以是任意的
        RBucket<String> bucket = redissonClient.getBucket("lock:number");
        boolean lockObj = bucket.trySet("lockObj",3*60, TimeUnit.SECONDS);
        if (!lockObj){
            //如果锁已存在，即已经加锁，则稍后重试
            try {
                Thread.sleep(10);
                incrWithLock();
                return;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //如果加锁成功，则number自增1
        try {
            RBucket<Integer> bucket1 = redissonClient.getBucket("number");
            int number =bucket1.get();
            number++;
            bucket1.set(number);
        }finally {
            //访问完数据之后，释放锁，即删除lock:number这个key
            bucket.delete();
        }
    }

    private void synoriginalMethod() {
        RBucket<Integer> bucket = redissonClient.getBucket("number");
        //获取key为number的value的值
        int number = bucket.get() == null ? 0 : bucket.get();
        number++;
        bucket.set(number);
    }
}
