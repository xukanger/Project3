package just.yt.service;

import just.yt.dao.ExamineeMapper;
import just.yt.model.Examinee;
import just.yt.model.ExamineeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by llf on 2017/3/5.
 *考生信息类的增删改查
 */
@Service("examineeService")
public class ExamineeService {


    @Resource
    ExamineeMapper examineeMapper;


    public Examinee insert(Examinee examinee){
        examineeMapper.insertSelective(examinee);
        return examinee;
    }

    public List<Examinee> getAll(){
        return examineeMapper.selectByExample(new ExamineeExample());
    }
    int deleteAll(){return examineeMapper.deleteByExample(new ExamineeExample());};
    public Examinee update(Examinee examinee){
        ExamineeExample examineeExample=new ExamineeExample();
        examineeExample.createCriteria().andIdEqualTo(examinee.getId());
        examineeMapper.updateByExampleSelective(examinee,examineeExample);
        return examinee;
    }

    public boolean delete(Long id){
        return examineeMapper.deleteByPrimaryKey(id) > 0;
    }

    public void save(Examinee examinee){
        examineeMapper.insert(examinee);
    }

    public Integer count(){
        return examineeMapper.countByExample(new ExamineeExample());
    }

    public List<Examinee> getByPage(int pageSize,int pageNo){
        ExamineeExample examineeExample=new ExamineeExample();
        examineeExample.setLimitStart((pageNo - 1) * pageSize);
        examineeExample.setLimitSize(pageSize);
        return examineeMapper.selectByExample(examineeExample);
    }

    public Examinee getById(Long id){
        return examineeMapper.selectByPrimaryKey(id);
    }

    public List<Examinee> selectByExample(ExamineeExample examineeExample){
        return examineeMapper.selectByExample(examineeExample);
    }


}
