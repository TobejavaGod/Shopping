package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.util.Map;

/**
 * 订单接口
 * @author jyw
 * @date 2019/10/28-10:52
 */
public interface IOrderService {

    ServerResponse createOrder(Integer shippingId,Integer userId);

    ServerResponse pay(Integer userId,Long orderNo);

    /**
     * 支付回调接口
     */
    String callback(Map<String,String> requestParams);
}
