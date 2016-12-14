package just.yt.dao;

import java.util.List;
import just.yt.model.TCommodity;
import just.yt.model.TCommodityExample;
import org.apache.ibatis.annotations.Param;

public interface TCommodityMapper {
    int countByExample(TCommodityExample example);

    int deleteByExample(TCommodityExample example);

    int insert(TCommodity record);

    int insertSelective(TCommodity record);

    List<TCommodity> selectByExample(TCommodityExample example);

    int updateByExampleSelective(@Param("record") TCommodity record, @Param("example") TCommodityExample example);

    int updateByExample(@Param("record") TCommodity record, @Param("example") TCommodityExample example);
}