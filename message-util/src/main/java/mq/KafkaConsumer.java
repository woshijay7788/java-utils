package mq;


import com.google.common.base.Splitter;
import mq.api.IConsumerHandler;
import mq.api.IG2Consumer;
import mq.config.MqConsumerConfig;
import mq.config.MqRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chibei
 * @description: kafka consumer 消费者
 */
public class KafkaConsumer implements IG2Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private String topic;
    private MqConsumerConfig config;
    private IConsumerHandler consumerHandler;
    private org.apache.kafka.clients.consumer.KafkaConsumer consumer;
    private boolean isRunning = true;

    public KafkaConsumer(String topic, MqConsumerConfig config,
        IConsumerHandler consumerHandler) {
        this.topic = topic;
        this.config = config;
        this.consumerHandler = consumerHandler;
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(config.getAllProps());
    }

    @Override
    public void run() {

        consumer.subscribe(Splitter.on(",").splitToList(topic));

        while (isRunning) {

            ConsumerRecords<String, String> records = consumer.poll(config.getTimeOut());
            for (ConsumerRecord<String, String> record : records) {
                MqRecord mqRecord = new MqRecord(record.offset(), record.key(), record.value());
                LOGGER.info("kafka consumer record :{}", mqRecord.toString());
                consumerHandler.execute(mqRecord);
            }

            //如果不是自动提交offset，则此处提交offset。
            if (!config.getEnableAutoCommit()) {
                consumer.commitSync();
            }
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

}
