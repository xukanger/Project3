package just.yt.service;

import just.yt.dao.BopsUserMapper;
import just.yt.model.*;
import org.apache.commons.logging.impl.Log4JLogger;
import org.springframework.stereotype.Service;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tool.DefaultResult;
import tool.ExcelUtil;


import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by yt on 2017/3/4.
 */
@Service("bopsService")
public class BopsService {
    @Resource
    BopsUserMapper bopsUserMapper;
    @Resource
    ExamineeService examineeService;
    @Resource
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

    public DefaultResult<BopsUser> login(String account, String password){
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
            return DefaultResult.successResult(user);
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
    public DefaultResult importExaminee(MultipartFile file){
        List<Examinee> examinees = new ArrayList<Examinee>();
        List<TestMark> testMarks = new ArrayList<TestMark>();
        Date now = new Date();
        List<List<Map<String, String>>> initData;
        try {
            initData = ExcelUtil.readExcelWithTitle(file);
        }catch (Exception e){
            e.printStackTrace();
            return  DefaultResult.failResult("导入失败");
        }
        List<Map<String, String>> excel = initData.get(0);

        for (Map<String, String> row:excel) {
            String name = row.get("姓名");
            String identity = row.get("身份证号码");
            String position = row.get("报考职位");
            if (StringUtils.isEmpty(identity)||StringUtils.isEmpty(name)){
//                return  DefaultResult.failResult("有记录的身份证或姓名列为空！");
                continue;
            }
            Examinee e = new Examinee(now,now,identity,name,position);
            examinees.add(e);
            TestMark testMark = new TestMark(now,now,identity,name);
            testMark.setType("A");
            testMarks.add(testMark);
        }
        for (Examinee e: examinees) {
            examineeService.insert(e);
        }
        for (TestMark t:testMarks) {
            testMarkService.insert(t);
        }
        return  DefaultResult.successResult("导入成功");
    }

    /**
     * 导入考生成绩
     * */

    /**
     * 导出考生成绩
     * */
    public  DefaultResult outputTestMarks(String type, OutputStream os){
        if (StringUtils.isEmpty(type)){
            return null;
        }
        Boolean isB = false;
        //原始数据
        TestMarkExample testMarkExample = new TestMarkExample();
        List<TestMark> marks = null;
        List<TestMark> Amarks = new ArrayList<TestMark>();
        List<TestMark> Bmarks = new ArrayList<TestMark>();
        if (type.equals("A")) {
            testMarkExample.createCriteria().andTypeEqualTo(type);
            marks = testMarkService.selectByExamlpe(testMarkExample);
            Amarks= marks;
        }else {
            isB = true;
            marks = testMarkService.getAll();
            for(TestMark mark:marks){
                if (mark.getType().equals("A")){
                    Amarks.add(mark);
                }else {
                    Bmarks.add(mark);
                }
            }
        }

        List<String> cells = new ArrayList<String>();
            cells.add("学生姓名");
            cells.add("身份证号");
            cells.add("初试题号");
            cells.add("初试考生答题内容");
        if (isB) {
            cells.add("复试题号");
            cells.add("复试考生答题内容");
        }

        List<List<String>> rows = new ArrayList<List<String>>();
        rows.add(cells);
        if (!isB){
            for (TestMark mark:marks) {
                cells = new ArrayList<String>();
                cells.add(mark.getName());
                cells.add(mark.getIdentity());
                cells.add(String.valueOf(mark.getNum()));
                cells.add(mark.getContent());
                rows.add(cells);
            }
        }else {
            for (TestMark bmark:Bmarks){
                TestMark  amark =  getByIdentity(Amarks,bmark.getIdentity());
                cells = new ArrayList<String>();
                cells.add(bmark.getName());
                cells.add(bmark.getIdentity());
                cells.add(amark.getNum()+"");
                cells.add(amark.getContent());
                cells.add(bmark.getNum()+"");
                cells.add(bmark.getContent());
                rows.add(cells);
            }
        }

        Map<String,List<List<String>>> data = new HashMap<String, List<List<String>>>();
        if (!isB){
            data.put("初试考生信息",rows);
        }else {
            data.put("复试考生信息",rows);
        }

        try {
            ExcelUtil.writeExcel(os,"xls",data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  DefaultResult.successResult("下载成功");
    }

    /**
     *导出考生答题内容（初试/复试）
     * */
    public  DefaultResult outputContent(String type){
        TestMarkExample testMarkExample = new TestMarkExample();
        testMarkExample.createCriteria().andTypeEqualTo(type);
        List<TestMark> marks = testMarkService.selectByExamlpe(testMarkExample);
        Map<String,String> contents = new HashMap<String, String>();
        FileOutputStream out = null;
        for (TestMark mark:marks) {
            contents.put(mark.getIdentity(),mark.getContent());
        }
        try {
            for (Map.Entry<String, String> entry : contents.entrySet()) {
                out = new FileOutputStream("A//"+entry.getKey()+".txt");
                out.write(entry.getValue().getBytes());
                out.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            return DefaultResult.failResult("下载失败");
        }
        return  DefaultResult.successResult("下载成功");
    }

    private TestMark getByIdentity(List<TestMark> list,String identity){
        for (TestMark l:list){
            if (identity.equals(l.getIdentity()))
                return  l;
        }
        return  null;
    }
}
