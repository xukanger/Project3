package just.yt.controller;

import just.yt.controller.vo.Page;
import just.yt.model.TPromotion;
import just.yt.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Controller
@RequestMapping("/promotion")
public class PromotionController {

        @Resource
        private PromotionService promotionService;


        @RequestMapping(value="/getAll",method= RequestMethod.GET)
        public @ResponseBody List<TPromotion> getAll() {
            List<TPromotion> promotions = this.promotionService.getAll();
                return promotions;
        }

        @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
        public @ResponseBody String delete(@PathVariable Integer id) {
                promotionService.delete(id);
                return null;
        }

        @RequestMapping(value="/update", method=RequestMethod.POST)
        public @ResponseBody String update(@RequestBody TPromotion tpromotion) {
                promotionService.update(tpromotion);
                return null;
        }

        @RequestMapping(value="/save", method=RequestMethod.POST)
        public @ResponseBody String save(@RequestBody TPromotion tpromotion) {
                promotionService.save(tpromotion);
                return null;
        }

        @RequestMapping(value="/count", method=RequestMethod.POST)
                public @ResponseBody Integer count() {
                return promotionService.count();
        }

        @RequestMapping(value="/{id}",method= RequestMethod.GET)
        public @ResponseBody TPromotion getById(@PathVariable Integer id) {
                return this.promotionService.getById(id);
        }

        @RequestMapping(value="/getByPage",method= RequestMethod.GET)
        public @ResponseBody List<TPromotion> getByPage(@RequestBody Page page) {
            List<TPromotion> promotions = this.promotionService.getByPage(page.getPageSize(),page.getPageNo());
                return promotions;
        }
}