package sms.api;

import java.util.List;
import sms.config.SmsConfig;

/**
 * @author chibei
 * @description sms短信发送接口
 */
public interface ISmsSender {

    /**
     * 发送一条短信
     *
     * @param smsEntity sms短信实体类对象
     * @return true发送成功，false发送失败
     */
    boolean send(SmsEntity smsEntity);

    /**
     * 群发短信
     *
     * @param smsEntityList sms短信实体类对象列表
     * @return 发送成功的短信条数
     */
    Integer send(List<SmsEntity> smsEntityList);

    /**
     * 由于短信服务商不同，其所需参数也不同，应该将配置校验工作个性化，即由具体的不同的sender去校验
     *
     * @param smsConfig sms配置类对象
     * @return true校验成功，false校验失败
     */
    boolean verifySmsConfig(SmsConfig smsConfig);
}
