package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.*;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.pojo.*;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.service.IShippingService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.DateUtils;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ShippingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * @author jyw
 * @date 2019/10/28-10:53
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    ICartService cartService;

    @Autowired
    IProductService productService;

    @Autowired
    IShippingService shippingService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Value("${business.imageHost}")
    private String imageHost;

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
        ServerResponse<List<Cart>> serverResponse = cartService.findCartsByIdAndChecked(userId);
        List<Cart> cartList = serverResponse.getData();
        if(cartList==null || cartList.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车为空或购物车中商品未选中");
        }
        // 将购物车pojo转为orderItemVO
        ServerResponse orderItem_serverResponse = getCartOrderItem(userId, cartList);
        if(!orderItem_serverResponse.isSuccess()){
            return orderItem_serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) orderItem_serverResponse.getData();
        // 创建order实体并保存在数据库中
        ServerResponse<Order> orderServerResponse = generateOrder(userId,shippingId,orderItemList);
        if(!orderServerResponse.isSuccess()){
            return orderServerResponse;
        }
        Order order = orderServerResponse.getData();
        // 保存订单项vo集合
        ServerResponse save_ServerResponse = saveOrderItems(orderItemList, order);
        if(!save_ServerResponse.isSuccess()){
            return save_ServerResponse;
        }
        // 扣库存
        reduceProductStock(orderItemList);
        // 清空购物车中下单的商品 -> 批量删除
        ServerResponse cart_serverResponse = cartService.deleteBatch(cartList);
        if(!cart_serverResponse.isSuccess()){
            return cart_serverResponse;
        }
        // 返回订单vo
        return assembleOrderVO(order,orderItemList,shippingId);
    }


    //OrderVO
    private ServerResponse assembleOrderVO(Order order, List<OrderItem> orderItemList, Integer shippingId){
        OrderVO orderVO=new OrderVO();

        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO= assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(imageHost);
        ServerResponse<Shipping> serverResponse= shippingService.findShippingById(shippingId);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        Shipping shipping=serverResponse.getData();
        if(shipping!=null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO= assembleShippingVO(shipping);
            orderVO.setShippingVo(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }

        orderVO.setStatus(order.getStatus());
        OrderStatusEnum orderStatusEnum= OrderStatusEnum.codeOf(order.getStatus());
        if(orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }

        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        PaymentWayEnum paymentEnum=PaymentWayEnum.codeOf(order.getPaymentType());
        if(paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());

        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO shippingVO=new ShippingVO();

        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }


    private ServerResponse reduceProductStock(List<OrderItem> orderItemList){
        for(OrderItem orderItem:orderItemList){
            Integer productId = orderItem.getProductId();
            ServerResponse<Product> serverResponse = productService.findProductByProductId(productId);
            Product product = serverResponse.getData();
            int stock = product.getStock()-orderItem.getQuantity();
            ServerResponse reduce_Response = productService.reduceProductStock(productId, stock);
            if(!reduce_Response.isSuccess()){
                return reduce_Response;
            }
        }
        return  ServerResponse.serverResponseBySuccess();
    }

    // 保存订单项vo集合
    private ServerResponse saveOrderItems(List<OrderItem> orderItems,Order order){
        for(OrderItem orderItem:orderItems){
            orderItem.setOrderNo(order.getOrderNo());
        }
        int result = orderItemMapper.insertBatch(orderItems);
        if(result!=orderItems.size()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单明细保存失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    // 生成订单
    private ServerResponse generateOrder(Integer userId,Integer shippingId,List<OrderItem> orderItemList){
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(generatorOrderNo());
        order.setShippingId(shippingId);
        order.setPayment(getOrderTotalPrice(orderItemList));
        order.setPaymentType(PaymentWayEnum.PAYMENT_ONLINE.getMethod());
        order.setStatus(OrderStatusEnum.ORDER_NOT_PAY.getStatus());
        int result = orderMapper.insert(order);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"插入失败");
        }
        return ServerResponse.serverResponseBySuccess(order);
    }


    // 生成订单号
    private Long generatorOrderNo(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    // 生成订单总价
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItem orderItem : orderItems){
            totalPrice = BigDecimalUtils.add(totalPrice.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return totalPrice;

    }


    private  ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){


        List<OrderItem> orderItemList= Lists.newArrayList();

        for(Cart cart:cartList){

            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            ServerResponse<Product> serverResponse=productService.findProductById(cart.getProductId());
            if(!serverResponse.isSuccess()){
                return  serverResponse;
            }
            Product product= serverResponse.getData();
            if(product==null){
                return  ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品不存在");
            }
            if(product.getStatus()!= ProductStatusEnum.PRODUCT_SALE.getStatus()){//商品下架
                return ServerResponse.serverResponseByError("id为"+product.getId()+"的商品已经下架");
            }
            if(product.getStock()<cart.getQuantity()){//库存不足
                return ServerResponse.serverResponseByError("id为"+product.getId()+"的商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }

        return  ServerResponse.serverResponseBySuccess(orderItemList);
    }



    /**
     * orderitem pojo对象转vo
     * @param orderItem
     * @return
     */
    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();

        if(orderItem!=null){

            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());

        }

        return orderItemVO;
    }
}
