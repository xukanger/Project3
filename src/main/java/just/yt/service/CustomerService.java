package just.yt.service;

import just.yt.dao.TCommodityMapper;
import just.yt.dao.TShoppinglistMapper;
import just.yt.model.TCommodity;
import just.yt.model.TShoppinglist;
import just.yt.model.TShoppinglistExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yt on 2016/12/22.
 */
@Service
public class CustomerService {

    @Resource
    TShoppinglistMapper tShoppinglistMapper;

    @Resource
    TCommodityMapper tCommodityMapper;

    public List<TCommodity> getCommoditiesByCustomerId(Integer id){
        TShoppinglistExample tShoppinglistExample=new TShoppinglistExample();
        tShoppinglistExample.or().andUseridEqualTo(id);
        List<TShoppinglist> tShoppinglists=tShoppinglistMapper.selectByExample(tShoppinglistExample);
        List<TCommodity> list=new ArrayList<TCommodity>();
        for (TShoppinglist t :
                tShoppinglists) {
            list.add(tCommodityMapper.selectByPrimaryKey(t.getId()));
        }
        return list;
    }

    public TShoppinglist checkExist(Integer userId,Integer commodityId){
        TShoppinglistExample tShoppinglistExample=new TShoppinglistExample();
        tShoppinglistExample.or().andUseridEqualTo(userId).andCommodityidEqualTo(commodityId);
        List<TShoppinglist> shoppinglist=tShoppinglistMapper.selectByExample(tShoppinglistExample);
        if(shoppinglist.size()==0)return null;
        else return shoppinglist.get(0);
    }
}
