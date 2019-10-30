package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingService;
import com.neuedu.util.Const;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author jyw
 * @date 2019/10/28-8:31
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    IShippingService shippingService;

    @RequestMapping("/add.do")
    public ServerResponse add(Shipping shipping){

        return shippingService.add(shipping);
    }

    @RequestMapping("/del.do")
    public ServerResponse delete(Integer shippingId){
        return shippingService.delete(shippingId);
    }

    @RequestMapping("/select.do")
    public ServerResponse selectShipping(Integer shippingId){

        return shippingService.selectShipping(shippingId);
    }

    @RequestMapping("/list.do")
    public ServerResponse listAllShipping(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                          HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.serverResponseByError("未登录");
        }
        return shippingService.listAllShipping(pageNum,pageSize,user.getId());
    }
}
