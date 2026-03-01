package com.flower.game.ai.templates.abstractTemplates;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象图片模型调用模板类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Slf4j
public abstract class AbstractImageModelCallTemplate<T> {

    @Value("${spring.ali.api-key}")
    private String apiKey;

    @Value("${spring.ali.image-model}")
    private String imageModel;

    /**
     * 获取图片 url
     *
     * @param request 请求
     * @return 图片 url
     */
    public String fetchPicUrl(T request) {
        // 校验参数
        ParamsCheckUtils.checkObj(request);
        String imagePrompt = fetchImagePrompt();
        // 生成 JSON 参数（美化格式）
        String requestJson = JSONUtil.toJsonPrettyStr(request);
        // 为 StringBuilder 拼接
        String finalPrompt = imagePrompt.replace("%s", requestJson);
        return callImageModel(finalPrompt);
    }

    /**
     * 根据 prompt 调用模型并返回 url
     *
     * @param prompt 提示词
     * @return url
     */
    private String callImageModel(String prompt) {
        log.info("即将调用模型：{}, 请求提示词为: {}", imageModel, prompt);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prompt_extend", true);
        parameters.put("watermark", false);
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model(imageModel)
                        .prompt(prompt)
                        .n(1)
                        .size("1328*1328")
                        .parameters(parameters)
                        .build();
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result;
        try {
            log.info("---同步调用模型: {}，请等待任务执行----", imageModel);
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e) {
            log.error("图像模型调用失败. 调用模型: {}, 调用参数: {}, 错误原因: {}", imageModel, JSONUtil.toJsonPrettyStr(param), e.getMessage());
            throw new BusinessException(ErrorCode.TEXT_CALL_ERROR);
        }
        log.info("图像模型返回结果为: {}", JSONUtil.toJsonPrettyStr(result));
        // 返回 url
        return result.getOutput().getResults().get(0).get("url");
    }

    /**
     * 获取图片提示词
     *
     * @return 图片提示词
     */
    public abstract String fetchImagePrompt();

}
