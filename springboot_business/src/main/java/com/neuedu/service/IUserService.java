package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author jyw
 * @date 2019/10/23-18:02
 */
public interface IUserService {

    /**
     * 用户注册接口
     * @param user
     * @return
     */
    ServerResponse register(User user);

    /**
     * 用户登陆接口
     * @param username
     * @param password
     * @param type 1:普通用户  0:管理员
     * @return
     */
    ServerResponse login(String username,String password,int type);

    /**
     * 根据用户名获取密保问题接口
     * @param username
     * @return
     */
    ServerResponse forget_get_question(String username);

    /**
     * 校验问题与答案接口
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse forget_check_answer(String username, String question, String answer);

    /**
     * 重置密码接口
     * @param username
     * @param newpassword
     * @param forgettoken
     * @return
     */
    ServerResponse forget_reset_password(String username, String newpassword, String forgettoken);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    ServerResponse update_information(User user);
}
