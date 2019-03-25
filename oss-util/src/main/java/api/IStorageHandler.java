package api;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @description oss操作handler
 * @author chibei
 */
public interface IStorageHandler {

    /**
     * 向文件服务器上传文件，返回一个oss存储的key
     * @param ossKey 用户可以指定自己的ossKey，如果ossKey相同将会执行文件覆盖，缺省为UUID生成
     * @param uploadFile
     * @return ossKey
     */
    String uploadFile(String ossKey, @NotNull File uploadFile);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key
     * @param ossKey 用户可以指定自己的ossKey，如果ossKey相同将会执行文件覆盖，缺省为UUID生成
     * @param uploadFile
     * @param rootDir
     * @return ossKey
     */
    String uploadFile(String ossKey, @NotNull File uploadFile, String rootDir);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key, 缺省key为UUID生成
     * @param uploadFile
     * @return ossKey
     */
    String uploadFile(@NotNull File uploadFile);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key, 缺省key为UUID生成
     * @param uploadFile
     * @param rootDir 父文件夹
     * @return ossKey
     */
    String uploadFile(@NotNull File uploadFile, String rootDir);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key, 缺省key为UUID生成
     * @param inputStream
     * @param fileName
     * @return ossKey
     */
    String uploadFile(@NotNull InputStream inputStream, @NotBlank String fileName);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key, 缺省key为UUID生成
     * @param inputStream
     * @param fileName
     * @param rootDir 父文件夹
     * @return ossKey
     */
    String uploadFile(@NotNull InputStream inputStream, @NotBlank String fileName, String rootDir);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key
     * @param ossKey 用户可以指定自己的ossKey，如果ossKey相同将会执行文件覆盖，缺省为UUID生成
     * @param inputStream
     * @param fileName
     * @return ossKey
     */
    String uploadFile(String ossKey, @NotNull InputStream inputStream, @NotBlank String fileName);

    /**
     * 向文件服务器上传文件，返回一个oss存储的key
     * @param ossKey 用户可以指定自己的ossKey，如果ossKey相同将会执行文件覆盖，缺省为UUID生成
     * @param inputStream
     * @param fileName
     * @param rootDir
     * @return ossKey
     */
    String uploadFile(String ossKey, @NotNull InputStream inputStream, @NotBlank String fileName,
        String rootDir);

    /**
     * 使用指定的OSS Endpoint、STS提供的临时Token信息(Access Id/Access Key/Security Token)、
     * 客户端配置构造一个新的{@link OSSClient}对象。并上传文件
     * @param endpoint
     * @param accessKeyId
     * @param secretAccessKey
     * @param securityToken
     * @param inputStream
     * @param fileName
     * @param rootDir
     * @return
     */
    String uploadFile(String endpoint, String bucketName, String accessKeyId,
        String secretAccessKey, String securityToken,
        @NotNull InputStream inputStream, @NotBlank String fileName, String rootDir);


    /**
     * 向文件服务器上传文件，并自定义文件http头，osskey可自定义，也可以使用OSS默认的 返回一个oss存储的key
     * @param uploadFile
     * @param objectMetadata
     * @return
     */
    String uploadFile(String ossKey, @NotNull File uploadFile,
        @NotNull ObjectMetadata objectMetadata);

    /**
     * 使用指定的OSS Endpoint、STS提供的临时Token信息(Access Id/Access Key/Security Token)、
     * 客户端配置构造一个新的{@link OSSClient}对象。并上传文件
     * @param ossKey
     * @param endpoint
     * @param accessKeyId
     * @param secretAccessKey
     * @param securityToken
     * @param inputStream
     * @param fileName
     * @param rootDir
     * @return
     */
    String uploadFile(String ossKey, String endpoint, String bucketName, String accessKeyId,
        String secretAccessKey, String securityToken,
        @NotNull InputStream inputStream, @NotBlank String fileName, String rootDir);

    /**
     * 通过oss上面存储的key获取签名的url
     * @param ossKey
     * @param expirationTime 设置该url的过期时间，缺省为有效期10年
     * @return 签名后的url
     */
    String getOssUrlByOssKey(@NotBlank String ossKey, Date expirationTime);

    /**
     * 通过oss上面存储的key获取签名的url,签名的有效期10年
     * @param ossKey
     * @return 签名后的url
     */
    String getOssUrlByOssKey(@NotBlank String ossKey);

    /**
     * 通过oss上面存储的key获取签名的url,签名的有效期10年
     * @param accessKeyId
     * @param endpoint
     * @param bucketName
     * @param secretAccessKey
     * @param securityToken
     * @param ossKey
     * @return 签名后的url
     */
    String getOssUrlByOssKey(String endpoint, String bucketName, String accessKeyId,
        String secretAccessKey, String securityToken, @NotBlank String ossKey);

    /**
     * 从文件服务器下载文件
     * @param ossKey
     * @return 文件的输入流
     */
    InputStream downloadFile(@NotBlank String ossKey);

    /**
     * 根据key删除oss对象
     * @param ossKey
     * @return
     */
    Boolean deleteObject(@NotBlank String ossKey);

    /**
     * 根据keys批量删除oss对象
     * @param ossKeys
     * @return
     */
    Boolean deleteObjectBatch(@NotEmpty List<String> ossKeys);
}
