package just.yt.service;

import just.yt.dao.BopsUserMapper;
import just.yt.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tool.DefaultResult;
import tool.ExcelUtil;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by yt on 2017/3/4.
 */
@Service("bopsService")
public class BopsService {
    @Resource
    BopsUserMapper bopsUserMapper;
    ExamineeService examineeService;
    TestMarkService testMarkService;


    public BopsUser insert(BopsUser bopsUser){
        bopsUserMapper.insertSelective(bopsUser);
        return bopsUser;
    }

    public List<BopsUser> getAll(){
        return bopsUserMapper.selectByExample(new BopsUserExample());
    }

    public BopsUser update(BopsUser bopsUser){
        BopsUserExample bopsUserExample=new BopsUserExample();
        bopsUserExample.createCriteria().andIdEqualTo(bopsUser.getId());
        bopsUserMapper.updateByExampleSelective(bopsUser,bopsUserExample);
        return bopsUser;
    }

    public boolean delete(Long id){
        return bopsUserMapper.deleteByPrimaryKey(id) > 0;
    }

    public void save(BopsUser bopsUser){
        bopsUserMapper.insert(bopsUser);
    }

    public Integer count(){
        return bopsUserMapper.countByExample(new BopsUserExample());
    }

    public List<BopsUser> getByPage(int pageSize,int pageNo){
        BopsUserExample bopsUserExample=new BopsUserExample();
        bopsUserExample.setLimitStart((pageNo - 1) * pageSize);
        bopsUserExample.setLimitSize(pageSize);
        return bopsUserMapper.selectByExample(bopsUserExample);
    }

    public BopsUser getById(Long id){
        return bopsUserMapper.selectByPrimaryKey(id);
    }

    public DefaultResult login(String account, String password){
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return  DefaultResult.failResult("输入为空");
        }
        BopsUserExample bopsUserExample=new BopsUserExample();
        bopsUserExample.createCriteria().andStatusEqualTo(0).andAccountEqualTo(account);
        List<BopsUser> users= bopsUserMapper.selectByExample(bopsUserExample);
        if (users.isEmpty()){
            return  DefaultResult.failResult("无此账号");
        }
        if (users.size()>1){
            return  DefaultResult.failResult("该账号数据存在异常，请联系技术人员！");
        }

        BopsUser user = users.get(0);
        if (user.getPassword().equals(password)) {
            return DefaultResult.successResult("登陆成功");
        }else {
            return DefaultResult.failResult("密码错误");
        }
    }
    /**
     * 查询考生信息
     * */
    public List<Examinee> listExaminee(String identity){
        if (StringUtils.isEmpty(identity)){
            return  examineeService.getAll();
        }
        ExamineeExample examineeExample = new ExamineeExample();
        examineeExample.createCriteria().andIdentityEqualTo(identity);
        return  examineeService.selectByExamlpe(examineeExample);
    }

    /**
     * 查询某个考生的成绩
     * */
    public List<TestMark> listTestMark(String identity){
        if (StringUtils.isEmpty(identity)){
            return  testMarkService.getAll();
        }
        TestMarkExample testMarkExample = new TestMarkExample();
        testMarkExample.createCriteria().andIdentityEqualTo(identity);
        return  testMarkService.selectByExamlpe(testMarkExample);
    }

    /**
     * 导入考生信息
     * */
    public DefaultResult importExaminee(String filepath){
        try {
            List<List<Map<String, String>>> initData = ExcelUtil.readExcelWithTitle(filepath);
        }catch (Exception e){
            e.printStackTrace();
            return  DefaultResult.failResult("导入失败");
        }
        return  null;
    }

    /**
     * 导出考生成绩
     * */
    public  DefaultResult outputTestMark(){
        return  null;
    }

    /**
     *导出考生答题内容（初试/复试）
     * */
    public  DefaultResult outputConternt(String type){
        return  null;
    }
}
