package common.utils;

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
        // todo: 对 records 分页返回
        return null;
    }
}
