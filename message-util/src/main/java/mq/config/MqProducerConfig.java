package mq.config;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Properties;
import mq.exception.KafkaErrorEnum;
import mq.exception.KafkaException;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

/**
 * @description: kafka producer配置
 * @author chibei
 */
@Setter
@ToString
public class MqProducerConfig implements Serializable {

    private static final long serialVersionUID = -1356801171660762360L;

    private String bootstrapServers;
    private String acks;
    private Integer retries;
    private Integer batchSize;
    private Integer lingerMs;
    private Long bufferMemory;
    private String keySerializer;
    private String valueSerializer;
    private Integer maxBlockMs;
    //使用阿里云kafka所需要ssl配置
    private Boolean aliFlag;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String securityProtocol;
    private String saslMechanism;


    /**
     * 参数添加默认值
     */
    public MqProducerConfig() {
        this.acks = "1";
        this.retries = 0;
        this.batchSize = 16384;
        this.lingerMs = 10;
        this.bufferMemory = 33554432L;
        this.keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
        this.valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
        this.maxBlockMs = 30 * 1000;
        this.aliFlag = false;
        this.securityProtocol = "SASL_SSL";
        this.saslMechanism = "ONS";
    }

    public Properties getAllProps() {

        if (Strings.isNullOrEmpty(bootstrapServers)) {
            throw new KafkaException(KafkaErrorEnum.ABSENCE_PARAMETER,
                "bootstrap.servers cannot be empty!");
        }

        if (aliFlag && (StringUtils.isBlank(sslTruststoreLocation)
            || StringUtils.isBlank(sslTruststorePassword))) {
            throw new KafkaException(KafkaErrorEnum.ABSENCE_PARAMETER,
                "sslTruststoreLocation、sslTruststorePassword cannot be empty!");
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, this.retries);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, this.maxBlockMs);

        //非必要配置
        props.put(ProducerConfig.ACKS_CONFIG, this.acks);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, this.lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.bufferMemory);

        if (aliFlag) {
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, this.sslTruststoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, this.sslTruststorePassword);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.securityProtocol);
            props.put(SaslConfigs.SASL_MECHANISM, this.saslMechanism);
        }
        return props;
    }


}
