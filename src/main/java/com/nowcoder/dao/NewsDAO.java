package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ASUS on 2017/7/4.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME="news";
    String INSERT_FIELDS=" title, link, image, like_count, comment_count, created_date,user_id ";
    String SELECT_FIELDS=" id, "+INSERT_FIELDS;
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where id=#{id}"})
    News selectById(int id);

    @Update({"update ", TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(News news);

    @Update({"update ", TABLE_NAME," set like_count=#{likeCount} where id=#{id}"})
    void updateLikeCount(News news);

    @Delete({"delete from ", TABLE_NAME,"where id=#{id}" })
    void deleteById(int id);

    //这个是通过xml配置文件来做的，而不是用的注解，因为select的比较复杂
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
                                       @Param("offset") int offset,@Param("limit") int limit);
}
