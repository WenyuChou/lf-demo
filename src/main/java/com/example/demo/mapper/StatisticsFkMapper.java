package com.example.demo.mapper;
import com.example.demo.pojo.StatisticsFk;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-05-16 10:27:46
 *
 */
@Mapper
public interface StatisticsFkMapper{
    long insert(StatisticsFk param);
    int deleteById(Long id);
    int updateById(StatisticsFk param);
    List<StatisticsFk> selectListByParams(StatisticsFk param);
    List<StatisticsFk> selectByIds(@Param("ids")Set<Long> ids);
    StatisticsFk selectByObject(StatisticsFk param);
    StatisticsFk selectById(Long id);

}