package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by ASUS on 2017/7/13.
 */
@Controller
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path={"/mustlogin"})//定义入口
    public String mustlogin(Model model){
        //   logger.info("在这里也可以打log");
        model.addAttribute("error","温馨提示：你需要登录后才能看这里哦！");
        return "mustlogin";
    }
    @RequestMapping(path={"/qinqin"})//定义入口
    //@ResponseBody//需要在页面返回字符串时候，需要用RespnseBody这个注解，当需要返回一个页面时候，这个注解不能要
    public String index(){
        //   logger.info("在这里也可以打log");
        return "createNews";
    }
    @RequestMapping(path={"/addnews"},method = {RequestMethod.POST})//定义入口
    public String addnews(Model model, @RequestParam("title") String title,
                          @RequestParam("link") String link,
                          @RequestParam("file") MultipartFile file,Model mode){
        User user=hostHolder.getUser();
        int userId=user.getId();
        try {
            String fileUrl=newsService.saveImage(file);
            if(fileUrl==null) {
                model.addAttribute("error", "上传失败");
                return "mustlogin";
            }
            newsService.addNewsByUserId(userId,title,link,fileUrl);

        }catch (Exception e){
            return  "error";
        }

       return "createNews";
    }
//    @RequestMapping(path = {"/uploadImage"},method = {RequestMethod.POST})
//    public String uploadImage(@RequestParam("file") MultipartFile file,Model model){
//        try {
//            String fileUrl=newsService.saveImage(file);
//            if(fileUrl==null){
//                model.addAttribute("error","上传失败");
//                return "mustlogin";
//            }
//            model.addAttribute("correct",fileUrl);
//
//            return "test";
//        }catch (Exception e){
//            return  "error";
//        }
//    }
    @RequestMapping(path = {"/imges"})
    @ResponseBody
    public void getImage(@PathVariable("newsId") int newsId,
                           HttpServletResponse response){
        try {
            String fileName=newsService.getNewsImage(newsId);
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+fileName)),
                    response.getOutputStream());
        }catch (Exception e){

        }

    }

}
