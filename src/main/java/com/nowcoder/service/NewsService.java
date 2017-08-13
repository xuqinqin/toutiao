package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by ASUS on 2017/7/4.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatesNews(int userId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    public void addlikecount(int newsId){
        News news=newsDAO.selectById(newsId);
        news.setLikeCount(news.getLikeCount()+1);
        newsDAO.updateLikeCount(news);
    }
    public void sublikecount(int newsId){
        News news=newsDAO.selectById(newsId);
        news.setLikeCount(news.getLikeCount()-1);
        if(news.getLikeCount()<0){
            news.setLikeCount(0);
        }
        newsDAO.updateLikeCount(news);
    }
    public void addNewsByUserId(int userId,String title,String link,String fileUrl){
       News news=new News();
        news.setTitle(title);
        news.setLink(link);
        news.setLikeCount(0);
        news.setCommentCount(0);
        news.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime());
        news.setCreatedDate(date);
        Random random=new Random();
       // news.setImage(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        news.setImage(fileUrl);
        newsDAO.addNews(news);
    }
    public String saveImage(MultipartFile file) throws IOException{
        //aaa.jpeg
        int dotPos=file.getOriginalFilename().lastIndexOf(".");//最后一次出现.的位置
        if(dotPos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName= UUID.randomUUID().toString().replace("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
       // return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
//        return ToutiaoUtil.IMAGE_DIR+fileName;
        return  fileName;
    }
    public String getNewsImage(int newsId){
        News news=newsDAO.selectById(newsId);
        String image=news.getImage();
        return image;
    }
 }
