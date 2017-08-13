package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2017/7/3.
 */
@Controller
//测试用到的controller层
public class IndexController {
   // private static final Logger logger= LoggerFactory.getLogger(IndexController.class);


    //http://localhost:8080/或者qinqin//http://localhost:8080/qinqin
    //@RequestMapping(path={"/","/qinqin"})//定义入口


    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId")   int userId,
                          @RequestParam(value = "type", defaultValue="1") int type,
                          @RequestParam(value="key",defaultValue="xiaoqinqin")String key
                          ){
        //http://localhost:8080/profile/hah/3
        //http://localhost:8080/profile/hah/3?key=ff&type=33
        return String.format("groupId{%s},userId{%d},type{%d},key{%s}",groupId,userId,type,key);
    }
    //http://localhost:8080/profile/vm
    @RequestMapping(path={"/vm"})
    public String newway(Model model){
        model.addAttribute("value1","japan");
        List<String> nochange= Arrays.asList("你好","开心");
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));//基本类型转换成String变量
        }
        model.addAttribute("nochange",nochange);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("花湖"));
        return "news";//news.vm
    }

}
