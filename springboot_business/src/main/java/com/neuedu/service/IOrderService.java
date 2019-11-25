package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
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

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    ServerResponse cancelOrder(Long orderNo);

    /**
     * 获取订单信息
     */
    ServerResponse get_order_product(Integer userId);

    /**
     * 订单列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    ServerResponse listOrders(Integer pageNum,Integer pageSize,Integer userId);


    /**
     * 发货
     * @param orderNo
     * @return
     */
    ServerResponse send_product(Long orderNo);

    /**
     * 查询订单详情
     * @param orderNo
     * @param userId 前台查询订单详情只能查询自己的
     * @return
     */
    ServerResponse order_detail(Integer userId,Long orderNo);


    /**
     * 列出所有订单
     * @return
     */
    ServerResponse listAllOrders();
}
