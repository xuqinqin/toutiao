package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by ASUS on 2017/7/5.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path={"/reg"},method={RequestMethod.GET,RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("cookie",map.get("ticket").toString());
                cookie.setPath("/");//定义这个cookie全局有效
//                if(remember>0){
//                    cookie.setMaxAge(3600*24*5);
//                }
                response.addCookie(cookie);
                return "redirect:/qin1";
            }else {
               model.addAttribute("error",map);
                //return "redirect:/qin1";
                return "error";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "error";
        }
    }
    @RequestMapping(path={"/log"},method={RequestMethod.GET,RequestMethod.POST})
    public String log(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("cookie",map.get("ticket").toString());
                cookie.setPath("/");//定义这个cookie全局有效

//                if(remember>0){
//                    cookie.setMaxAge(3600*24*1);
//                }
                response.addCookie(cookie);
                return "redirect:/qin1";
            }else {
                model.addAttribute("error",map);
                return "error";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "error";
        }
    }
    @RequestMapping(path={"/logout"},method ={RequestMethod.GET,RequestMethod.POST} )
    public String logu(@CookieValue("cookie") String cookie){
        userService.logout(cookie);
        return "redirect:/qin1";
    }
    @RequestMapping(path = {"/changemsg"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model){
        User user=hostHolder.getUser();
        model.addAttribute("user",user);
        return "npass";
    }

    @RequestMapping(path={"/changenp"},method = {RequestMethod.POST})
    public String changeusername(@RequestParam("username")String username,
                                 @RequestParam("password")String password,Model model) {
        User user=hostHolder.getUser();

        //是否修改用户名
        //if(username!=""){
        if(!StringUtils.isBlank(username)){
            if(userService.checkUsername(username)||user.getName().equals(username)) {
                user.setName(username);
            }else {
                model.addAttribute("msg","该用户名存在，请重新输入");
                return "npass";
            }
        }
        //是否修改密码
        //if(password!=""){
        if(!StringUtils.isBlank(password)){
            user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        }
        //提交修改
        userService.updateUsernp(user);
        hostHolder.setUser(user);
        model.addAttribute("msg","修改成功");
        return "npass";
    }
}
