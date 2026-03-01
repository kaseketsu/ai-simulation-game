package common.manager;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import common.config.AppConfig;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.FileUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    @Value("${spring.cos.save-path}")
    private String savePath;

    @Resource
    private AppConfig appConfig;

    /**
     * 上传文件到腾讯云存储
     *
     * @param key       上传路径
     * @param localPath 本地文件路径
     */
    public void putLocalFile(String key, String localPath) {
        try (InputStream stream = FileUtil.getInputStream(localPath)) {
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

    /**
     * 将图片下载到本地并存入云存储
     *
     * @param picUrl 图片 url
     * @return 云存储地址
     */
    public String uploadToCOS(String picUrl) {
        // 拼接保存路径
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String formatted = LocalDateTimeUtil.format(now, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 图片的完整保存路径
        String fullPath = "%s/%s_%s.png".formatted(savePath, uuid, formatted);
        // 生成临时文件
        Path path;
        try {
            path = Paths.get(fullPath);
            Path parentDir = path.getParent();
            // 补全父目录
            if (parentDir != null) {
                Files.createDirectories(parentDir);
                log.info("父目录创建完毕: {}", parentDir.toAbsolutePath());
            }
            // 创建文件
            Files.createFile(path);
            log.info("文件创建完毕, 路径为: {}", path.toAbsolutePath());
        } catch (IOException ex) {
            log.error("文件生成失败，原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.FILE_CREATE_ERROR);
        }
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
            HttpUtil.download(picUrl, outputStream, true);
        } catch (Exception ex) {
            log.error("图片下载失败，url: {}, 保存路径: {}, 原因为: {}", picUrl, fullPath, ex.getMessage());
            throw new BusinessException(ErrorCode.DOWNLOAD_ERROR, "图片下载失败");
        }
        fullPath = path.toAbsolutePath().toString();
        log.info("图片下载成功, 保存路径为: {}", fullPath);
        // 保存到腾讯云存储
        String fileName = FileUtils.fetchFileName("png");
        String putPath = appConfig.getImageSavePath() + "/" + fileName;
        this.putLocalFile(putPath, fullPath);
        log.info("上传到腾讯云存储成功，文件路径: {}", putPath);
        // 拼接 url
        String imageUrl = appConfig.getCosImagePrefix() + putPath;
        log.info("前端展示图片 url 为: {}", imageUrl);
        return imageUrl;
    }
}
