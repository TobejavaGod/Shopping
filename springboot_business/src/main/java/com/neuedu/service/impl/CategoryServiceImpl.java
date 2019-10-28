package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jyw
 * @date 2019/10/25-17:41
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(Category category) {
        // 参数非空校验
        if(category==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        int result = categoryMapper.insert(category);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"种类添加失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse updateCategory(Category category) {
        // 参数非空校验
        if(category==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        // 判断类别id是否为空
        if(category.getId()==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"类别id不能为空");
        }
        int result = categoryMapper.updateByPrimaryKey(category);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"种类更改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse getCategoryById(Integer categoryId) {
        // 参数非空校验
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);
        if(categoryList==null||categoryList.size()<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"数据未查询到");
        }
        return ServerResponse.serverResponseBySuccess(categoryList,"成功");
    }

    @Override
    public ServerResponse deepCategory(Integer categoryId) {
        // 判断类别id是否为空
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"类别id不能为空");
        }
        Set<Category> categorySet = Sets.newHashSet();
        Set<Category> allChildCategory = findAllChildCategory(categorySet, categoryId);
        Iterator<Category> categoryIterator = allChildCategory.iterator();
        Set<Integer> categoryIds = Sets.newHashSet();
        while(categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            categoryIds.add(category.getId());
        }
        return ServerResponse.serverResponseBySuccess(categoryIds);
    }

    @Override
    public ServerResponse selectCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"类别id不能为空");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        return ServerResponse.serverResponseBySuccess(category);
    }

    public Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        // 查看category的类别信息
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        // 查看category的平级子类
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);
        if(categoryList!=null&&categoryList.size()>0){
            for (Category category1:categoryList){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
