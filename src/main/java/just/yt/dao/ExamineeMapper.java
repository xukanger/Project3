package just.yt.dao;

import java.util.List;
import just.yt.model.Examinee;
import just.yt.model.ExamineeExample;
import org.apache.ibatis.annotations.Param;

public interface ExamineeMapper {
    int countByExample(ExamineeExample example);

    int deleteByExample(ExamineeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Examinee record);

    int insertSelective(Examinee record);

    List<Examinee> selectByExample(ExamineeExample example);

    Examinee selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Examinee record, @Param("example") ExamineeExample example);

    int updateByExample(@Param("record") Examinee record, @Param("example") ExamineeExample example);

    int updateByPrimaryKeySelective(Examinee record);

    int updateByPrimaryKey(Examinee record);
}