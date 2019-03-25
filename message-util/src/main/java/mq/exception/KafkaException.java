package mq.exception;

import java.io.Serializable;
import lombok.Getter;

/**
 * @author chibei
 * @description: kafka 异常
 */
public class KafkaException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 2039353085272376567L;
    @Getter
    private int code;

    public KafkaException(int code, Exception e) {
        super(e);
        this.code = code;
    }

    public KafkaException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public KafkaException(KafkaErrorEnum errorCode) {
        this(errorCode.code(), errorCode.message());
    }

    public KafkaException(KafkaErrorEnum errorCode, String msg) {
        this(errorCode.code(), msg);
    }

}
