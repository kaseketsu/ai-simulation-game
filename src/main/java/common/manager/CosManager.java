package common.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 云存储管理类
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Component
@Slf4j
public class CosManager {

    @Value("${spring.cos.secret-id}")
    private String secretId;

    @Value("${spring.cos.secret-key}")
    private String secretKey;

    @Value("${spring.cos.bucket-name}")
    private String bucketName;

    @Value("${spring.cos.region}")
    private String region;

    @Resource
    private COSClient cosClient;

    /**
     * 上传文件到腾讯云存储
     *
     * @param key       上传路径
     * @param localPath 本地文件路径
     */
    public void putLocalFile(String key, String localPath) {
        try (InputStream stream = this.getClass().getResourceAsStream(localPath)) {
            ThrowUtils.throwIf(stream == null, ErrorCode.NOT_FOUND_ERROR, "文件不存在, 路径; %s".formatted(localPath));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(stream.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, stream, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            String requestId = putObjectResult.getRequestId();
            log.info("请求 id 为: {}", requestId);
        } catch (CosClientException cce) {
            log.error("文件上传失败, key: {}, localPath: {}, 原因是: {}", key, localPath, cce.getMessage());
            throw new BusinessException(ErrorCode.UPLOAD_ERROR);
        } catch (IOException e) {
            log.error("流未正常关闭，path: {}", localPath);
            throw new BusinessException(ErrorCode.CLOSE_ERROR);
        }
    }
}
