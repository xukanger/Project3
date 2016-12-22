package just.yt.service;

import just.yt.dao.TUserMapper;
import just.yt.model.TUser;
import just.yt.model.TUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yt on 2016/12/13.
 */
@Service("userService")
public class UserService {

    @Resource
    TUserMapper tUserMapper;

    public TUser getUser(String name,String password){
        TUserExample tUserExample=new TUserExample();
        tUserExample.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
        List<TUser>list=tUserMapper.selectByExample(tUserExample);
        if(list.isEmpty())return null;
        return list.get(0);
    }

    public TUser insert(TUser user){
        tUserMapper.insertSelective(user);
        return user;
    }

    public List<TUser> getAll(){
        return tUserMapper.selectByExample(new TUserExample());
    }

    public TUser update(TUser user){
        TUserExample tUserExample=new TUserExample();
        tUserExample.createCriteria().andIdEqualTo(user.getId());
        tUserMapper.updateByExampleSelective(user,tUserExample);
        return user;
    }

    public void delete(Integer id){
        tUserMapper.deleteByPrimaryKey(id);
    }

    public void save(TUser user){
        tUserMapper.insert(user);
    }


    public Integer count(){
        return tUserMapper.countByExample(new TUserExample());
    }

    public List<TUser> getByPage(int pageSize,int pageNo){
        TUserExample tUserExample=new TUserExample();
        tUserExample.setLimitStart((pageNo - 1) * pageSize);
        tUserExample.setLimitSize(pageSize);
        return tUserMapper.selectByExample(tUserExample);
    }

    public TUser getById(Integer id){
        return tUserMapper.selectByPrimaryKey(id);
    }


}
