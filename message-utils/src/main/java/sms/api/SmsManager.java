package sms.api;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sms.config.SmsConfig;
import sms.exception.SmsErrCodeEnum;
import sms.exception.SmsException;
import sms.submail.SubmailSmsSender;

/**
 * @author chibei
 * @description sms短信发送管理器
 */
@Component
public class SmsManager {
    private ISmsSender smsSender;
    private static final Logger LOG = LoggerFactory.getLogger(SmsManager.class);

    @Autowired
    public SmsManager(SmsConfig smsConfig) {
        smsSender = createSmsSender(smsConfig);
    }

    /**
     * 处理各种driver不同生成的smsSender具体实现类不同，默认submail
     *
     * @param smsConfig
     * @return smsSender sms短信发射器
     * @throws SmsException sms自定义异常
     */
    public ISmsSender createSmsSender(SmsConfig smsConfig) throws SmsException {
        if (smsConfig == null) {
            LOG.error("SMS: create sms sender failed...caused by sms config is null...");
            throw new SmsException(SmsErrCodeEnum.SMS_LOAD_CONFIG_FAIL);
        }
        if (!smsConfig.verifyDriver()) {
            LOG.error("SMS: create sms sender failed...caused by sms config property value is invalid...{}"
                    , smsConfig.toString());
            throw new SmsException(SmsErrCodeEnum.SMS_INVALID_CONFIG);
        }
        //处理各种driver不同生成的smsSender具体实现类不同，默认submail
        if (smsConfig.getSmsDriver().equals(SmsConfig.DRIVER_SUBMAIL)) {
            return new SubmailSmsSender(smsConfig);
        }
        return new SubmailSmsSender(smsConfig);
    }

    /**
     * 单发短信
     *
     * @param smsEntity
     * @return true发送成功，false失败
     */
    public boolean send(SmsEntity smsEntity) {
        return this.smsSender.send(smsEntity);
    }

    /**
     * 群发短信
     *
     * @param smsEntityList
     * @return 返回发送成功的条数（Integer类型），返回null表示出现异常
     */
    public Integer send(List<SmsEntity> smsEntityList) {
        return this.smsSender.send(smsEntityList);
    }

}
