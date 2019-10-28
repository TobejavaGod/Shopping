package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 品类接口
 * @author jyw
 * @date 2019/10/25-17:37
 */
public interface ICategoryService {

    /**
     * 添加类别
     * @return
     */
    ServerResponse addCategory(Category category);

    /**
     * 修改类别
     * @param category
     * categoryId
     * categoryName
     * categoryUrl
     * @return
     */
    ServerResponse updateCategory(Category category);

    /**
     * 查看平级类别
     * @param categoryId
     * @return
     */
    ServerResponse getCategoryById(Integer categoryId);

    /**
     * 递归查看子类别
     * @param categoryId
     * @return
     */
    ServerResponse deepCategory(Integer categoryId);

    /**
     * 根据id查询类别
     * @param categoryId
     * @return
     */
    ServerResponse<Category> selectCategory(Integer categoryId);
}
