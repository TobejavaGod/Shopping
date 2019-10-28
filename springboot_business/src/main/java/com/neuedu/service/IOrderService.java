package com.neuedu.service;

import com.neuedu.common.ServerResponse;

/**
 * 订单接口
 * @author jyw
 * @date 2019/10/28-10:52
 */
public interface IOrderService {

    ServerResponse createOrder(Integer shippingId,Integer userId);
}
