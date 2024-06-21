package com.cskaoyan.mall.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.DateUtil;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 创建日期: 2023/03/17 14:38
 */
@Component
public class AlipayHelper implements PayHelper {

    @Autowired
    CsmallAlipayConfig csmallAlipayConfig;

    @Autowired
    AlipayClient alipayClient;


    /**
     * 向支付宝发起请求，生成支付页面（表单）
     */
    @SneakyThrows
    @Override
    public String prePay(OrderInfoDTO orderInfo) {
        // 构建请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 设置同步回调（不需要公网可访问）
        request.setReturnUrl(csmallAlipayConfig.getReturnPaymentUrl());

        // 设置异步回调（公网可访问）
        request.setNotifyUrl(csmallAlipayConfig.getNotifyPaymentUrl());

        // 构建参数
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderInfo.getOutTradeNo());
        model.setTotalAmount(orderInfo.getTotalAmount().toString());
        model.setSubject(orderInfo.getTradeBody());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        // 当前时间
        long now = System.currentTimeMillis();
        // 订单创建时间
        Date createTime = orderInfo.getCreateTime();
        long timeout = new Date().getTime() - createTime.getTime();
        // 订单过期时间 时间单位是分钟
        long timeOutSpan = 3;
        if (timeout >= timeOutSpan * 60 * 1000) {
            // 超过了订单的超时时间
            return "对不起，已经超过支付时间，请重新下单";
        }
        String timeoutStr;
        // 四舍五入求超时时间
        // 求以毫秒为单位的超时时间
        long timeoutRemain = timeOutSpan * 60 * 1000 - timeout;
        // 生成过期时间对应的日期格式字符串
        timeoutStr = DateFormatUtils.format(new Date(now + timeoutRemain), "yyyy-MM-dd HH:mm:ss");

        // 设置超时时间
        model.setTimeExpire(timeoutStr);
        request.setBizModel(model);

        // 向支付宝发起请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

        return response.getBody();
    }

    /**
     * 根据外部订单号 查询支付宝支付状态
     */
    @Override
    public String queryTradeStatus(String outTradeNo) {
        String tradeStatus = "";
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();

            model.setOutTradeNo(outTradeNo);
            request.setBizModel(model);

            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if ("ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {
                return "ACQ.TRADE_NOT_EXIST";
            }

            if (response.isSuccess()) {
                tradeStatus = response.getTradeStatus();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return tradeStatus;
    }

    /**
     * 关闭支付宝支付记录
     */
    public void closeTrade(String outTradeNo) {

        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(outTradeNo);

        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);

        try {
            alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
