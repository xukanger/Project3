package just.yt.service;

import just.yt.dao.TestMarkMapper;
import just.yt.model.TestMark;
import just.yt.model.TestMarkExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("testMarkService")
public class TestMarkService {

    @Resource
    TestMarkMapper testMarkMapper;


    public TestMark insert(TestMark testMark){
        testMarkMapper.insertSelective(testMark);
        return testMark;
    }

    public List<TestMark> getAll(){
        return testMarkMapper.selectByExample(new TestMarkExample());
    }
    int deleteAll(){return testMarkMapper.deleteByExample(new  TestMarkExample());};
    public TestMark update(TestMark testMark){
        TestMarkExample TestMarkExample=new TestMarkExample();
        TestMarkExample.createCriteria().andIdEqualTo(testMark.getId());
        testMarkMapper.updateByExampleSelective(testMark,TestMarkExample);
        return testMark;
    }

    public boolean delete(Long id){
        return testMarkMapper.deleteByPrimaryKey(id) > 0;
    }

    public void save(TestMark testMark){
        testMarkMapper.insert(testMark);
    }

    public Integer count(){
        return testMarkMapper.countByExample(new TestMarkExample());
    }

    public List<TestMark> getByPage(int pageSize,int pageNo){
        TestMarkExample TestMarkExample=new TestMarkExample();
        TestMarkExample.setLimitStart((pageNo - 1) * pageSize);
        TestMarkExample.setLimitSize(pageSize);
        return testMarkMapper.selectByExample(TestMarkExample);
    }

    public TestMark getById(Long id){
        return testMarkMapper.selectByPrimaryKey(id);
    }

    public List<TestMark> selectByExample(TestMarkExample testMarkExample){
        return testMarkMapper.selectByExample(testMarkExample);
    }

}