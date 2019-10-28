package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
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
    @RequestMapping("/{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId") Integer shippingId, HttpSession session){

        return null;
    }
}
