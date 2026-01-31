package common.singleTon;

import java.util.Random;

/**
 * 全局 random 单例
 *
 * @author F1ower
 * @since 2026-1-31
 */
public class RandomSingleTon {

    private RandomSingleTon() {
    }

    /**
     * 静态内部类实现全局单例
     */
    private static final class SingletonHolder {
        private static final Random RANDOM = new Random(42);
    }

    /**
     * 获取 random 单例
     *
     * @return random
     */
    public static Random getRandom() {
        return SingletonHolder.RANDOM;
    }
}
