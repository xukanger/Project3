package just.yt.service;

import just.yt.dao.TCommentMapper;
import just.yt.model.TComment;
import just.yt.model.TCommentExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Service("commentService")
public class CommentService {

@Resource
TCommentMapper tCommentMapper;


    public TComment insert(TComment comment){
        tCommentMapper.insertSelective(comment);
        return comment;
    }

    public List<TComment> getAll(){
        return tCommentMapper.selectByExample(new TCommentExample());
    }

    public TComment update(TComment comment){
        TCommentExample tCommentExample=new TCommentExample();
        tCommentExample.createCriteria().andIdEqualTo(comment.getId());
        tCommentMapper.updateByExampleSelective(comment,tCommentExample);
        return comment;
    }

    public void delete(Integer id){
        tCommentMapper.deleteByPrimaryKey(id);
    }

    public void save(TComment comment){
        tCommentMapper.insert(comment);
    }

    public Integer count(){
        return tCommentMapper.countByExample(new TCommentExample());
    }

    public List<TComment> getByPage(int pageSize,int pageNo){
        TCommentExample tCommentExample=new TCommentExample();
        tCommentExample.setLimitStart((pageNo - 1) * pageSize);
        tCommentExample.setLimitSize(pageSize);
        return tCommentMapper.selectByExample(tCommentExample);
        }

        public TComment getById(Integer id){
        return tCommentMapper.selectByPrimaryKey(id);
        }
}