package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.CheckEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jyw
 * @date 2019/10/27-14:16
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    IProductService productService;

    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        // 参数非空判断
        if(productId==null){
            ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        }
        if(count==null){
            ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品数量不能为空");
        }
        // 判断商品是否存在
        ServerResponse<Product> serverResponse = productService.findProductById(productId);
        if(!serverResponse.isSuccess()){
            return ServerResponse.serverResponseByError(serverResponse.getStatus(),serverResponse.getMsg());
        }else {
            Product product = serverResponse.getData();
            if(product.getStock()<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品已售空");
            }
        }
        // 判断商品是否在购物车中
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 添加商品到购物车
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setChecked(CheckEnum.PRODUCT_CHECKED.getStatus());
            int result = cartMapper.insert(newCart);
            if(result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
            }
        }else {
            // 更新商品的数量
            cart.setQuantity(cart.getQuantity()+count);
            int result = cartMapper.updateByPrimaryKey(cart);
            if(result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
            }
        }
        //封装购物车对象cartVO
        CartVO cartVO = getCartVO(userId);
        // 返回cartVO
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse findCartsByIdAndChecked(Integer userId) {
        List<Cart> cartList = cartMapper.findCartsByIdAndChecked(userId);
        return ServerResponse.serverResponseBySuccess(cartList);
    }

    @Override
    public ServerResponse deleteBatch(List<Cart> cartList) {
        if(cartList==null || cartList.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"要删除的购物车商品不能为空");
        }
        int result = cartMapper.deleteBatch(cartList);
        if(result!=cartList.size()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车清空失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse listCarts(Integer userId) {
        CartVO cartVO = getCartVO(userId);
        if(cartVO==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车中无商品");
        }
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse updateCarts(Integer productId, Integer userId, Integer count) {
        // 参数非空校验
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        }
        if(count==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加商品数量不能为空");
        }
        int result = cartMapper.updateProductByProductId(productId, userId, count);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "更新失败");
        }
        CartVO cartVO = getCartVO(userId);
        if(cartVO==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"vo转换失败");
        }
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse delete_product(String productIds) {
        // 参数非空校验
        if(productIds==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        // 分解参数
        String[] productArr = productIds.split(",");
        List<Integer> productList = Lists.newArrayList();
        for(String productId : productArr){
            productList.add(Integer.parseInt(productId));
        }
        if(productList==null || productList.size()<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数传递有误");
        }
        int result = cartMapper.deleteProducts(productList);
        if(result!=productList.size()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"删除失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse select_product(Integer productId,Integer userId) {
        // 参数非空校验
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        int result = cartMapper.selectProduct(productId, userId);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新出现错误");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse unSelect_product(Integer productId, Integer userId) {
        // 参数非空校验
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        int result = cartMapper.unSelectProduct(productId, userId);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新出现错误");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    private CartVO getCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        // 根据userId查询用户的购物信息
        List<Cart> carts = cartMapper.selectCartsByUserId(userId);
        if(carts==null||carts.size()==0){
            return cartVO;
        }
        // 将cart转为cartProductVO
        int limit_quantity = 0; // 最大购买数量不能超过商品的库存
        String limitQuantity = null;
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        BigDecimal cartTotalPrize = new BigDecimal("0"); // 购物车商品总价
        for (Cart c:carts){
            CartProductVO cartProductVO = new CartProductVO();
            cartProductVO.setId(c.getId());
            cartProductVO.setUserId(userId);
            cartProductVO.setProductId(c.getProductId());
            ServerResponse<Product> serverResponse = productService.findProductById(c.getProductId());
            if(serverResponse.isSuccess()){
                Product product = serverResponse.getData();
                if(product.getStock()>=c.getQuantity()){
                    limit_quantity = c.getQuantity();
                    limitQuantity = "LIMIT_NUM_SUCCESS";
                }else {
                    limit_quantity = product.getStock();
                    limitQuantity = "LIMIT_NUM_FAIL";
                }
                cartProductVO.setLimitQuantity(limitQuantity);
                cartProductVO.setQuantity(limit_quantity);
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),c.getQuantity()*1.0));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductChecked(c.getChecked());
                cartProductVOList.add(cartProductVO);
                if(c.getChecked()==CheckEnum.PRODUCT_CHECKED.getStatus()){
                    // 商品被选中
                    cartTotalPrize = BigDecimalUtils.add(cartTotalPrize.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
            }

        }
        cartVO.setCartProductVOList(cartProductVOList);
        // 计算购物车商品总价
        cartVO.setCartTotalPrice(cartTotalPrize);
        // 查看是否全选
        Integer result = cartMapper.findAllUncheckedProduct(userId);
        if(result==0){
            cartVO.setAllChecked(true);
        }else {
            cartVO.setAllChecked(false);
        }
        return cartVO;
    }
}
