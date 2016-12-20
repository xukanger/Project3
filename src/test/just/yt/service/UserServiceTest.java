package just.yt.service;

import just.yt.model.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by yt on 2016/12/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void getUser() throws Exception {
        TUser user=new TUser();
        user.setName("test");
        user.setEmail("test@test.com");
        user.setPassword("test");
        System.out.println(userService.insert(user));
    }

}