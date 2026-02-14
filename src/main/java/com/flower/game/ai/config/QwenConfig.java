package com.flower.game.ai.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 千问模型相关配置
 *
 * @author F1ower
 * @since 2026-2-14
 */
@Configuration
public class QwenConfig {

    @Value("${spring.ali.api-key}")
    private String apiKey;

    @Value("${spring.ali.base-url}")
    private String baseUrl;

    /**
     * 注册 openAiClient
     *
     * @return openAiClient
     */
    @Bean
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
    }
}
