package sms.config;

import com.google.common.base.Strings;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author chibei
 * @description sms公共配置类，用于加载必要的配置参数
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@Configuration
public class SmsConfig implements Serializable {
    private static final long serialVersionUID = -4889171967520493283L;
    private static final Logger LOG = LoggerFactory.getLogger(SmsConfig.class);
    /**
     * 短信服务提供商，默认submail公司
     */
    public static final String DRIVER_SUBMAIL = "submail";
    /**
     * sms短信发送服务商，默认submail
     */
//    @Value("sms.smsDriver:submail")
    private String smsDriver;
    /**
     * 应用id
     */
//    @Value("${sms.appid}")
    private String smsAppId;
    /**
     * 应用秘钥
     */
//    @Value("${sms.appkey}")
    private String smsAppKey;
    /**
     * 加密类型
     */
//    @Value("${sms.signtype}")
    private String smsSignType;

    public SmsConfig() {
        this.smsDriver = DRIVER_SUBMAIL;
    }

    /**
     * 校验短信发送服务提供商
     *
     * @return true服务行配置正确，false不正确
     */
    public boolean verifyDriver() {
        if (!Strings.isNullOrEmpty(smsDriver)) {
            smsDriver = smsDriver.trim();
            if (smsDriver.equals(DRIVER_SUBMAIL)) {
                return true;
            }
        }
        LOG.error("SMS: invalid sms driver...caused by {}", smsDriver);
        return false;
    }
}
