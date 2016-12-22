package just.yt.service;

import just.yt.dao.TShoppinglistMapper;
import just.yt.model.TShoppinglist;
import just.yt.model.TShoppinglistExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Service("shoppinglistService")
public class ShoppinglistService {

@Resource
TShoppinglistMapper tShoppinglistMapper;


    public TShoppinglist insert(TShoppinglist shoppinglist){
        tShoppinglistMapper.insertSelective(shoppinglist);
        return shoppinglist;
    }

    public List<TShoppinglist> getAll(){
        return tShoppinglistMapper.selectByExample(new TShoppinglistExample());
    }

    public TShoppinglist update(TShoppinglist shoppinglist){
        TShoppinglistExample tShoppinglistExample=new TShoppinglistExample();
        tShoppinglistExample.createCriteria().andIdEqualTo(shoppinglist.getId());
        tShoppinglistMapper.updateByExampleSelective(shoppinglist,tShoppinglistExample);
        return shoppinglist;
    }

    public void delete(Integer id){
        tShoppinglistMapper.deleteByPrimaryKey(id);
    }

    public void save(TShoppinglist shoppinglist){
        tShoppinglistMapper.insert(shoppinglist);
    }

    public Integer count(){
        return tShoppinglistMapper.countByExample(new TShoppinglistExample());
    }

    public List<TShoppinglist> getByPage(int pageSize,int pageNo){
        TShoppinglistExample tShoppinglistExample=new TShoppinglistExample();
        tShoppinglistExample.setLimitStart((pageNo - 1) * pageSize);
        tShoppinglistExample.setLimitSize(pageSize);
        return tShoppinglistMapper.selectByExample(tShoppinglistExample);
        }

        public TShoppinglist getById(Integer id){
        return tShoppinglistMapper.selectByPrimaryKey(id);
        }
}