package mq.config;


import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Properties;
import mq.exception.KafkaErrorEnum;
import mq.exception.KafkaException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

/**
 * @author chibei
 * @description kafka consumer配置
 */
@Setter
@Getter
@ToString
public class MqConsumerConfig implements Serializable {

    private static final long serialVersionUID = -5058648945621391103L;

    private String bootstrapServers;
    private String groupId;
    private Boolean enableAutoCommit;
    private Integer autoCommitInterval;
    private String keyDeserializer;
    private String valueDeserializer;
    private Integer timeOut;
    //使用阿里云kafka所需要ssl配置
    private Boolean aliFlag;
    private Integer sessionTimeoutMs;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String securityProtocol;
    private String saslMechanism;

    /**
     * 参数添加默认值
     */
    public MqConsumerConfig() {
        this.enableAutoCommit = true;
        this.autoCommitInterval = 1000;
        this.keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        this.valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        this.timeOut = 200;
        this.sessionTimeoutMs = 30000;
        this.aliFlag = false;
        this.securityProtocol = "SASL_SSL";
        this.saslMechanism = "ONS";
    }

    public Properties getAllProps() {

        if (Strings.isNullOrEmpty(bootstrapServers)) {
            throw new KafkaException(KafkaErrorEnum.ABSENCE_PARAMETER,
                "bootstrapServers cannot be empty!");
        }

        if (Strings.isNullOrEmpty(groupId)) {
            throw new KafkaException(KafkaErrorEnum.ABSENCE_PARAMETER,
                "groupId cannot be empty!");
        }

        if (aliFlag && (StringUtils.isBlank(sslTruststoreLocation)
            || StringUtils.isBlank(sslTruststorePassword))) {
            throw new KafkaException(KafkaErrorEnum.ABSENCE_PARAMETER,
                "sslTruststoreLocation、sslTruststorePassword cannot be empty!");
        }

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.enableAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, this.autoCommitInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, this.sessionTimeoutMs);

        if (aliFlag) {
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, this.sslTruststoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, this.sslTruststorePassword);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.securityProtocol);
            props.put(SaslConfigs.SASL_MECHANISM, this.saslMechanism);
        }
        return props;
    }
}

