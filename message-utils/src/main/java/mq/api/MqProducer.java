package mq.api;

import mq.KafkaProducer;
import mq.config.MqProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chibei
 */
public class MqProducer {

    private IProducer producer;
    private static final Logger LOG = LoggerFactory.getLogger(MqProducer.class);

    public MqProducer(MqProducerConfig config) {
        this.producer = new KafkaProducer(config);
    }

    /**
     * 往topic中发送一条消息
     *
     * @param topic topic
     * @param key key
     * @param value value
     * @return true/false
     */
    public boolean send(String topic, String key, String value) {
        return producer.send(topic, key, value);
    }

    /**
     * 往topic中发送一条消息 重载
     * @param topic
     * @param partition
     * @param timestamp
     * @param key
     * @param value
     * @return
     */
    public boolean send(String topic, Integer partition, Long timestamp, String key, String value) {
        return producer.send(topic, partition, timestamp, key, value);
    }

}
