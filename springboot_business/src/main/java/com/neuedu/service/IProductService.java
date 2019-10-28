package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.vo.ProductDetailVO;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;


/**
 * @author jyw
 * @date 2019/10/25-19:41
 */
public interface IProductService {
    /**
     * 添加或更新商品
     * @param product
     * @return
     */
    ServerResponse addOrUpdate(Product product);

    /**
     * 商品上下架
     * @param productId
     * @param status
     * @return
     */
    ServerResponse set_sale_status(Integer productId, Integer status);

    /**
     * 后台搜索商品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize);


    /**
     * 商品详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVO> detail(Integer productId);

    ServerResponse<Product> findProductByProductId(Integer productId);

    /**
     * 根据id查询商品的库存信息
     * @param productId
     * @return
     */
    ServerResponse<Product> findProductById(Integer productId);

    /**
     * 扣库存
     */
    ServerResponse reduceProductStock(Integer productId, Integer stock);
};
