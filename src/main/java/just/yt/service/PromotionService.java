package just.yt.service;

import just.yt.dao.TPromotionMapper;
import just.yt.model.TPromotion;
import just.yt.model.TPromotionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Service("promotionService")
public class PromotionService {

@Resource
TPromotionMapper tPromotionMapper;


    public TPromotion insert(TPromotion promotion){
        tPromotionMapper.insertSelective(promotion);
        return promotion;
    }

    public List<TPromotion> getAll(){
        return tPromotionMapper.selectByExample(new TPromotionExample());
    }

    public TPromotion update(TPromotion promotion){
        TPromotionExample tPromotionExample=new TPromotionExample();
        tPromotionExample.createCriteria().andIdEqualTo(promotion.getId());
        tPromotionMapper.updateByExampleSelective(promotion,tPromotionExample);
        return promotion;
    }

    public void delete(Integer id){
        tPromotionMapper.deleteByPrimaryKey(id);
    }

    public void save(TPromotion promotion){
        tPromotionMapper.insert(promotion);
    }

    public Integer count(){
        return tPromotionMapper.countByExample(new TPromotionExample());
    }

    public List<TPromotion> getByPage(int pageSize,int pageNo){
        TPromotionExample tPromotionExample=new TPromotionExample();
        tPromotionExample.setLimitStart((pageNo - 1) * pageSize);
        tPromotionExample.setLimitSize(pageSize);
        return tPromotionMapper.selectByExample(tPromotionExample);
        }

        public TPromotion getById(Integer id){
        return tPromotionMapper.selectByPrimaryKey(id);
        }
}