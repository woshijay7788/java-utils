package response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description 分页参数
 * @author chibei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private static final Integer DEFAULT_CURRENT = 1;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private Integer current = DEFAULT_CURRENT;

    private Long total;

}
