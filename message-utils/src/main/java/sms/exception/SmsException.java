package sms.exception;

import java.io.Serializable;
import lombok.Getter;

/**
 * @author chibei
 * @description sms自定义异常类
 */
public class SmsException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -4522487617909857150L;
    @Getter
    private String errCode;

    public SmsException(String message) {
        super(message);
    }

    public SmsException(String errCode, Exception e) {
        super(e);
        this.errCode = errCode;
    }

    public SmsException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public SmsException(SmsErrCodeEnum errCodeEnum) {
        this(errCodeEnum.getErrCode(), errCodeEnum.getErrMessage());
    }

    public SmsException(SmsErrCodeEnum errCodeEnum, String errMessage) {
        this(errCodeEnum.getErrCode(), errMessage);
    }


}
