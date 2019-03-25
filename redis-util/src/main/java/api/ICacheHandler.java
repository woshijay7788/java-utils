package api;

import java.util.Set;

/**
 * @author chibei
 * @description [缓存服务接口]
 */
public interface ICacheHandler {

    //********************************增*********************************//

    /**
     * @description [根据key存入元素]
     * @author chibei
     */
    boolean set(String key, String value);

    /**
     * @description [如果key不存在，则存入]
     * @author chibei 2017/12/6
     */
    boolean setnx(String key, String value);

    /**
     * @description [如果不存在key，则存入, 时效为妙]
     * @author chibei 2017/12/6
     */
    boolean setnxAndExpire(String key, String value, int seconds);
    /**

     * @description [根据key存入元素 字节流]

     *
     * @author chibei

     */
    //void setByte(byte[] key, byte[] value);

    /**
     * @description [根据key field进行Hash存入]
     * @author chibei
     */
    boolean setHash(String key, String field, String value);

    /**
     * @description [根据key存入带有生命周期的元素]
     * @author chibei
     */
    boolean setAndExpire(String key, String value, int seconds);
    /**

     * @description [根据key存入带有生命周期的元素 字节流]

     *
     * @author chibei

     */
    //String setAndExpireByte(byte[] key, byte[] value, int seconds);

    /**
     * @description [根据key存入对象]
     * @author chibei
     */
    boolean setObject(String key, Object object);

    /**
     * @description [根据key存入带有生命周期的对象]
     * @author chibei
     */
    boolean setObject(String key, Object object, int seconds);

    //********************************删*********************************//

    /**
     * @description [根据key删除元素]
     * @author chibei
     */
    boolean del(String key);
    /**

     * @description [根据key删除元素 字节流]

     *
     * @author chibei

     */
    //void delByte(final byte[] key);

    //********************************改*********************************//

    /**
     * @description [每次加1]
     * @author chibei 6
     */
    Long incr(String key);

    /**
     * @description [每次加integer]
     * @author chibei 6
     */
    Long incrBy(String key, long integer);

    /**
     * @description [每次减1]
     * @author chibei 6
     */
    Long decr(String key);

    /**
     * @description [每次减integer]
     * @author chibei 6
     */
    Long decrBy(String key, long integer);
    //********************************查*********************************//

    /**
     * @description [根据key查找元素]
     * @author chibei
     */
    String get(String key);
    /**

     * @description [根据key删除元素 字节流]

     *
     * @author chibei

     */
    //byte[] getByte(final byte[] key);

    /**
     * @description [根据key field进行Hash查询]
     * @author chibei
     */
    String getHash(String key, String field);

    /**
     * @description [根据key查找对象]
     * @author chibei
     */
    Object getObject(String key);

    /**
     * @description [根据规则查找所有key]
     * @author chibei
     */
    Set<String> getKeys(String pattern);

    /**
     * @description [根据规则查找所有key 字节流]
     *
     * @author chibei
     */
    //Set<byte[]> getKeysByte(String pattern);

    //********************************其它*********************************//

    /**
     * @description [清空所有服务器缓存]
     * @author chibei
     */
    boolean flushAll();

    /**
     * @description [key是否存在]
     * @author chibei
     */
    boolean isExist(String key);

    /**
     * @description [设置key的到期时间]
     * @author chibei
     */
    boolean setExpire(String key, int endTime);

    /**
     * @description [key的剩余到期时间]
     * @author chibei
     */
    Long getTTL(String key);


}
