package common.page;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页参数类
 *
 * @param <T> 泛型参数
 * @author F1ower
 * @since 2026-2-1
 */
@Data
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6409511619971506344L;

    /**
     * 当前页码
     */
    private Integer currentPage = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 记录列表
     */
    private List<T> records;

    /**
     * 空分页
     *
     * @param <T> 泛型参数
     * @return 空分页
     */
    public static <T> PageVO<T> emptyPage() {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setRecords(List.of());
        pageVO.setTotal(0L);
        return pageVO;
    }

    /**
     * 空分页
     *
     * @param <T> 泛型参数
     * @return 空分页
     */
    public static <T> PageVO<T> emptyPage(int currentPage, int pageSize) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setCurrentPage(currentPage);
        pageVO.setPageSize(pageSize);
        pageVO.setRecords(List.of());
        pageVO.setTotal(0L);
        return pageVO;
    }
}
