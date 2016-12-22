package just.yt.service;

import just.yt.dao.TCommodityMapper;
import just.yt.model.TCommodity;
import just.yt.model.TCommodityExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Service("commodityService")
public class CommodityService {

@Resource
TCommodityMapper tCommodityMapper;


    public TCommodity insert(TCommodity commodity){
        tCommodityMapper.insertSelective(commodity);
        return commodity;
    }

    public List<TCommodity> getAll(){
        return tCommodityMapper.selectByExample(new TCommodityExample());
    }

    public TCommodity update(TCommodity commodity){
        TCommodityExample tCommodityExample=new TCommodityExample();
        tCommodityExample.createCriteria().andIdEqualTo(commodity.getId());
        tCommodityMapper.updateByExampleSelective(commodity,tCommodityExample);
        return commodity;
    }

    public void delete(Integer id){
        tCommodityMapper.deleteByPrimaryKey(id);
    }

    public void save(TCommodity commodity){
        tCommodityMapper.insert(commodity);
    }

    public Integer count(){
        return tCommodityMapper.countByExample(new TCommodityExample());
    }

    public List<TCommodity> getByPage(int pageSize,int pageNo){
        TCommodityExample tCommodityExample=new TCommodityExample();
        tCommodityExample.setLimitStart((pageNo - 1) * pageSize);
        tCommodityExample.setLimitSize(pageSize);
        return tCommodityMapper.selectByExample(tCommodityExample);
        }

        public TCommodity getById(Integer id){
        return tCommodityMapper.selectByPrimaryKey(id);
        }
}