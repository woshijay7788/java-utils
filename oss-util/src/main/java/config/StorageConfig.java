package config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @description oss配置
 * @author chibei
 */
@Data
public class StorageConfig {

    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

    private static volatile StorageConfig storageConfig;

    private StorageConfig(String bucketName, String endpoint, String accessKeyId, String accessKeySecret){
        this.bucketName = bucketName;
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    private StorageConfig(String endpoint, String accessKeyId, String accessKeySecret){
        this(null,endpoint,accessKeyId,accessKeySecret);
    }

    /**
     * 初始化storage的配置，如果没有设置bucketName，那么需要在方法参数上传递
     * @param bucketName
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    public static StorageConfig newInstance(@NotBlank String bucketName, @NotBlank String endpoint, @NotBlank String accessKeyId, @NotBlank String accessKeySecret) {

        if (storageConfig == null) {
            synchronized (StorageConfig.class) {
                if (storageConfig == null) {
                    storageConfig = new StorageConfig(bucketName, endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return storageConfig;
    }
}
