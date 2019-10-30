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

    /**
     * 购物车列表 -> 用户
     * @param userId
     * @return
     */
    ServerResponse listCarts(Integer userId);

    /**
     * 更新购物车中某商品的数量
     * @param productId
     * @param userId
     * @param count
     * @return
     */
    ServerResponse updateCarts(Integer productId,Integer userId,Integer count);

    /**
     * 移除购物车中某些商品
     * @param productIds
     * @return
     */
    ServerResponse delete_product(String productIds);

    /**
     * 购物车选中某商品
     * @param productId
     * @return
     */
    ServerResponse select_product(Integer productId,Integer userId);

    /**
     * 取消选中
     * @param productId
     * @param userId
     * @return
     */
    ServerResponse unSelect_product(Integer productId,Integer userId);

    /**
     * 购物车全选
     * @param userId
     * @return
     */
    ServerResponse select_allProduct(Integer userId);

    /**
     * 购物车取消全选
     * @param userId
     * @return
     */
    ServerResponse select_noneProduct(Integer userId);
}
