package com.example.demo.mapper;
import com.example.demo.pojo.GrantBudgetDetailLh;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-03 11:12:42
 *
 */
@Mapper
public interface GrantBudgetDetailLhMapper{
    long insert(GrantBudgetDetailLh param);
    int deleteById(Long id);
    int updateById(GrantBudgetDetailLh param);
    List<GrantBudgetDetailLh> selectListByParams(GrantBudgetDetailLh param);
    List<GrantBudgetDetailLh> selectByIds(@Param("ids")Set<Long> ids);
    GrantBudgetDetailLh selectByObject(GrantBudgetDetailLh param);
    GrantBudgetDetailLh selectById(Long id);

}