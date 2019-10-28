package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;

import java.util.List;

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

    /**
     * 根据用户id查看购物车中已选中的商品
     */
    ServerResponse<List<Cart>> findCartsByIdAndChecked(Integer userID);

    /**
     * 批量删除
     * @param cartList
     * @return
     */
    ServerResponse deleteBatch(List<Cart> cartList);
}
