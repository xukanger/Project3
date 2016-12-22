package just.yt.controller;

import just.yt.controller.vo.Page;
import just.yt.model.TShoppinglist;
import just.yt.service.ShoppinglistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by yt on 2016/12/13.
*/
@Controller
@RequestMapping("/shoppinglist")
public class ShoppinglistController {

        @Resource
        private ShoppinglistService shoppinglistService;


        @RequestMapping(value="/getAll",method= RequestMethod.GET)
        public @ResponseBody List<TShoppinglist> getAll() {
            List<TShoppinglist> shoppinglists = this.shoppinglistService.getAll();
                return shoppinglists;
        }

        @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
        public @ResponseBody String delete(@PathVariable Integer id) {
                shoppinglistService.delete(id);
                return null;
        }

        @RequestMapping(value="/update", method=RequestMethod.POST)
        public @ResponseBody String update(@RequestBody TShoppinglist tshoppinglist) {
                shoppinglistService.update(tshoppinglist);
                return null;
        }

        @RequestMapping(value="/save", method=RequestMethod.POST)
        public @ResponseBody String save(@RequestBody TShoppinglist tshoppinglist) {
                shoppinglistService.save(tshoppinglist);
                return null;
        }

        @RequestMapping(value="/count", method=RequestMethod.POST)
                public @ResponseBody Integer count() {
                return shoppinglistService.count();
        }

        @RequestMapping(value="/{id}",method= RequestMethod.GET)
        public @ResponseBody TShoppinglist getById(@PathVariable Integer id) {
                return this.shoppinglistService.getById(id);
        }

        @RequestMapping(value="/getByPage",method= RequestMethod.GET)
        public @ResponseBody List<TShoppinglist> getByPage(@RequestBody Page page) {
            List<TShoppinglist> shoppinglists = this.shoppinglistService.getByPage(page.getPageSize(),page.getPageNo());
                return shoppinglists;
        }
}