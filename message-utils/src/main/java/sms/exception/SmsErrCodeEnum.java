package sms.exception;

/**
 * @author chibei
 * @description sms自定义异常码
 */
public enum SmsErrCodeEnum {

    SMS_INVALID_CONFIG("-100001", "SMS短信参数配置不合法"),
    SMS_LOAD_CONFIG_FAIL("-100002", "SMS短信参数配置加载失败"),
    SMS_CHECK_FAILED_BEFORE_SENDING("-100003","SMS发送前校验失败，请检查相关配置和短信内容"),
    SMS_TEMPLATE_PARAMS_CAN_NOT_NULL("-100004","SMS短信模板动态参数的key和value不能为空"),
    SMS_UNKWON_SENDING_ERROR("-100005","SMS短信发送未知错误，请稍后重试");


    private String errCode;
    private String errMessage;

    SmsErrCodeEnum(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
