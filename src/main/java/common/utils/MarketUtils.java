package common.utils;

import common.singleTon.RandomSingleTon;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 灵材采买相关工具类
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Slf4j
public class MarketUtils {

    /**
     * random 单例
     */
    private static final Random RANDOM = RandomSingleTon.getRandom();
    /**
     * 基础比例
     */
    private static final Integer BASE_RATIO = 1000;
    /**
     * 稀有比例
     */
    private static final Integer RARE_RATIO = 100;

    /**
     * 稀有上限
     */
    private static final Integer RARE_UPPERBOUND = 500;

    /**
     * 稀有每点眼光提升
     */
    private static final Integer RARE_UP = 20;

    /**
     * 传世比例
     */
    private static final Integer SUPER_RARE_RATIO = 30;

    /**
     * 传世上限
     */
    private static final Integer SUPER_RARE_UPPERBOUND = 150;

    /**
     * 传世每点眼光提升
     */
    private static final Integer SUPER_RARE_UP = 6;

    /**
     * 神话比例
     */
    private static final Integer MYTHICAL_RATIO = 10;

    /**
     * 神话上限
     */
    private static final Integer MYTHICAL_UPPERBOUND = 30;

    /**
     * 神话每点眼光提升
     */
    private static final Integer MYTHICAL_UP = 1;

    /**
     * 根据眼光计算比例
     *
     * @param sense 眼光
     * @return 比例
     */
    public static Double fetchPriceRatio(int sense) {
        // 1 - (1 / (1 + sense * 0.1)))
        return Math.max(1.0 - 0.02 * sense, 0.2);
    }

    /**
     * 获取当前日灵市价格波动比例
     *
     * @return 波动比例
     */
    public static double fetchWavePriceRatio() {
        // -0.5 - 1.5
        // 0.5 - 1.5 倍价格
        return RANDOM.nextDouble() - 0.5;
    }

    /**
     * 获取每种品质的灵材数量
     *
     * @param sense 眼光
     * @param total 总量
     * @return 每种灵材数量
     */
    public static int[] fetchEachCategoryCount(int sense, int total) {
        // 区间概率
        // 计算比例
        int rare = Math.min(RARE_RATIO + sense * RARE_UP, RARE_UPPERBOUND);
        int super_rare = Math.min(SUPER_RARE_RATIO + sense * SUPER_RARE_UP, SUPER_RARE_UPPERBOUND);
        int mythical = Math.min(MYTHICAL_RATIO + sense * MYTHICAL_UP, MYTHICAL_UPPERBOUND);
        int normal = BASE_RATIO - (rare + super_rare + mythical);
        // 构造区间
        int[] intervals = new int[4];
        intervals[0] = normal;
        intervals[1] = intervals[0] + rare;
        intervals[2] = intervals[1] + super_rare;
        intervals[3] = intervals[2] + mythical;
        // random 随机 [0 - 1000]
        int[] res = new int[4];
        for (int i = 0; i < total; i++) {
            // 换一个 seed
            RANDOM.setSeed(RANDOM.nextInt(114514));
            int number = RANDOM.nextInt(1001);
            log.info("弟 {} 次，数值为: {}", i, number);
            if (number < intervals[0]) {
                res[0]++;
            } else if (number < intervals[1]) {
                res[1]++;
            } else if (number < intervals[2]) {
                res[2]++;
                log.info("弟 {} 次，数值为: {}，获得超稀有！", i, number);
            } else {
                res[3]++;
                log.info("弟 {} 次，数值为: {}，获得神话！", i, number);
            }
        }
        return res;
    }
}
