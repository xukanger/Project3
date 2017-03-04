package just.yt.dao;

import java.util.List;
import just.yt.model.BopsUser;
import just.yt.model.BopsUserExample;
import org.apache.ibatis.annotations.Param;

public interface BopsUserMapper {
    int countByExample(BopsUserExample example);

    int deleteByExample(BopsUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BopsUser record);

    int insertSelective(BopsUser record);

    List<BopsUser> selectByExample(BopsUserExample example);

    BopsUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BopsUser record, @Param("example") BopsUserExample example);

    int updateByExample(@Param("record") BopsUser record, @Param("example") BopsUserExample example);

    int updateByPrimaryKeySelective(BopsUser record);

    int updateByPrimaryKey(BopsUser record);
}