package mail;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Description: [邮件发送工具类]
 * </p>
 *
 * @author chibei
 */
@Slf4j
public class MailSender {
    
    public static boolean sendMail(MailConfig mailConfig) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties properties = mailConfig.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailConfig.isValidate()) {
            authenticator = new MyAuthenticator(mailConfig.getUserName(), mailConfig.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(properties, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailConfig.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailConfig.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置多个抄送人 carbonAddress多地址可以用逗号分隔
            String carbonAddress = mailConfig.getCarbonAddress();
            String[] carbonAddressArr = mailConfig.getCarbonAddressArr();
            if (StringUtils.isNotBlank(carbonAddress)) {
                InternetAddress[] internetAddressCC = new InternetAddress().parse(carbonAddress);
                mailMessage.setRecipients(Message.RecipientType.CC, internetAddressCC);
            } else if(carbonAddressArr != null && carbonAddressArr.length != 0) {
                InternetAddress[] internetAddressCC = new InternetAddress[carbonAddressArr.length];
                for (int i = 0; i < carbonAddressArr.length; i++) {
                    internetAddressCC[i] = new InternetAddress(carbonAddressArr[i]);
                }
                mailMessage.setRecipients(Message.RecipientType.CC, internetAddressCC);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mailConfig.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个MimeBodyPart
            BodyPart bodyPart = new MimeBodyPart();
            // 设置文本内容
            bodyPart.setContent(mailConfig.getContent(), mailConfig.getTextFormat());
            mainPart.addBodyPart(bodyPart);
            // 为邮件添加附件
            String[] attachFileNames = mailConfig.getAttachFileNameArr();
            // 附件打包
            if (attachFileNames != null && attachFileNames.length > 0) {
                // 存放邮件附件的MimeBodyPart
                MimeBodyPart attachment = null;
                File file = null;
                for (int i = 0; i < attachFileNames.length; i++) {
                    attachment = new MimeBodyPart();
                    // 根据附件文件创建文件数据源
                    file = new File(attachFileNames[i]);
                    FileDataSource fds = new FileDataSource(file);
                    attachment.setDataHandler(new DataHandler(fds));
                    // 为附件设置文件名
                    attachment.setFileName(MimeUtility.encodeWord(
                            file.getName(), "GBK", null));
                    mainPart.addBodyPart(attachment);
                }
            }

            // 附件流不为空，追加附件信息
            if (CollectionUtils.isNotEmpty(mailConfig.getMimeBodyParts())) {

                for (MimeBodyPart mimeBodyPart: mailConfig.getMimeBodyParts()) {
                    if (mimeBodyPart != null) {
                        mainPart.addBodyPart(mimeBodyPart);
                    }
                }
            }

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);

            return true;
        } catch (Exception ex) {
            log.warn("send mail failed, errorMessage:{}", ex);
        }
        return false;
    }

    /**
     * 批量发送邮件，需要设置 MailConfig.toAdressList
     * @param mailConfig
     * @return
     */
    public static SendResult sendMailBatch(MailConfig mailConfig) {

        List<String> toAdressList = mailConfig.getToAdressList();
        if (CollectionUtils.isEmpty(toAdressList)) {
            return SendResult.builder().success(Boolean.TRUE).build();
        }

        List<String> failedAddress = Lists.newArrayList();
        toAdressList.forEach(item -> {
            mailConfig.setToAddress(item);
            Boolean result = sendMail(mailConfig);
            if (!result) {
                failedAddress.add(item);
            }
        });

        if (CollectionUtils.isEmpty(failedAddress)) {
            return SendResult.builder().success(Boolean.TRUE).build();
        }

        /**
         * 返回发送失败的邮件地址
         */
        return SendResult.builder().success(Boolean.FALSE).failedAddress(failedAddress).build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SendResult implements Serializable {

        private static final long serialVersionUID = -5317710296697106381L;
        private Boolean success;
        private List<String> failedAddress;
    }

}
