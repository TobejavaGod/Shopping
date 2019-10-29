package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.util.MD5Utils;
import com.neuedu.util.TokenCache;
import com.neuedu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author jyw
 * @date 2019/10/24-15:13
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse register(User user){
        //参数校验
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        //判断用户名是否存在
        int existUsername = userMapper.isExistUsername(user.getUsername());
        if(existUsername>0){
            return ServerResponse.serverResponseByError(ResponseCode.USERNAME_EXIST,"用户名已存在");
        }
        //判断邮箱是否存在
        int existEmail = userMapper.isExistEmail(user.getEmail());
        if(existEmail>0){
            return ServerResponse.serverResponseByError(ResponseCode.EMAIL_EXIST,"邮箱已存在");
        }
        //使用MD5对密码进行加密，设置用户角色
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        // 设置角色为普通用户
        user.setRole(RoleEnum.ROLE_USER.getRole());
        //注册
        int insertResult = userMapper.insert(user);
        if(insertResult<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"注册失败");
        }
        //返回
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse login(String username, String password,int type) {
        // 参数非空校验
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(password==null||"".equals(password)){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码不能为空");
        }
        // 判断用户名是否存在
        int existUsername = userMapper.isExistUsername(username);
        if(existUsername<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不存在");
        }
        // 根据用户名和密码登陆
        password = MD5Utils.getMD5Code(password);
        User user = userMapper.findUserByUsernameAndPassword(username, password);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误");
        }
        if(type==0){ // 管理员
            if(user.getRole()==RoleEnum.ROLE_USER.getRole()){ // 没有管理权限
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
            }
        }
        return ServerResponse.serverResponseBySuccess(user,"登陆成功");
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        // 参数非空校验
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"用户名不能为空");
        }
        // 根据用户名查询问题
        int existUsername = userMapper.isExistUsername(username);
        if(existUsername<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不存在");
        }
        String question = userMapper.forget_get_question(username);
        if(question==null||"".equals(question)){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有查询到密保问题");
        }
        // 返回结果
        return ServerResponse.serverResponseBySuccess(question);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        // 参数非空校验
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"用户名不能为空");
        }
        if(question==null||"".equals(question)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"密保问题不能为空");
        }
        if(answer==null||"".equals(answer)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"答案不能为空");
        }
        // 校验问题与答案是否匹配
        int result = userMapper.forget_check_answer(username, question, answer);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案错误");
        }
        // 生成token
        String token = UUID.randomUUID().toString();
        TokenCache.set("username:"+username, token);
        // 返回结果
        return ServerResponse.serverResponseBySuccess(token);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String newpassword, String forgettoken) {
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"用户名不能为空");
        }
        if(newpassword==null||"".equals(newpassword)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"新密码不能为空");
        }
        if(forgettoken==null||"".equals(forgettoken)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"token不能为空");
        }
        // 是否修改的是自己的密码
        String token = TokenCache.get("username:" + username);
        if(token==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"不能修改别人的密码或token已过期");
        }
        if(!token.equals(forgettoken)){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"无效的token");
        }
        int result = userMapper.forget_reset_password(username, MD5Utils.getMD5Code(newpassword));
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码修改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse update_information(User user) {
        // 参数非空校验
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        int result = userMapper.updateUserByActivate(user);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        // 参数非空校验
        if(str==null||"".equals(str)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        if(type==null||"".equals(type)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        if(type.equals("username")){
            // 检查用户名是否存在
            int resultUser = userMapper.isExistUsername(str);
            if(resultUser!=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名已存在");
            }
        }
        if(type.equals("email")){
            // 检查邮箱是否存在
            int resultEmail = userMapper.isExistEmail(str);
            if(resultEmail!=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"邮箱已存在");
            }
        }
        return ServerResponse.serverResponseBySuccess("校验成功");
    }

    @Override
    public ServerResponse get_user_info(String  username) {
        User user = userMapper.findUserByUsername(username);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"查询失败");
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(username);
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        return ServerResponse.serverResponseBySuccess(userVO);
    }

    @Override
    public ServerResponse reset_password(String oldPassword, String newPassword,Integer userId) {
        // 参数非空判断
        if(oldPassword==null||"".equals(oldPassword)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        if(newPassword==null||"".equals(newPassword)){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if(!MD5Utils.getMD5Code(oldPassword).equals(user.getPassword())){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"旧密码输入有误");
        }
        int result = userMapper.resetPasswordById(userId, MD5Utils.getMD5Code(newPassword));
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServerResponse.serverResponseBySuccess("修改密码成功");
    }

    /**
     * 获取当前用户的详细信息
     * @param userId
     * @return
     */
    @Override
    public ServerResponse get_information(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return ServerResponse.serverResponseBySuccess(user);
    }

    @Override
    public ServerResponse listUsers(Integer pageSize, Integer pageNum,int role) {
        if(role==RoleEnum.ROLE_USER.getRole()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        }
        Page page = PageHelper.startPage(pageNum,pageSize);
        List<User> users = userMapper.selectAll();
        if(users==null||users.size()<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"查询失败");
        }
        PageInfo pageInfo = new PageInfo(users);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
