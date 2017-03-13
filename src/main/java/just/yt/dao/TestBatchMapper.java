package just.yt.dao;

import just.yt.model.TestBatch;
import just.yt.model.TestBatchExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestBatchMapper {
    int countByExample(TestBatchExample example);

    int deleteByExample(TestBatchExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TestBatch record);

    int insertSelective(TestBatch record);

    List<TestBatch> selectByExample(TestBatchExample example);

    TestBatch selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TestBatch record, @Param("example") TestBatchExample example);

    int updateByExample(@Param("record") TestBatch record, @Param("example") TestBatchExample example);

    int updateByPrimaryKeySelective(TestBatch record);

    int updateByPrimaryKey(TestBatch record);
}