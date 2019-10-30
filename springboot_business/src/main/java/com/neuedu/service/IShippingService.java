package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

/**
 * @author jyw
 * @date 2019/10/28-8:32
 */
public interface IShippingService {
    /**
     * 添加收货地址
     * @param shipping
     * @return
     */
    ServerResponse add(Shipping shipping);

    ServerResponse findShippingById(Integer shippingId);

    /**
     * 删除地址
     * @param shippingId
     * @return
     */
    ServerResponse delete(Integer shippingId);


    /**
     * 查看某地址
     * @param shippingId
     * @return
     */
    ServerResponse selectShipping(Integer shippingId);


    /**
     * 地址列表
     */
    ServerResponse listAllShipping(Integer pageNum,Integer pageSize,Integer userId);
}
