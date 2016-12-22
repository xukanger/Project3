package just.yt.controller;

import just.yt.model.TCommodity;
import just.yt.model.TShoppinglist;
import just.yt.service.CustomerService;
import just.yt.service.ShoppinglistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yt on 2016/12/22.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private ShoppinglistService shoppinglistService;

    @Resource
    private CustomerService customerService;

    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public @ResponseBody
    List<TCommodity> getById(@PathVariable Integer id) {
        return this.customerService.getCommoditiesByCustomerId(id);
    }

    @RequestMapping(value="/shoppinglist",method= RequestMethod.POST)
    public @ResponseBody TShoppinglist getById(@RequestBody TShoppinglist tShoppinglist) {
        return this.customerService.checkExist(tShoppinglist.getUserid(),tShoppinglist.getCommodityid());
    }

}
