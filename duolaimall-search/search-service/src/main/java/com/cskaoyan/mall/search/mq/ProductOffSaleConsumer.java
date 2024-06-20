package com.cskaoyan.mall.search.mq;

import com.cskaoyan.mall.mq.constant.MqTopicConst;
import com.cskaoyan.mall.search.service.SearchService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class ProductOffSaleConsumer {
    DefaultMQPushConsumer consumer;

    @Value("${rocketmq.namesrv.addr}")
    String nameserverAddress;

    @Value("${rocketmq.consumer.group}")
    String consumerGroup;

    @Autowired
    SearchService searchService;

    @PostConstruct
    public void init(){
        //创建一个不同的consumer对象
        consumer = new DefaultMQPushConsumer(nameserverAddress+"_offSale");

        //设置nameServer的地址
        consumer.setNamesrvAddr(nameserverAddress);
        //此处是消费者，需要订阅相应的topic
        try {
            consumer.subscribe(MqTopicConst.PRODUCT_OFFSALE_TOPIC,"*");
            //设置消息监听器
            consumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    //在消息监听器中进行操作
                    MessageExt messageExt = list.get(0);
                    byte[] body = messageExt.getBody();
                    try {
                        String skuStr = new String(body, 0, body.length, "utf-8");
                        Long skuId = Long.parseLong(skuStr);
                        searchService.lowerGoods(skuId);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //不要忘了启动consumer
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
