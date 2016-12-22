package just.yt.controller;

import just.yt.controller.vo.Page;
import just.yt.model.TComment;
import just.yt.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Controller
@RequestMapping("/comment")
public class CommentController {

        @Resource
        private CommentService commentService;


        @RequestMapping(value="/getAll",method= RequestMethod.GET)
        public @ResponseBody List<TComment> getAll() {
            List<TComment> comments = this.commentService.getAll();
                return comments;
        }

        @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
        public @ResponseBody String delete(@PathVariable Integer id) {
                commentService.delete(id);
                return null;
        }

        @RequestMapping(value="/update", method=RequestMethod.POST)
        public @ResponseBody String update(@RequestBody TComment tcomment) {
                commentService.update(tcomment);
                return null;
        }

        @RequestMapping(value="/save", method=RequestMethod.POST)
        public @ResponseBody String save(@RequestBody TComment tcomment) {
                commentService.save(tcomment);
                return null;
        }

        @RequestMapping(value="/count", method=RequestMethod.POST)
                public @ResponseBody Integer count() {
                return commentService.count();
        }

        @RequestMapping(value="/{id}",method= RequestMethod.GET)
        public @ResponseBody TComment getById(@PathVariable Integer id) {
                return this.commentService.getById(id);
        }

        @RequestMapping(value="/getByPage",method= RequestMethod.GET)
        public @ResponseBody List<TComment> getByPage(@RequestBody Page page) {
            List<TComment> comments = this.commentService.getByPage(page.getPageSize(),page.getPageNo());
                return comments;
        }
}