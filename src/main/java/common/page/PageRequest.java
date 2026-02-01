package common.page;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用分页请求
 */
@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4229408553318469329L;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 分页大小
     */
    private Integer pageSize;
}
