package mq.exception;

/**
 * @author chibei
 */
public enum KafkaErrorEnum {

    ABSENCE_PARAMETER(100001, "缺少必填参数"),
    INIT_OBJECT_FAILED(100002, "初始化对象失败"),
    ;

    private int code;
    private String message;

    KafkaErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
