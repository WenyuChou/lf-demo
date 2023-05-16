package com.example.demo.mapper;
import com.example.demo.pojo.GrantSyncLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-04-12 15:30:40
 *
 */
@Mapper
public interface GrantSyncLogMapper{
    long insert(GrantSyncLog param);
    int deleteById(Long id);
    int updateById(GrantSyncLog param);
    List<GrantSyncLog> selectListByParams(GrantSyncLog param);
    List<GrantSyncLog> selectByIds(@Param("ids")Set<Long> ids);
    GrantSyncLog selectByObject(GrantSyncLog param);
    GrantSyncLog selectById(Long id);

}