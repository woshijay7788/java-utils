package common.constant;

/**
 * @description 异常枚举
 * @author chibei
 */
public enum ErrorCodeEnum {

    PARAM_NOT_VALID("PARAM_NOT_VALID", "参数校验失败"),
    PARAM_CANNOT_BE_NULL("PARAM_CANNOT_BE_NULL", "参数不能为空"),
    BUCKET_NAME_CANNOT_BE_NULL("BUCKET_NAME_CANNOT_BE_NULL","bucketName不能为空"),
    DOWNLOAD_KEY_CANNOT_BE_NULL("DOWNLOAD_KEY_CANNOT_BE_NULL","下载文件时key不能为空"),
    CONFIG_CANNOT_BE_NULL("CONFIG_CANNOT_BE_NULL", "配置项不能为null");
    ;
    private String errorCode;
    private String errorMessage;
    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
