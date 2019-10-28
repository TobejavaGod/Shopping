package com.neuedu.service;

import com.neuedu.common.ServerResponse;

/**
 * @author jyw
 * @date 2019/10/27-14:14
 */
public interface ICartService {
    /**
     * 添加商品到购物车
     * @return
     */
    ServerResponse add(Integer userId,Integer productId,Integer count);
}
