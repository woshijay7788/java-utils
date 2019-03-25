package common.exception;


import common.constant.ErrorCodeEnum;

/**
 * @description 异常定义
 * @author chibei
 */
public class StorageException extends RuntimeException{

    private String errorCode;
    private String errorMessage;

    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public StorageException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public StorageException(ErrorCodeEnum errorCodeEnum) {
        this.errorCode = errorCodeEnum.getErrorCode();
        this.errorMessage = errorCodeEnum.getErrorMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return String.format("errorCode is: %s, errorMessage is: %s", errorCode, errorMessage);
    }
}
