package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ASUS on 2017/7/4.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;



    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("用户名问题","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("密码问题","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("用户名问题","该用户名已经存在");
            return map;
        }
         user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        Random random=new Random();
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);
        //注册成功，自动登录
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("用户名问题", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("密码问题", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("用户名问题", "用户名不存在");
            return map;
        }
        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }
        LoginTicket loginticket1=loginTicketDAO.selectByUserId(user.getId());
        String ticket=loginticket1.getTicket();
        loginTicketDAO.updateStatus(ticket,0);
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginticket=new LoginTicket();
        loginticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginticket.setExpired(date);
        loginticket.setStatus(0);
        loginticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginticket);
        return loginticket.getTicket();
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public int getUserIdByTicket(String ticket){
        return loginTicketDAO.selectByTicket(ticket).getUserId();
    }

    public void updateUsernp(User user){
        userDAO.updateUser(user);
    }

    public boolean checkUsername(String username){
        if(userDAO.selectByName(username)!=null) {
            return false;
        }
        return true;
    }
}
