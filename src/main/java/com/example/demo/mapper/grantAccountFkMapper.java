package com.example.demo.mapper;
import com.example.demo.pojo.grantAccountFk;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-02-10 10:26:46
 *
 */
@Mapper
public interface grantAccountFkMapper{
    long insert(grantAccountFk param);
    int deleteById(Long id);
    int updateById(grantAccountFk param);
    List<grantAccountFk> selectListByParam(grantAccountFk param);
    List<grantAccountFk> selectByIds(@Param("ids")Set<Long> ids);
    grantAccountFk selectOneByParam(grantAccountFk param);
    grantAccountFk selectById(Long id);

}