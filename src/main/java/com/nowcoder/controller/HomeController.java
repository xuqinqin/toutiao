package com.nowcoder.controller;

import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2017/7/4.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;


//    private List<ViewObject> getNews(int userId,int offset,int limit){
//        List<News> newsList= newsService.getLatesNews(userId,offset,limit);
//        List<ViewObject> vos=new ArrayList<>();
//        for(News news:newsList){
//            ViewObject vo=new ViewObject();
//            vo.set("news",news);
//            vo.set("user",userService.getUser(news.getUserId()));
//            vos.add(vo);
//        }
//        return vos;
//    }
    private List<HashMap> getNews(int userId,int offset,int limit){
        List<News> newsList=newsService.getLatesNews(userId,offset,limit);
        List<HashMap> vos=new ArrayList<>();
        for(News news:newsList){
            HashMap<String,Object> vo=new HashMap<>();
            vo.put("news",news);
            vo.put("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return  vos;
    }

    @RequestMapping(path = {"/qin1"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        model.addAttribute("vos",getNews(0,0,10));
        return "home";
    }

    @RequestMapping(path = {"user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }
    @RequestMapping(path = {"news/{direction}/{newsId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String likeCount(Model model, @PathVariable("newsId") int newsId,
                            @PathVariable("direction") String direction ){
        if(direction.equals("up")){
            newsService.addlikecount(newsId);
        }else {
            newsService.sublikecount(newsId);
        }
        return "redirect:/qin1";
    }

}
