package just.yt.controller.vo;

/**
 * Created by yt on 2016/12/21.
 */

public class Page {

    Integer pageNo;

    Integer pageSize;

    public Page(){

    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
