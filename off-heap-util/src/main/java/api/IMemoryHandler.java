package api;


/**
 * @description 处理堆外内存的工具类
 * @author chibei
 */
public interface IMemoryHandler {

    /**
     * 通过key获取value值
     * @param key
     * @return
     */
    String getValue(String key);

    /**
     * 向堆内内存放值
     * @param key
     * @param value
     * @return
     */
    boolean setValue(String key, String value);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean deleteKey(String key);
}
