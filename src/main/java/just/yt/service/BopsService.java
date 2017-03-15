package just.yt.service;

import just.yt.dao.BopsUserMapper;
import just.yt.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tool.DefaultResult;
import tool.ExcelUtil;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        return  examineeService.selectByExample(examineeExample);
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
        return  testMarkService.selectByExample(testMarkExample);
    }

    /**
     * 导入考生信息
     * */
    public DefaultResult importExaminee(MultipartFile file){
        List<Examinee> examinees = new ArrayList<Examinee>();
        Date now = new Date();
        List<List<Map<String, String>>> initData;
        try {
            initData = ExcelUtil.readExcelWithTitle(file);
        }catch (Exception e){
            e.printStackTrace();
            return  DefaultResult.failResult("导入失败");
        }
        List<Map<String, String>> excel = initData.get(0);
        List<Examinee> old = listExaminee();
        if (old == null){
            old = new ArrayList<Examinee>();
        }

        for (Map<String, String> row:excel) {
            String name = row.get("姓名");
            String identity = row.get("身份证号码");
            String position = row.get("报考职位");
//            String batch = row.get("考试批次");
            if (StringUtils.isEmpty(identity)||StringUtils.isEmpty(name)){
//                return  DefaultResult.failResult("有记录的身份证或姓名列为空！");
                continue;
            }
            Examinee e = new Examinee(now,now,identity,name,position,"");
            Byte b = 0;
            e.setEnd(b);
            examinees.add(e);
        }
        for (Examinee e: examinees) {
            if (old.contains(e)){
                examineeService.update(e);
            }else {
                examineeService.insert(e);
            }
        }
        return  DefaultResult.successResult("导入成功");
    }


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
            marks = testMarkService.selectByExample(testMarkExample);
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
    public  DefaultResult outputContent(OutputStream os) throws Exception {
        TestMarkExample testMarkExamplea = new TestMarkExample();
        testMarkExamplea.createCriteria().andTypeEqualTo("A");
        List<TestMark> amarks = testMarkService.selectByExample(testMarkExamplea);
        TestMarkExample testMarkExampleb = new TestMarkExample();
        testMarkExampleb.createCriteria().andTypeEqualTo("B");
        List<TestMark> bmarks = testMarkService.selectByExample(testMarkExampleb);
        Map<String,String> acontents = new HashMap<String, String>();
        Map<String,String> bcontents = new HashMap<String, String>();
        for (TestMark mark:amarks) {
            acontents.put(mark.getIdentity(),mark.getContent());
        }
        for (TestMark mark:bmarks) {
            bcontents.put(mark.getIdentity(),mark.getContent());
        }
        try {
            for (Map.Entry<String, String> entry : acontents.entrySet()) {
                if (Objects.isNull(entry)) continue;
                String filePath = "D://score//A//";
                File file=new File(filePath);
                if (!file.exists())
                file.mkdirs();
                filePath += entry.getKey()+".txt";
                file = new File(filePath);
                if (!file.exists())  file.createNewFile();
                FileOutputStream  fos = new FileOutputStream(file);
                if (entry.getValue()!= null)
                    fos.write(entry.getValue().getBytes("UTF-8"));
                fos.flush();
                fos.close();
            }

            for (Map.Entry<String, String> entry : bcontents.entrySet()) {
                if (Objects.isNull(entry)) continue;
                String filePath = "D://score//B//";
                File file=new File(filePath);
                if (!file.exists())
                    file.mkdirs();
                filePath += entry.getKey()+".txt";
                file = new File(filePath);
                if (!file.exists())  file.createNewFile();
                FileOutputStream  fos = new FileOutputStream(file);
                if (entry.getValue()!= null)
                fos.write(entry.getValue().getBytes("UTF-8"));
                fos.flush();
                fos.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            return DefaultResult.failResult("下载失败");
        }

        zip("D://score//","D://temp.zip");
        File temp = new File("D://temp.zip");
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(temp.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        os.write(buffer);
        temp.delete();
        removedir(new File("D://score"));
        return  DefaultResult.successResult("下载成功");
    }

    private TestMark getByIdentity(List<TestMark> list,String identity){
        for (TestMark l:list){
            if (identity.equals(l.getIdentity()))
                return  l;
        }
        return  null;
    }

    /**
     *
     * @param inputFileName
     *            输入一个文件夹
     * @param zipFileName
     *            输出一个压缩文件夹，打包后文件名字
     * @throws Exception
     */
    public  void zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    private  void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    private  void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) { // 判断是否为目录
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else { // 压缩目录中的所有文件
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
    //删除文件夹以及其中内容
    public  void removedir(File file)
    {
        File[] files=file.listFiles();
        for(File f:files)
        {
            if(f.isDirectory())//递归调用
            {
                removedir(f);
            }
            else {
                f.delete();
            }
        }
        //一层目录下的内容都删除以后，删除掉这个文件夹
        file.delete();
    }

    //删除所有考生信息
    public DefaultResult deleteAll(){
        examineeService.deleteAll();
        testMarkService.deleteAll();
        return  DefaultResult.successResult("删除成功");
    }
    //查询所有考生信息
    public List<Examinee> listExaminee(){
        return examineeService.getAll();
    }


    public DefaultResult resetEnd(Long id){
        if (id == null || id<0) return  DefaultResult.failResult("参数非法");
        Examinee ex = examineeService.getById(id);
        if (ex == null) return  DefaultResult.failResult("无此考生");
        Byte b = 0;
        ex.setEnd(b);
        Examinee e = examineeService.update(ex);
        return DefaultResult.successResult();
    }

    public DefaultResult delMark(String identity){
        TestMarkExample example = new TestMarkExample();
        example.createCriteria().andIdentityEqualTo(identity);
        if (testMarkService.deleteByExample(example)) {
            return DefaultResult.successResult();
        }else {
            return  DefaultResult.failResult();
        }
    }
}
