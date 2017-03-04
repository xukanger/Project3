package just.yt.dao;

import java.util.List;
import just.yt.model.TestMark;
import just.yt.model.TestMarkExample;
import org.apache.ibatis.annotations.Param;

public interface TestMarkMapper {
    int countByExample(TestMarkExample example);

    int deleteByExample(TestMarkExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TestMark record);

    int insertSelective(TestMark record);

    List<TestMark> selectByExampleWithBLOBs(TestMarkExample example);

    List<TestMark> selectByExample(TestMarkExample example);

    TestMark selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TestMark record, @Param("example") TestMarkExample example);

    int updateByExampleWithBLOBs(@Param("record") TestMark record, @Param("example") TestMarkExample example);

    int updateByExample(@Param("record") TestMark record, @Param("example") TestMarkExample example);

    int updateByPrimaryKeySelective(TestMark record);

    int updateByPrimaryKeyWithBLOBs(TestMark record);

    int updateByPrimaryKey(TestMark record);
}