package com.neuedu.interceptor;

import com.neuedu.pojo.User;
import com.neuedu.util.Const;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义登陆拦截器
 * @author jyw
 * @date 2019/10/27-17:49
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 到达controller之前的动作
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        System.out.println("拦截器执行了");
        if(user==null){
            return false;
        }else{
            return true;
        }
    }
}
