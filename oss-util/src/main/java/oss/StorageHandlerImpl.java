package oss;

import api.IStorageHandler;
import common.constant.ErrorCodeEnum;
import common.exception.StorageException;
import config.StorageConfig;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;


/**
 * @description oss配置
 * @author chibei
 */
@Slf4j
public class StorageHandlerImpl implements IStorageHandler {

    private static final long TEN_YEAR_MILLISECOND = 3600L * 1000 * 24 * 365 * 10;

    private static final String CONNECT = "/";

    private OSSClient ossClient;
    private String bucketName;


    public StorageHandlerImpl(StorageConfig storageConfig) throws StorageException{
        if (storageConfig == null) {
            throw new StorageException(ErrorCodeEnum.CONFIG_CANNOT_BE_NULL);
        }
        ossClient = new OSSClient(storageConfig.getEndpoint(), storageConfig.getAccessKeyId(),
                storageConfig.getAccessKeySecret());
        this.bucketName = storageConfig.getBucketName();
    }

    @Override
    public String uploadFile(String ossKey, File uploadFile) {

        return uploadFile(ossKey, uploadFile, "");
    }

    @Override
    public String uploadFile(String ossKey, File uploadFile, String rootDir) {

        // 1. 校验参数
        if (uploadFile == null) {
            log.error("uploadFile failed, the uploadFile or bucketName is null");
            return null;
        }

        // 2. 得到上传的文件流
        InputStream is;
        String fileName;
        try {
            is = new FileInputStream(uploadFile);
            fileName = uploadFile.getName();
            ossKey = uploadFileCommonMethod(this.ossClient, this.bucketName, ossKey, is, fileName, rootDir);
            return ossKey;
        } catch (IOException ex) {
            log.error("new FileInputStream failed, errorMessage is:{}", ExceptionUtils.getStackTrace(ex));
            return null;
        }

    }

    @Override
    public String uploadFile(File uploadFile) {
        return uploadFile(null, uploadFile);
    }

    @Override
    public String uploadFile(File uploadFile, String rootDir) {
        return uploadFile(null, uploadFile, rootDir);
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName) {
        return uploadFile(null, inputStream, fileName);
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String rootDir) {

        return uploadFile(null, inputStream, fileName, rootDir);
    }

    @Override
    public String uploadFile(String ossKey, InputStream inputStream, String fileName) {

        // 1. 校验参数
        if (inputStream == null || StringUtils.isBlank(fileName)) {
            log.error("uploadFile(InputStream inputStream, String fileName), param is:[{},{}]", inputStream, fileName);
            return null;
        }

        ossKey = uploadFileCommonMethod(this.ossClient, this.bucketName, ossKey, inputStream, fileName, null);

        return ossKey;
    }

    @Override
    public String uploadFile(String ossKey, InputStream inputStream, String fileName, String rootDir) {

        // 1. 校验参数
        if (inputStream == null || StringUtils.isBlank(fileName)) {
            log.error("uploadFile(InputStream inputStream, String fileName), param is:[{},{}]", inputStream, fileName);
            return null;
        }

        ossKey = uploadFileCommonMethod(this.ossClient, this.bucketName, ossKey, inputStream, fileName, rootDir);

        return ossKey;
    }

    @Override
    public String uploadFile(String endpoint, String bucketName, String accessKeyId, String secretAccessKey, String securityToken, InputStream inputStream, String fileName, String rootDir) {

        return uploadFile(null, endpoint, bucketName, accessKeyId, secretAccessKey, securityToken, inputStream, fileName, rootDir);
    }

    @Override
    public String uploadFile(String ossKey, String endpoint, String bucketName, String accessKeyId, String secretAccessKey, String securityToken, InputStream inputStream, String fileName, String rootDir) {

        if (StringUtils.isAnyBlank(endpoint, accessKeyId, secretAccessKey, securityToken, fileName)) {
            log.error("the input param has empty param");
        }

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, secretAccessKey, securityToken);

        return uploadFileCommonMethod(ossClient, bucketName, ossKey, inputStream, fileName, rootDir);
    }

    @Override
    public String uploadFile(String ossKey, File uploadFile, ObjectMetadata objectMetadata) {
        // 1. 校验参数
        if (uploadFile == null) {
            log.error("uploadFile failed, the uploadFile or bucketName is null");
            return null;
        }

        // 2. 得到上传的文件流
        InputStream inputStream;
        String fileName;
        try {
            inputStream = new FileInputStream(uploadFile);
            fileName = uploadFile.getName();

            // 3 设置OSS上面的文件名，防止重复文件名互相替换
            if (StringUtils.isBlank(ossKey)) {
                log.info("uploadFile ossKey is null, use the UUID");
                ossKey = UUID.randomUUID().toString().replaceAll("-", "");
            }

            log.debug("start upload the file, fileName is: {}", fileName);
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, ossKey, inputStream, objectMetadata);
            log.debug("upload the file finish, eTag is: {}", putObjectResult.getETag());
            return ossKey;
        } catch (IOException ex) {
            log.error("new FileInputStream failed, errorMessage is:{}", ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    @Override
    public String getOssUrlByOssKey(String ossKey, Date expirationTime) {

        // 1. 得到当前操作的bucketName
        if (StringUtils.isBlank(this.bucketName)) {
            log.error("getOssUrlByOssKey method ckeck bucketName failed, ossKey is: [{}]", ossKey);
            return null;
        }

        if (expirationTime == null) {
            // 如果没有设置过期时间，设置url过期的时间为10年
            expirationTime = new Date(System.currentTimeMillis() + TEN_YEAR_MILLISECOND);
        }

        URL url = ossClient.generatePresignedUrl(this.bucketName, ossKey, expirationTime);
        if (url != null) {
            return url.toString().replace("+","%2B");
        }

        return null;
    }

    @Override
    public String getOssUrlByOssKey(String ossKey) {
        return getOssUrlByOssKey(ossKey, null);
    }

    @Override
    public String getOssUrlByOssKey(String endpoint, String bucketName, String accessKeyId, String secretAccessKey, String securityToken, String ossKey) {

        // 如果没有设置过期时间，设置url过期的时间为10年
        Date expirationTime = new Date(System.currentTimeMillis() + TEN_YEAR_MILLISECOND);

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, secretAccessKey, securityToken);
        URL url = ossClient.generatePresignedUrl(bucketName, ossKey, expirationTime);

        if (url != null) {
            return url.toString().replace("+","%2B");
        }

        return null;
    }

    @Override
    public InputStream downloadFile(String ossKey) {

        // 1 校验buketName
        if (StringUtils.isBlank(this.bucketName)) {
            log.error("downloadFile method ckeck bucketName failed, ossKey is: [{}]", ossKey);
            return null;
        }

        // 2 调用API
        OSSObject ossObject = ossClient.getObject(this.bucketName, ossKey);

        // 3 返回结果集
        return ossObject.getObjectContent();
    }

    @Override
    public Boolean deleteObject(String ossKey) {

        if (StringUtils.isBlank(ossKey)) {
            log.warn("deleteObject ossKey is blank");
            return Boolean.TRUE;
        }

        try {
            ossClient.deleteObject(this.bucketName, ossKey);
        } catch (ClientException | OSSException e) {
            log.warn("deleteObject failed, errorMessage:{}", e);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteObjectBatch(List<String> ossKeys) {

        if (CollectionUtils.isEmpty(ossKeys)) {
            log.warn("deleteObjectBatch ossKeys is empty");
            return Boolean.TRUE;
        }

        try {
            DeleteObjectsRequest request = new DeleteObjectsRequest(this.bucketName);
            request.setKeys(ossKeys);
            ossClient.deleteObjects(request);
        } catch (ClientException | OSSException e) {
            log.warn("deleteObject failed, errorMessage:{}", e);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 文件上传的通用方法
     * @param ossKey
     * @param inputStream
     * @param fileName
     * @param rootDir
     * @return
     */
    private String uploadFileCommonMethod(OSSClient ossClient, String bucketName, String ossKey, InputStream inputStream, String fileName, String rootDir) {

        if (ossClient == null) {
            ossClient = this.ossClient;
        }

        // 1 调用相关的API进行上传文件
        // 1.1 判断相关的bucket是否存在
        if (!ossClient.doesBucketExist(bucketName)) {
            log.warn("the bucket[{}] not exist, create the bucket", bucketName);
            return null;
        }

        // 1.2 设置上传的属性
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentDisposition(String.format("attachment; filename=%s", fileName));

        // 1.3 设置OSS上面的文件名，防止重复文件名互相替换
        if (StringUtils.isBlank(ossKey)) {
            log.info("uploadFile ossKey is null, use the UUID");
            ossKey = UUID.randomUUID().toString().replaceAll("-", "");
        }

        // 设置父文件夹
        if (StringUtils.isNotBlank(rootDir)) {
            ossKey = rootDir + CONNECT + ossKey;
        }

        if (inputStream != null) {
            log.debug("start upload the file, fileName is: {}", fileName);
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, ossKey, inputStream, objectMetadata);
            log.debug("upload the file finish, eTag is: {}", putObjectResult.getETag());
            return ossKey;
        }
        log.warn("the InputStream is null, upload failed, fileName is: {}", fileName);
        return null;
    }



}
