package mq.config;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chibei
 * @description mq 内容
 */
@Getter
@Setter
@ToString
public class MqRecord implements Serializable{

    private static final long serialVersionUID = 6045264331503392025L;

    private String key;
    private String value;
    private Long offset;

    public MqRecord(Long offset, String key, String value) {
        this.offset = offset;
        this.key = key;
        this.value = value;
    }
}
