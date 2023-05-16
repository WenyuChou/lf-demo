package com.example.demo.mapper;
import com.example.demo.pojo.GrantBudgetMain;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-03 11:12:41
 *
 */
@Mapper
public interface GrantBudgetMainMapper{
    long insert(GrantBudgetMain param);
    int deleteById(Long id);
    int updateById(GrantBudgetMain param);
    List<GrantBudgetMain> selectListByParams(GrantBudgetMain param);
    List<GrantBudgetMain> selectByIds(@Param("ids")Set<Long> ids);
    GrantBudgetMain selectByObject(GrantBudgetMain param);
    GrantBudgetMain selectById(Long id);

}