package com.example.demo.mapper;
import com.example.demo.pojo.PkgLimitNotify;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-09 17:52:02
 *
 */
@Mapper
public interface PkgLimitNotifyMapper{
    long insert(PkgLimitNotify param);
    int deleteById(Long id);
    int updateById(PkgLimitNotify param);
    List<PkgLimitNotify> selectListByParams(PkgLimitNotify param);
    List<PkgLimitNotify> selectByIds(@Param("ids")Set<Long> ids);
    PkgLimitNotify selectByObject(PkgLimitNotify param);
    PkgLimitNotify selectById(Long id);

}