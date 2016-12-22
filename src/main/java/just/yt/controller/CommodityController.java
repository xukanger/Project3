package just.yt.controller;

import just.yt.controller.vo.Page;
import just.yt.model.TCommodity;
import just.yt.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Controller
@RequestMapping("/commodity")
public class CommodityController {

        @Resource
        private CommodityService commodityService;


        @RequestMapping(value="/getAll",method= RequestMethod.GET)
        public @ResponseBody List<TCommodity> getAll() {
            List<TCommodity> commoditys = this.commodityService.getAll();
                return commoditys;
        }

        @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
        public @ResponseBody String delete(@PathVariable Integer id) {
                commodityService.delete(id);
                return null;
        }

        @RequestMapping(value="/update", method=RequestMethod.POST)
        public @ResponseBody String update(@RequestBody TCommodity tcommodity) {
                commodityService.update(tcommodity);
                return null;
        }

        @RequestMapping(value="/save", method=RequestMethod.POST)
        public @ResponseBody String save(@RequestBody TCommodity tcommodity) {
                commodityService.save(tcommodity);
                return null;
        }

        @RequestMapping(value="/count", method=RequestMethod.POST)
                public @ResponseBody Integer count() {
                return commodityService.count();
        }

        @RequestMapping(value="/{id}",method= RequestMethod.GET)
        public @ResponseBody TCommodity getById(@PathVariable Integer id) {
                return this.commodityService.getById(id);
        }

        @RequestMapping(value="/getByPage",method= RequestMethod.GET)
        public @ResponseBody List<TCommodity> getByPage(@RequestBody Page page) {
            List<TCommodity> commoditys = this.commodityService.getByPage(page.getPageSize(),page.getPageNo());
                return commoditys;
        }
}