package com.cskaoyan.mall.mq.producer;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class BaseProducer {
    @Value("${rocketmq.namesrv.addr}")
    String namesrvAddr;

    @Value("${rocketmq.producer.group}")
    String producerGroup;

    DefaultMQProducer mqProducer;

    @PostConstruct
    public void init() {
        mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr(namesrvAddr);
        try {
            mqProducer.start();
            log.info("mqProducer inited successed...namesrcAddr:{}, producerGroup:{}", namesrvAddr,producerGroup);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    public Boolean sendMessage(String topicName,Object messageBody){
        try {
            String jsonMessage = JSON.toJSONString(messageBody);
            log.info("准备发送消息，topic:{}, message:{}", topicName, jsonMessage);
            //注意此处是特意设置成这样--方便打印出错误信息
            Message message = new Message(topicName, jsonMessage.getBytes("utf-8"));
            SendResult sendResult = mqProducer.send(message);
            if (sendResult == null
                    || sendResult.getSendStatus() == null
                    || !SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                log.info("消息发送失败，topic:{}, message:{}", topicName,jsonMessage);
                return false;
            }
            log.info("消息发送成功，topic:{}, message:{}", topicName,jsonMessage);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //发送延迟信息
    public Boolean sendDelayedMessage(String topicName,Object messageBody,int delayLevel){
        try {
            String jsonMessage = JSON.toJSONString(messageBody);
            log.info("准备发送延迟消息，topic:{}, message:{}", topicName, jsonMessage);
            Message message = new Message(topicName, jsonMessage.getBytes("utf-8"));

            //设置消息延迟级别
            if (delayLevel<=0) throw new RuntimeException("发送延迟消息，延迟级别应该大于0，当前延迟级别为: delayLevel="+delayLevel);
            message.setDelayTimeLevel(delayLevel);
            SendResult sendResult = mqProducer.send(message);
            if (sendResult == null
                    || sendResult.getSendStatus() == null
                    || !SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                log.info("延迟消息发送失败，topic:{}, message:{}", topicName,jsonMessage);
                return false;
            }

            // 消息发送成功
            log.info("延迟消息发送成功，topic:{}, message:{}", topicName, jsonMessage);
            return true;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
