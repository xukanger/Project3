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
        int id=tUserMapper.insertSelective(user);
        TUserExample tUserExample=new TUserExample();
        System.out.println(user.getId());
        tUserExample.createCriteria().andIdEqualTo(id);
        //return tUserMapper.selectByExample(tUserExample).get(0);
        return user;
    }



}
