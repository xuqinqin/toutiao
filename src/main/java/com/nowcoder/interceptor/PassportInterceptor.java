package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                String ticket = null;
                if (httpServletRequest.getCookies() != null) {
                    for (Cookie cookie : httpServletRequest.getCookies()) {//获取所有的cookie
                        if (cookie.getName().equals("cookie")) {//cookie.getName(),返回cookie的名称
                            ticket = cookie.getValue();//返回cookie的值
                            break;
                        }
                    }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //在获取到网页向后台请求的ticket时，还要多加几个验证，因为有可能此时伪cookie，或者cookie生效的时间已经过期，
            //或者此时用户已经退出了，拦截器还放着上次拦截中存放的user,在退出状态下还自动登录上，需要将hostHolder里面的
            //内容清除，就不会自动登录了
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                hostHolder.setUser(null);
                return true;//当ticket时伪cookie,或者过期，或状态是离线的，就将hostHolder里面的东西置空，在这里return true后
                //执行拦截时，此时拦截时hostHolder里面获得user就是null,接下来所有的动作都是没有user这个用户操作的
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            //System.out.println("拦截器:name="+user.getName());
            hostHolder.setUser(user);
       }else{
            hostHolder.setUser(null);
        }
        //在这里返回true，hostHolder里面可能带着两种情况执行拦截时，当通过ticket查到该用户，或者没过期，或者是在线状态0，就
        //将这个hostHolder里的user传给拦截时，这个user用户将贯穿所有的动作
        //当压根没有这个cookie时候，ticket=null,此时hostHolder里的user就是空，接下来所有的动作都是没有user这个用户操作的
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
                modelAndView.addObject("user", hostHolder.getUser());
            }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
