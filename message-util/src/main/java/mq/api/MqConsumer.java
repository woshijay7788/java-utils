package mq.api;


import mq.KafkaConsumer;
import mq.config.MqConsumerConfig;

/**
 * @author chibei
 */
public class MqConsumer {

    private IG2Consumer g2Consumer;
    private String topic;

    /**
     * MqConsumer
     * @param topic topic
     * @param config consumer所需的配置
     * @param consumerHandler 消费者执行器
     */
    public MqConsumer(String topic, MqConsumerConfig config, IConsumerHandler consumerHandler) {
        g2Consumer = new KafkaConsumer(topic, config, consumerHandler);
    }

    /**
     * 启动该topic的消息监听
     */
    public void run() {
        g2Consumer.run();
    }
}
