package sms.submail;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import config.AppConfig;
import config.MessageConfig;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lib.MESSAGEXsend;
import lib.MessageMultiXSend;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sms.api.ISmsSender;
import sms.api.SmsEntity;
import sms.config.SmsConfig;
import sms.exception.SmsErrCodeEnum;
import sms.exception.SmsException;
import utils.ConfigLoader;

/**
 * @author chibei
 * @description submail短信发射器
 */
@Setter
@Getter
public class SubmailSmsSender implements ISmsSender {
    private static final Logger LOG = LoggerFactory.getLogger(SubmailSmsSender.class);
    public static final String SIGN_TYPE_NORMAL = "normal";
    public static final String SIGN_TYPE_MD5 = "md5";
    public static final String SIGN_TYPE_SHA1 = "sha1";
    public static final Integer ZERO = 0;

    private AppConfig sdkConfig;

    public SubmailSmsSender(SmsConfig smsConfig) {
        this.sdkConfig = transferSmsConfigToSdkConfig(smsConfig);
    }

    /**
     * 转化sms全局配置对象为特定config对象
     *
     * @param smsConfig sms配置类对象
     * @return 成功：返回sdkConfig即submal配置类对象，失败：返回空
     */
    private AppConfig transferSmsConfigToSdkConfig(SmsConfig smsConfig) {
        if (smsConfig != null && verifySmsConfig(smsConfig)) {
            Properties pros = new Properties();
            pros.put(MessageConfig.APP_ID, smsConfig.getSmsAppId());
            pros.put(MessageConfig.APP_KEY, smsConfig.getSmsAppKey());
            pros.put(MessageConfig.APP_SIGNTYPE, smsConfig.getSmsSignType());
            ConfigLoader.setPros(pros);
            return ConfigLoader.load(ConfigLoader.ConfigType.Message);
        }
        return null;
    }

    /**
     * 由于短信服务商不同，其所需参数也不同，应该将配置校验工作个性化，即由具体的不同的sender去校验
     *
     * @param smsConfig sms配置类对象
     * @return true校验成功，false校验失败
     */
    @Override
    public boolean verifySmsConfig(SmsConfig smsConfig) {
        if (Strings.isNullOrEmpty(smsConfig.getSmsAppId())
                || Strings.isNullOrEmpty(smsConfig.getSmsAppKey())
                || Strings.isNullOrEmpty(smsConfig.getSmsSignType())) {
            return false;
        }
        if (!smsConfig.getSmsSignType().trim().equals(SIGN_TYPE_NORMAL)
                && !smsConfig.getSmsSignType().trim().equals(SIGN_TYPE_MD5)
                && !smsConfig.getSmsSignType().trim().equals(SIGN_TYPE_SHA1)) {
            return false;
        }
        return true;
    }

    /**
     * 发送一条短信
     *
     * @param smsEntity sms短信实体类对象
     * @return true发送成功，false发送失败
     */
    @Override
    public boolean send(SmsEntity smsEntity) throws SmsException {
        if (smsEntity == null || Strings.isNullOrEmpty(smsEntity.getTemplateId())
                || Strings.isNullOrEmpty(smsEntity.getPhoneNum())
                || smsEntity.getTemplateParams() == null
                || smsEntity.getTemplateParams().isEmpty()) {
            LOG.error("SMS: method send(entity) check failed before sending...");
            return false;
        }
        MESSAGEXsend sdkXSender = new MESSAGEXsend(this.sdkConfig);
        sdkXSender.setProject(smsEntity.getTemplateId().trim());
        sdkXSender.addTo(smsEntity.getPhoneNum().trim());
        Map<String, String> map = smsEntity.getTemplateParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (Strings.isNullOrEmpty(key)) {
                LOG.error("SMS: method send(entity) key of params in message's template cannot be null...{}"
                        , smsEntity.toString());
                return false;
            }
            String value = entry.getValue();
            if (Strings.isNullOrEmpty(value)) {
                LOG.error("SMS: method send(entity) value of params in message's template cannot be null...{}"
                        , smsEntity.toString());
                return false;
            }
            sdkXSender.addVar(key, value);
        }
        String result = "";
        if (Strings.isNullOrEmpty(result)) {
            LOG.error("SMS: method send(entity) unkwon error,try again after a moment,please");
            throw new SmsException(SmsErrCodeEnum.SMS_UNKWON_SENDING_ERROR);
        } else if (!result.toLowerCase().contains("success")) {
            JSONObject json = JSONObject.parseObject(result);
            LOG.info("SMS: method send(entity) failed...result: {}", result);
            throw new SmsException(json.getString("code"), json.getString("msg"));
        } else {
            LOG.info("SMS: method send(entity) successful...result: {}", result);
            return true;
        }
    }

    /**
     * 群发短信
     *
     * @param smsEntityList sms短信实体类对象列表
     * @return 返回发送成功的条数（Integer类型），null表示产生异常
     */
    @Override
    public Integer send(List<SmsEntity> smsEntityList) throws SmsException {
        if (smsEntityList == null || smsEntityList.size() <= 0) {
            LOG.error("SMS: send(listSmsEntity) check failed before sending ... smsEntityList cannot be null or empty ...");
            return ZERO;
        }
        MessageMultiXSend sdkMultiXSender = new MessageMultiXSend(this.sdkConfig);
        SmsEntity smsEntity = null;
        String templateId = null;
        int size = smsEntityList.size();
        for (int i = 0; i < size; i++) {
            smsEntity = smsEntityList.get(i);
            templateId = smsEntity.getTemplateId();
            String phoneNum = smsEntity.getPhoneNum();
            if (Strings.isNullOrEmpty(templateId)
                    || Strings.isNullOrEmpty(phoneNum)) {
                LOG.error("SMS: send(listSmsEntity) check failed...templateId and phoneNum cannot be null ... entity.phoneNum: {}"
                        , smsEntity.toString());
                return ZERO;
            }
            Map<String, String> map = smsEntity.getTemplateParams();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                if (Strings.isNullOrEmpty(key)) {
                    LOG.error("SMS: send(listSmsEntity) check failed...key of params in message's template cannot be null ... entity.toString: {}"
                            , smsEntity.toString());
                    return ZERO;
                }
                String value = entry.getValue();
                if (Strings.isNullOrEmpty(value)) {
                    LOG.error("SMS: send(listSmsEntity) check failed...value of params in message's template cannot be null ... entity.toString: {}"
                            , smsEntity.toString());
                    return ZERO;
                }
                sdkMultiXSender.getVars(key, value);
            }
            sdkMultiXSender.addMulti(phoneNum);
        }
        sdkMultiXSender.addProject(templateId);
        String result = "";
        if (Strings.isNullOrEmpty(result)) {
            LOG.error("SMS: method send(listSmsEntity) unkwon error,try again after a moment,please");
            throw new SmsException(SmsErrCodeEnum.SMS_UNKWON_SENDING_ERROR);
        } else {
            return this.analyzeResult(result);
        }

    }

    /**
     * 解析群发短信结果result
     *
     * @param result 群发接口调用返回结果
     * @return 返回发送成功的条数（Integer类型），null表示产生异常
     */
    private Integer analyzeResult(String result) {
        if (Strings.isNullOrEmpty(result)) {
            LOG.error("SMS: method send(entity) unkwon error,try again after a moment,please");
            return null;
        } else {
            JSONArray failResults = new JSONArray();
            Integer counterSuccess = 0;
            JSONArray jsonArray = JSONArray.parseArray(result);
            int size = jsonArray.size();
            JSONObject json = null;
            for (int i = 0; i < size; i++) {
                json = jsonArray.getJSONObject(i);
                String status = json.getString("status");
                if (!Strings.isNullOrEmpty(status)
                        && status.trim().toLowerCase().equals("success")) {
                    counterSuccess += 1;
                } else {
                    failResults.add(json);
                }
            }
            if (!counterSuccess.equals(size)) {
                LOG.error("SMS: method send(listSmsEntity) send failed ... total: {} ... success:{} ... fail: {} ... fail_results: {}"
                        , size, counterSuccess, failResults.size(), failResults.toJSONString());
            } else {
                LOG.info("SMS: method send(listSmsEntity) send successful ... total: {} ... success: {}...fail: {} ... result: {}"
                        , size, counterSuccess, failResults.size(), result);
            }
            return counterSuccess;
        }
    }
}
