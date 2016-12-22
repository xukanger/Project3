package just.yt.controller;

import just.yt.controller.vo.Url;
import just.yt.model.TCommodity;
import just.yt.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by yt on 2016/12/21.
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private CommodityService commodityService;

    @RequestMapping(value="/commodity/{id}",method= RequestMethod.POST)
    public @ResponseBody
    void upload(@PathVariable Integer id, @RequestBody String url) {
        TCommodity tCommodity=new TCommodity();
        tCommodity.setId(id);
        tCommodity.setUrl(url);
        commodityService.update(tCommodity);
    }

    @RequestMapping(value="/commodity/{id}",method= RequestMethod.GET)
    public @ResponseBody
    Url getPic(@PathVariable Integer id) {

        Url url=new Url();
        url.setUrl(commodityService.getById(id).getUrl());
        return url;
    }
}
