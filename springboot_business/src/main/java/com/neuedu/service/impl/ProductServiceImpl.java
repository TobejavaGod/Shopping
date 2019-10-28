package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.util.DateUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jyw
 * @date 2019/10/25-19:42
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    ICategoryService categoryService;

    @Value("${business.imageHost}")
    private String imageHost;

    @Override
    public ServerResponse addOrUpdate(Product product) {
        // 参数非空校验
        if (product == null) {
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL, "参数不能为空");
        }
        // 设置商品的主图
        String subImages = product.getSubImages();
        if(subImages!=null&&!"".equals(subImages)){
            String[] imageArr = subImages.split(",");
            if(imageArr.length>0){
                product.setMainImage(imageArr[0]);
            }
        }
        Integer id = product.getId();
        if (id == null) {
            // 添加
            int resultInsert = productMapper.insert(product);
            if (resultInsert <= 0) {
                return ServerResponse.serverResponseByError(ResponseCode.ERROR, "添加失败");
            } else {
                return ServerResponse.serverResponseBySuccess();
            }
        } else {
            // 更新
            int resultUpdate = productMapper.updateByPrimaryKey(product);
            if (resultUpdate <= 0) {
                return ServerResponse.serverResponseByError(ResponseCode.ERROR, "更新失败");
            } else {
                return ServerResponse.serverResponseBySuccess();
            }
        }
    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        // 参数非空校验
        if (productId == null) {
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL, "参数不能为空");
        }
        if (status == null) {
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL, "参数不能为空");
        }
        if(status<1 || status>3){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品状态输入不合法");
        }
        int result = productMapper.updateStatusById(productId, status);
        if(result<=0){
            return  ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不存在");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        if(productName!=null){
            productName = "%"+productName+"%";
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectProductsByNameAndId(productName, productId);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        // 将pojo转为vo
        if(productList!=null&&productList.size()>0){
            for(Product p : productList){
                ProductListVO productListVO = assembleProductListVO(p);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer productId) {
        if(productId==null){
            ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseBySuccess();
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse findProductById(Integer productId) {
        // 参数非空校验
        if(productId==null){
            ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在");
        }
        return ServerResponse.serverResponseBySuccess(product);
    }

    /**
     * 将product的pojo转为vo
     * @param product
     * @return
     */
    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());


        return  productListVO;
    }

    private ProductDetailVO assembleProductDetailVO(Product product){


        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(imageHost);
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        ServerResponse<Category> serverResponse=categoryService.selectCategory(product.getCategoryId());
        Category category=serverResponse.getData();
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return productDetailVO;
    }
}
