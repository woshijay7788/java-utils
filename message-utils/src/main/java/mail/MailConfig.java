package mail;


import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import lombok.Data;

/**
 * <p> Description: [邮件配置信息] </p>
 *
 * @author chibei
 */
@Data
public class MailConfig implements Serializable {

    private static final long serialVersionUID = 4563814169002983728L;

    // 发送邮件的服务器的IP(或主机地址)
    private String mailServerHost = "smtp.outlook.cn";
    // 发送邮件的服务器的端口
    private String mailServerPort = "587";
    // 发件人邮箱地址
    private String fromAddress = "xx@xx.cn";
    // 收件人邮箱地址
    private String toAddress;
    // 抄送人邮箱地址 逗号分隔
    private String carbonAddress;
    // 抄送人邮箱地址数组
    private String[] carbonAddressArr;
    // 登陆邮件发送服务器的用户名
    private String userName = "xx@xx.cn";
    // 登陆邮件发送服务器的密码
    private String password = "123456";
    // 是否需要身份验证
    private boolean validate = true;
    // 邮件主题
    private String subject;
    // 邮件的文本内容
    private String content;
    // 邮件附件的文件名数组
    private String[] attachFileNameArr;
    // 附件打包名称
    private String bagName;
    // 文本格式
    private String textFormat = "text/plain; charset=GBK";

    /**
     * 文件附件的输入流
     */
    private List<MimeBodyPart> mimeBodyParts;

    /**
     * 批量发送
     */
    private List<String> toAdressList = Lists.newArrayList();

    Vector file = new Vector();// 附件文件集合

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.mailServerHost);
        properties.put("mail.smtp.port", this.mailServerPort);
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

    /**
     * <p> Description: [把主题转换为中文] </p>
     *
     * @author chibei
     */
    public String transferChinese(String strText) {
        try {
            strText = MimeUtility.encodeText(new String(strText.getBytes(),
                "GB2312"), "GB2312", "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strText;
    }

    public void attachfile(String attachFileNames) {
        file.addElement(attachFileNames);
    }

    public String[] getCarbonAddressArr() {
        return carbonAddressArr;
    }

    public void setCarbonAddressArr(String[] carbonAddressArr) {
        this.carbonAddressArr = carbonAddressArr;
    }

    public String[] getAttachFileNameArr() {
        return attachFileNameArr;
    }

    public void setAttachFileNameArr(String[] attachFileNameArr) {
        this.attachFileNameArr = attachFileNameArr;
    }

    public String getBagName() {
        return bagName;
    }

    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    public String getCarbonAddress() {
        return carbonAddress;
    }

    public void setCarbonAddress(String carbonAddress) {
        this.carbonAddress = carbonAddress;
    }

    public String getTextFormat() {
        return textFormat;
    }

    public void setTextFormat(String textFormat) {
        this.textFormat = textFormat;
    }
}
