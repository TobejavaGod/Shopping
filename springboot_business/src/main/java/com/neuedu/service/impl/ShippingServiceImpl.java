package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
