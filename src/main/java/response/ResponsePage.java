package response;

import com.github.pagehelper.Page;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @Description 分页实体类
 * @author chibei
 */
@Data
public class ResponsePage<T> extends Pagination implements Serializable {

    private List<T> list;

    public ResponsePage(){
        super();
    }

    public ResponsePage(Page page) {
        super();
        this.list = page.getResult();
        super.setCurrent(page.getPageNum());
        super.setPageSize(page.getPageSize());
        super.setTotal(page.getTotal());
    }

    public ResponsePage(List<T> list, Page page) {
        super();
        this.list = list;
        super.setCurrent(page.getPageNum());
        super.setPageSize(page.getPageSize());
        super.setTotal(page.getTotal());
    }

    public ResponsePage(List<T> list, Integer current, Integer pageSize, Long total) {
        super();
        this.list = list;
        super.setCurrent(current);
        super.setPageSize(pageSize);
        super.setTotal(total);
    }
}
