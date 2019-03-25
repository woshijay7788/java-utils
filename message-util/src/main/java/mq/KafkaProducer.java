package mq;

import java.util.concurrent.Future;
import mq.api.IG2Producer;
import mq.config.MqProducerConfig;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chibei
 * @description: kafka producer 生产者
 */
public class KafkaProducer implements IG2Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private Producer producer;

    public KafkaProducer(MqProducerConfig config) {
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer(config.getAllProps());
    }

    @Override
    public boolean send(String topic, String key, String value) {
        return this.send(topic, null, null, key, value, null);
    }

    @Override
    public boolean send(String topic, Integer partition, Long timestamp, String key, String value) {
        return send(topic, partition, timestamp, key, value, null);
    }

    @Override
    public boolean send(String topic, String key, String value, Callback callback) {
        return send(topic, null, null, key, value, callback);
    }

    @Override
    public boolean send(String topic, Integer partition, Long timestamp, String key, String value,
        Callback callback) {

        boolean result = true;
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, timestamp,
            key, value);

        try {

            Future<RecordMetadata> metadataFuture = producer.send(record, callback);
            // callback为空按照同步发送处理
            if (callback == null) {
                RecordMetadata recordMetadata = metadataFuture.get();
                LOGGER.info("kafaka msg monitor is : {}", recordMetadata.toString());
            }

        } catch (Exception e) {
            result = false;
            LOGGER.error("kafka send msg has error : {}", ExceptionUtils.getStackTrace(e));
        }

        return result;
    }

    @Override
    public void flush() {
        if (producer != null) {
            producer.flush();
        }
    }


    @Override
    public void close() {
        if (producer != null) {
            this.producer.close();
        }
    }

}
