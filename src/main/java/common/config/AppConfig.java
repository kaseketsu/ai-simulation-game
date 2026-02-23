package common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 存储一些配置信息
 *
 * @author F1ower
 * @since 2026-2-18
 */
@Component
@Data
public class AppConfig {

    /**
     * cos 中图片默认保存地址
     */
    @Value("${image-save-path:/images}")
    private String imageSavePath;

    /**
     * cos 中图片前缀路径
     */
    @Value("${cos-image-predix:https://ai-simulation-game-1334049508.cos.ap-shanghai.myqcloud.com}")
    private String cosImagePrefix;

    /**
     * 系统 prompt - 创建修士
     */
    @Value("${system-people:/prompts/systemPrompt-People.txt}")
    private String systemPeople;

    /**
     * 创建修士
     */
    @Value("${create-people:/prompts/createPeople.txt}")
    private String createPeople;
}
