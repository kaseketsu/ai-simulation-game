package common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 虚拟线程池配置类
 *
 * @author F1ower
 * @since 2026-3-1
 */
@Configuration
public class VirtualPoolConfig {

    /**
     * 返回虚拟线程池
     *
     * @return 虚拟线程池
     */
    @Bean
    public ExecutorService virtualPool() {
        return Executors.newCachedThreadPool();
    }
}
