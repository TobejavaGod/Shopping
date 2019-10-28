package com.neuedu.controller.front;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author jyw
 * @date 2019/10/22-20:12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * 注册接口
     */
    @RequestMapping("/register")
    public ServerResponse register(User user){


        return userService.register(user);
    }

    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @param session 会话域对象
     * @return
     */
    @RequestMapping("/login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse = userService.login(username, password,1);
        if(serverResponse.isSuccess()){
            // 登陆成功后将用户信息加入到会话域中
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 根据用户名获取密保问题
     * @param username
     * @return
     */
    @RequestMapping("/forget_get_question/{username}")
    public ServerResponse forget_get_question(@PathVariable("username") String username){

        return userService.forget_get_question(username);
    }

    /**
     * 检验答案与问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping("/forget_check_answer.do")
    public ServerResponse forget_check_answer(String username,String question,String answer){
        return userService.forget_check_answer(username, question, answer);
    }

    /**
     * 修改密码
     * @param username
     * @param newpassword 新密码
     * @param forgettoken 越权
     * @return
     */
    @RequestMapping("/forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,String newpassword,String forgettoken){
        return userService.forget_reset_password(username, newpassword, forgettoken);
    }

    @RequestMapping("/update_information.do")
    public ServerResponse update_information(User user,HttpSession session){
        User loginUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(loginUser==null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登录");
        }
        user.setId(loginUser.getId());
        return userService.update_information(user);

    }
}
