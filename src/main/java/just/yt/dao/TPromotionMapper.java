package just.yt.dao;

import java.util.List;
import just.yt.model.TPromotion;
import just.yt.model.TPromotionExample;
import org.apache.ibatis.annotations.Param;

public interface TPromotionMapper {
    int countByExample(TPromotionExample example);

    int deleteByExample(TPromotionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TPromotion record);

    int insertSelective(TPromotion record);

    List<TPromotion> selectByExample(TPromotionExample example);

    TPromotion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TPromotion record, @Param("example") TPromotionExample example);

    int updateByExample(@Param("record") TPromotion record, @Param("example") TPromotionExample example);

    int updateByPrimaryKeySelective(TPromotion record);

    int updateByPrimaryKey(TPromotion record);
}