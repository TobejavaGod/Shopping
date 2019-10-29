package com.neuedu.controller.front;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 购物车
 * @author jyw
 * @date 2019/10/27-14:09
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    /**
     * 添加商品到购物车
     */
    @RequestMapping("/add/{productId}/{count}")
    public ServerResponse add(@PathVariable("productId") Integer productId,
                              @PathVariable("count") Integer count,
                              HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        return cartService.add(user.getId(),productId,count);
    }

    /**
     * 购物车列表
     */
    @RequestMapping("/list.do")
    public ServerResponse listCarts(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return cartService.listCarts(user.getId());
    }

    /**
     * 更新购物车中某商品的数量
     */
    @RequestMapping("/update.do")
    public ServerResponse updateCart(Integer productId,Integer count,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return cartService.updateCarts(productId,user.getId(),count);
    }

    /**
     * 移除购物车中某些商品
     */
    @RequestMapping("delete_product.do")
    public ServerResponse delete_product(String productIds){

        return cartService.delete_product(productIds);
    }

    /**
     * 购物车选中某商品
     */
    @RequestMapping("/select.do")
    public ServerResponse select_product(@RequestParam("productId") Integer productId,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return cartService.select_product(productId,user.getId());
    }

    @RequestMapping("/un_select.do")
    public ServerResponse un_select_product(@RequestParam("productId") Integer productId, HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return cartService.unSelect_product(productId,user.getId());
    }
}
