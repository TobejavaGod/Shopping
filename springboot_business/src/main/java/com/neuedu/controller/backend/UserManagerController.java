package com.neuedu.controller.backend;

import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUserService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 管理员接口
 * @author jyw
 * @date 2019/10/25-17:17
 */
@RestController
@RequestMapping("/manager")
public class UserManagerController {

    @Autowired
    IUserService userService;

    @RequestMapping("/login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse = userService.login(username, password, RoleEnum.ROLE_ADMIN.getRole());
        if(serverResponse.isSuccess()){
            // 登陆成功后将用户信息加入到会话域中
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }
}
