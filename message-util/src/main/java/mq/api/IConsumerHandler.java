package mq.api;


import mq.config.MqRecord;

/**
 * 消费消息的执行器
 * @author chibei
 */
public interface IConsumerHandler {
    /**
     * 消费者消费消息时所要执行的方法
     * @param record 一条消息记录
     */
    void execute(MqRecord record);
}
