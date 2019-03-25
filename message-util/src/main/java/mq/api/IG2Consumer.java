package mq.api;

/**
 * @author chibei
 */
public interface IG2Consumer {
    /**
     * 消费者消费时的方法
     */
    void run();

    /**
     * 停止消费
     */
    void stop();
}
