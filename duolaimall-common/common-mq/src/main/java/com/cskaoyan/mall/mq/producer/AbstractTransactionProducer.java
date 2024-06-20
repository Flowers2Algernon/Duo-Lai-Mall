package com.cskaoyan.mall.mq.producer;

import com.alibaba.fastjson.JSON;
import com.cskaoyan.mall.mq.constant.MqResultEnum;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;

import java.io.UnsupportedEncodingException;

@Slf4j
public abstract class AbstractTransactionProducer {
    private TransactionMQProducer mqProducer;

    public void init(TransactionMQProducer mqProducer,String nameserverAddr){
        this.mqProducer=mqProducer;
        mqProducer.setNamesrvAddr(nameserverAddr);
        //设置监听器
        mqProducer.setTransactionListener(getTransactionListener());
        try {
            mqProducer.start();
            log.info("mqProducer inited successed...namesrcAddr:{}, producerGroup:{}", nameserverAddr);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public abstract TransactionListener getTransactionListener();

    public MqResultEnum sendTransactionMessage(String topicName,Object messageBody,Object arg){
        String jsonMessage = JSON.toJSONString(messageBody);
        try {
            log.info("准备发送消息，topic:{}, message:{}", topicName, jsonMessage);
            Message message = new Message(topicName, jsonMessage.getBytes("utf-8"));
            TransactionSendResult transactionSendResult = mqProducer.sendMessageInTransaction(message, arg);
            if (transactionSendResult==null||transactionSendResult.getSendStatus()==null||!SendStatus.SEND_OK.equals(transactionSendResult.getSendStatus())){
                log.info("消息发送失败，topic:{}, message:{}", topicName, jsonMessage);
                return MqResultEnum.SEND_FAIL;
            }
            if (LocalTransactionState.COMMIT_MESSAGE.equals(transactionSendResult.getLocalTransactionState())) {
                log.info("本地事务执行成功，topic:{}, message:{}", topicName, jsonMessage);
                return MqResultEnum.Local_TRANSACTION_SUCCESS;
            }
            log.info("本地事务执行失败，topic:{}, message:{}", topicName, jsonMessage);
            return MqResultEnum.LOCAL_TRANSACTION_FAIL;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return MqResultEnum.SEND_FAIL;
    }
}
