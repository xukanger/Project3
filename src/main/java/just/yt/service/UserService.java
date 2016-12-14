package just.yt.service;

import just.yt.dao.TUserMapper;
import just.yt.model.TUser;
import just.yt.model.TUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yt on 2016/12/13.
 */
@Service("userService")
public class UserService {

    @Resource
    TUserMapper tUserMapper;

    public TUser getUser(){
        TUserExample tUserExample=new TUserExample();
        tUserExample.createCriteria().andIdEqualTo(1);
        return tUserMapper.selectByExample(tUserExample).get(0);

    }


}
