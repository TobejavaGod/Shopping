package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author jyw
 * @date 2019/10/28-10:50
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @RequestMapping("/{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId") Integer shippingId, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.createOrder(shippingId,user.getId());
    }
}
