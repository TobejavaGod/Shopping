package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * @author jyw
 * @date 2019/10/28-10:53
 */
@Service
public class OrderServiceImpl implements IOrderService {
    /**
     * 创建订单
     * @param shippingId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse createOrder(Integer shippingId, Integer userId) {
        // 参数非空校验
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        // 查看用户购物车中已选中的商品
        // 将购物车pojo转为orderItemVO
        // 创建order实体并保存在数据库中
        // 保存订单项vo集合
        // 扣库存
        // 清空购物车中下单的商品
        // 返回订单vo
        return null;
    }
}
