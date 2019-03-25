package mq.api;

import org.apache.kafka.clients.producer.Callback;

/**
 * @author chibei
 */
public interface IG2Producer {

    /**
     * 往MQ中同步添加一条消息
     *
     * @param topic topicName
     * @param key the Key
     * @param value the Value
     */
    boolean send(String topic, String key, String value);

    /**
     * 往MQ中异步添加一条消息
     *
     * @param topic topicName
     * @param key the Key
     * @param value the Value
     * @param callback the callback function
     */
    boolean send(String topic, String key, String value, Callback callback);


    /**
     * 同步发送信息,进行重载
     * @param topic
     * @param partition
     * @param timestamp
     * @param key
     * @param value
     * @return
     */
    boolean send(String topic, Integer partition, Long timestamp, String key, String value);


    /**
     * 异步发送信息,进行重载
     * @param topic
     * @param partition
     * @param timestamp
     * @param key
     * @param value
     * @return
     */
    boolean send(String topic, Integer partition, Long timestamp, String key, String value,
        Callback callback);

    /**
     * 刷新producer
     */
    void flush();

    /**
     * 关闭producer
     */
    void close();

}
