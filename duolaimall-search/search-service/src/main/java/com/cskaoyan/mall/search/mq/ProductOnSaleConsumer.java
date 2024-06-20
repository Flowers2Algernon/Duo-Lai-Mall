package com.cskaoyan.mall.search.mq;

import com.cskaoyan.mall.mq.constant.MqTopicConst;
import com.cskaoyan.mall.search.service.SearchService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class ProductOnSaleConsumer {
    DefaultMQPushConsumer consumer;

    @Value("${rocketmq.namesrv.addr}")
    String nameserverAddress;

    @Value("${rocketmq.consumer.group}")
    String consumerGroup;

    @Autowired
    SearchService searchService;

    @PostConstruct
    public void init() {
        //创建consumer对象
        consumer = new DefaultMQPushConsumer(consumerGroup + "_onSale");
        //设置nameServe地址
        consumer.setNamesrvAddr(nameserverAddress);

        try {
            //订阅主题
            consumer.subscribe(MqTopicConst.PRODUCT_ONSALE_TOPIC,"*");

            //设置消息监听器
            consumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    MessageExt messageExt = list.get(0);
                    byte[] body = messageExt.getBody();
                    try {
                        String skuIdStr = new String(body, 0, body.length, "utf-8");
                        Long skuId = Long.parseLong(skuIdStr);
                        searchService.upperGoods( skuId);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //不要忘了最后的启动consumer
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
