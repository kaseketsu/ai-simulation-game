package common.utils;

import cn.hutool.db.Page;
import common.annotations.ExceptionLog;
import common.page.PageVO;

import java.util.List;

/**
 * 分页工具类
 *
 * @author F1ower
 * @since 2026-2-1
 */
public class PageUtils {

    @ExceptionLog("分页失败")
    public static <T> PageVO<T> buildPageVO(final List<T> records, int pageSize, int currentPage) {
        // 获取总数
        int total = records.size();
        if (total == 0) {
            return PageVO.emptyPage(currentPage, pageSize);
        }
        // 计算总页数
        int pages = (total + pageSize - 1) / pageSize;
        // 对 records 分页返回
        int startIdx = Math.min((currentPage - 1) * pageSize, total);
        List<T> res = records.subList(startIdx, Math.min(startIdx + pageSize, total));
        // 封装 pageVO
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setTotal((long) total);
        pageVO.setRecords(res);
        pageVO.setPageSize(pageSize);
        pageVO.setCurrentPage(currentPage);
        pageVO.setPages(pages);
        return pageVO;
    }
}
