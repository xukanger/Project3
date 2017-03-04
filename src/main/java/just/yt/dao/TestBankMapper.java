package just.yt.dao;

import java.util.List;
import just.yt.model.TestBank;
import just.yt.model.TestBankExample;
import org.apache.ibatis.annotations.Param;

public interface TestBankMapper {
    int countByExample(TestBankExample example);

    int deleteByExample(TestBankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TestBank record);

    int insertSelective(TestBank record);

    List<TestBank> selectByExample(TestBankExample example);

    TestBank selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TestBank record, @Param("example") TestBankExample example);

    int updateByExample(@Param("record") TestBank record, @Param("example") TestBankExample example);

    int updateByPrimaryKeySelective(TestBank record);

    int updateByPrimaryKey(TestBank record);
}