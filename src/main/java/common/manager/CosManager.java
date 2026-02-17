package common.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

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
    public void putLocalFileDemo(String key, String localPath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new File(localPath));
        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println(putObjectResult.getRequestId());
        } catch (CosClientException cce) {
            log.error("文件上传失败, key: {}, localPath: {}", key, localPath);
            throw new BusinessException(ErrorCode.UPLOAD_ERROR);
        }
    }
}
