package com.neuedu.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jyw
 * @date 2019/10/28-10:50
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    /**
     * 创建订单
     * @param shippingId
     * @param session
     * @return
     */
    @RequestMapping("/{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId") Integer shippingId, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.createOrder(shippingId,user.getId());
    }

    /**
     * 取消订单
     */
    @RequestMapping("/cancel/{orderNo}")
    public ServerResponse cancelOrder(@PathVariable("orderNo") Long orderNo){

        return orderService.cancelOrder(orderNo);
    }

    /**
     * 获取订单商品信息
     * @param session
     * @return
     */
    @RequestMapping("/get_order_cart_product.do")
    public ServerResponse get_order_product(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"未登录");
        }
        return orderService.get_order_product(user.getId());
    }

    @RequestMapping("/list.do")
    public ServerResponse listOrders(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                     HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"未登录");
        }
        return orderService.listOrders(pageNum,pageSize,user.getId());
    }

    /**
     * 支付
     */
    @RequestMapping("/pay/{orderNo}")
    public ServerResponse pay(@PathVariable("orderNo")Long orderNo,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.pay(user.getId(),orderNo);
    }

    /**
     * 回调接口
     */
    @RequestMapping("/callback.do")
    public String alipay_callback(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Iterator<String> iterator = map.keySet().iterator();
        Map<String,String> params = Maps.newHashMap();
        while(iterator.hasNext()){
            String key = iterator.next();
            String[] value = map.get(key);
            StringBuffer stringBuffer = new StringBuffer();
            if(value!=null&&value.length>=0){
                for (int i = 0; i < value.length; i++) {
                    stringBuffer.append(value[i]);
                    if(i!=value.length-1){
                        stringBuffer.append(",");
                    }
                }

            }
            params.put(key,stringBuffer.toString());
        }
        System.out.println(params);
        // 验签
        try {
            params.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(result){
                // 通过
                orderService.callback(params);
                System.out.println("通过");
            }else {
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
