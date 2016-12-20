package just.yt.dao;

import java.util.List;
import just.yt.model.TShoppinglist;
import just.yt.model.TShoppinglistExample;
import org.apache.ibatis.annotations.Param;

public interface TShoppinglistMapper {
    int countByExample(TShoppinglistExample example);

    int deleteByExample(TShoppinglistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TShoppinglist record);

    int insertSelective(TShoppinglist record);

    List<TShoppinglist> selectByExample(TShoppinglistExample example);

    TShoppinglist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TShoppinglist record, @Param("example") TShoppinglistExample example);

    int updateByExample(@Param("record") TShoppinglist record, @Param("example") TShoppinglistExample example);

    int updateByPrimaryKeySelective(TShoppinglist record);

    int updateByPrimaryKey(TShoppinglist record);
}