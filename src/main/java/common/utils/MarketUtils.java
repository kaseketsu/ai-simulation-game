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
     * NORMAL 的基础 ratio
     */
    private static final double BASE_RATIO = 0.2;
    /**
     * 基础比例
     */
    private static final double NORMAL_RATIO = 0.6;
    /**
     * 稀有比例
     */
    private static final double RARE_RATIO = 0.25;
    /**
     * 传世比例
     */
    private static final double SUPER_RARE_RATIO = 0.1;
    /**
     * 至尊比例
     */
    private static final double MYTHICAL_RATIO = 0.05;
    /**
     * 稀有权重
     */
    private static final double RARE_WEIGHT = 2.0;
    /**
     * 传世权重
     */
    private static final double SUPER_RARE_WEIGHT = 2.0;
    /**
     * 至尊权重
     */
    private static final double MYTHICAL_WEIGHT = 2.0;
    /**
     * 总权重
     */
    private static final double TOTAL_WEIGHT = 5.0;

    /**
     * 根据眼光计算比例
     *
     * @param sense 眼光
     * @return 比例
     */
    public static double fetchPriceRatio(int sense) {
        // 1 - (1 / (1 + sense * 0.1)))
        return 1.0 - (1.0 / (1.0 + sense * 0.1));
    }

    /**
     * 获取当前日灵市价格波动比例
     *
     * @return 波动比例
     */
    public static double fetchWavePriceRatio() {
        // 0.1 - 0.9
        return Math.max(RANDOM.nextDouble() + 0.1, 0.9);
    }

    /**
     * 获取每种品质的灵材数量
     *
     * @param sense 眼光
     * @param total 总量
     * @return 每种灵材数量
     */
    public static int[] fetchEachCategoryCount(int sense, int total) {
        // 追加比例
        double addRate = fetchPriceRatio(sense);
        // 低品质至少保留 20%
        addRate = Math.max(addRate, NORMAL_RATIO - BASE_RATIO);
        // 额外的比例 2:2:1 分配
        double[] ratios = new double[4];
        ratios[0] = NORMAL_RATIO - addRate;
        ratios[1] = RARE_RATIO + RARE_WEIGHT * (addRate / TOTAL_WEIGHT);
        ratios[2] = SUPER_RARE_RATIO + SUPER_RARE_WEIGHT * (addRate / TOTAL_WEIGHT);
        ratios[3] = MYTHICAL_RATIO + MYTHICAL_WEIGHT * (addRate / TOTAL_WEIGHT);
        // 遍历 total 次，返回
        int[] counts = new int[4];
        // 概率区间
        double[] probInterval = new double[4];
        probInterval[0] = ratios[0] * 100;
        probInterval[1] = probInterval[0] + ratios[1] * 100;
        probInterval[2] = probInterval[1] + ratios[2] * 100;
        probInterval[3] = probInterval[2] + ratios[3] * 100;
        // 遍历 total 次，填充 counts
        for (int i = 0; i < total; i++) {
            double randomNum = RANDOM.nextDouble() * 100;
            if (randomNum >= 0 && randomNum < probInterval[0]) {
                counts[0]++;
            } else if (randomNum >= probInterval[1] && randomNum < probInterval[2]) {
                counts[1]++;
            } else if (randomNum >= probInterval[2] && randomNum < probInterval[3]) {
                counts[2]++;
            } else {
                counts[3]++;
            }
        }
        return counts;
    }
}
