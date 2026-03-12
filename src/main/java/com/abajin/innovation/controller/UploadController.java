package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.util.MinioUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件上传（如团队申请简历附件）
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private MinioUtils minioUtils;

    /**
     * MinIO 桶名称和访问地址
     * 可在 application.yml 中通过 minio.bucket / minio.base-url 进行覆盖
     */
    @Value("${minio.bucket:first}")
    private String bucketName;

    @Value("${minio.base-url:http://192.168.147.110:9000}")
    private String minioBaseUrl; // 目前不直接使用，仅保留配置兼容

    /**
     * 上传文件，返回访问路径
     * POST /api/upload?dir=resume|activity-qrcode|activity-poster
     */
    @PostMapping
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", defaultValue = "resume") String dir) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择文件");
        }
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        try {
            // 确保桶存在
            minioUtils.createBucket(bucketName);
            // 对象名称按路径存储：second/dir/uuid.ext
            String objectName = "second/" + dir + "/" + filename;
            try (InputStream in = file.getInputStream()) {
                minioUtils.uploadFile(in, bucketName, objectName);
            }
            // 使用 MinIO 预签名 URL，确保浏览器可直接访问
            String url = minioUtils.getPreviewFileUrl(bucketName, objectName);
            return Result.success("上传成功", url);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
