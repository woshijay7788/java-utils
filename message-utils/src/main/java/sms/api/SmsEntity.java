package sms.api;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chibei
 * @description sms发送实体类
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsEntity implements Serializable {
    private static final long serialVersionUID = 8608299243595226389L;
    /**
     * 短信接收人手机号码，必填
     */
    private String phoneNum;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 短信模板Id
     */
    private String templateId;
    /**
     * 短信模板的参数集合，key为参数名称，value为对应内容
     */
    private Map<String, String> templateParams;

    public SmsEntity(String templateId, String phoneNum, Map<String, String> templateParams) {
        this.templateId = templateId;
        this.phoneNum = phoneNum;
        this.templateParams = templateParams;
    }
}
