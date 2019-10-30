package com.neuedu.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单商品信息vo
 * @author jyw
 * @date 2019/10/30-14:19
 */
public class OrderProductVO {
    private List<OrderItemVO> orderItemVOList;
    private String imageHost;
    private BigDecimal totalPrice;

    public OrderProductVO() {
    }

    public OrderProductVO(List<OrderItemVO> orderItemVOList, String imageHost, BigDecimal totalPrice) {
        this.orderItemVOList = orderItemVOList;
        this.imageHost = imageHost;
        this.totalPrice = totalPrice;
    }

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
