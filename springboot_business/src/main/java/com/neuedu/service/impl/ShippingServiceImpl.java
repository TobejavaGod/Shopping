package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jyw
 * @date 2019/10/28-8:33
 */
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Shipping shipping) {
        // 参数非空校验
        if (shipping == null) {
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL, "参数不能为空");
        }
        Integer id = shipping.getId();
        if (id == null) {
            // 添加
            int resultAdd = shippingMapper.insert(shipping);
            if (resultAdd <= 0) {
                return ServerResponse.serverResponseByError(ResponseCode.ERROR, "添加失败");
            } else {
                return ServerResponse.serverResponseBySuccess(shipping.getId());
            }
        } else {
            //更新
            int resultUpdate = shippingMapper.updateByPrimaryKey(shipping);
            if (resultUpdate<=0) {
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败");
            }else{
                return ServerResponse.serverResponseBySuccess(shipping.getId());
            }
        }
    }

    @Override
    public ServerResponse findShippingById(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"收货地址不存在");
        }
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse delete(Integer shippingId) {
        // 参数非空校验
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"地址id不能为空");
        }
        int result = shippingMapper.deleteByPrimaryKey(shippingId);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"删除失败");
        }
        return ServerResponse.serverResponseBySuccess("删除地址成功");
    }

    @Override
    public ServerResponse selectShipping(Integer shippingId) {
        // 参数非空校验
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"该地址不存在");
        }
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse listAllShipping(Integer pageNum, Integer pageSize, Integer userId) {
        Page page = PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.listShippingByUserId(userId);
        if(shippingList==null || shippingList.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"地址不存在");
        }
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
