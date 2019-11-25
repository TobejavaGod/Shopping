package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.User;
import com.neuedu.service.ICategoryService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author jyw
 * @date 2019/10/25-17:26
 */
@RestController
@RequestMapping("/manager/category")
@CrossOrigin(value = "http://localhost:8080")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;
    /**
     * 添加类别
     * @return
     */
    @RequestMapping("/add_category.do")
    public ServerResponse addCategory(Category category){
        return categoryService.addCategory(category);
    }

    /**
     * 修改类别
     * @param category
     * categoryId
     * categoryName
     * categoryUrl
     * @return
     */
    @RequestMapping("/set_category_name.do")
    public ServerResponse updateCategory(Category category){
        return categoryService.updateCategory(category);
    }

    /**
     * 查看平级类别
     * @param categoryId
     * @return
     */
    @RequestMapping("/{categoryId}")
    public ServerResponse getCategoryById(@PathVariable("categoryId") Integer categoryId){
        return categoryService.getCategoryById(categoryId);
    }

    /**
     * 递归查看子类别
     * @param categoryId
     * @return
     */
    @RequestMapping("/deep/{categoryId}")
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId){
        return categoryService.deepCategory(categoryId);
    }
}
