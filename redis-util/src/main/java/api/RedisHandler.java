package api;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import util.SerializeUtils;


/**
 * @author chibei
 * @description Rdeis实现CacheService接口
 */
@Slf4j
public class RedisHandler implements ICacheHandler {

    private static JedisPool jedisPool = null;

    /**
     * <p> Description: [注入cacheConfig交给spring管理] </p>
     *
     * @author chibei
     */
    public RedisHandler(CacheConfig cacheConfig) {
        if (jedisPool == null) {
            init(cacheConfig);
        }
    }

    /**
     * <p> Description: [初始化连接池] </p>
     *
     * @author chibei
     */
    public void init(CacheConfig cacheConfig) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(cacheConfig.getMaxTotal());
            config.setMaxIdle(cacheConfig.getMaxIdle());
            config.setMinIdle(cacheConfig.getMinIdle());
            config.setMaxWaitMillis(cacheConfig.getMaxWait());
            config.setTestOnBorrow(cacheConfig.isTestOnBorrow());
            config.setTestOnReturn(cacheConfig.isTestOnReturn());

            jedisPool = new JedisPool(config, cacheConfig.getHost(),
                cacheConfig.getPort(), cacheConfig.getTimeout(),
                cacheConfig.getPassword(), cacheConfig.getDatabase(),
                cacheConfig.getClientName());

        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-init", e);
        }

        closeLisenter();
    }

    /**
     * <p> Description: [jvm监听 关闭连接池] </p>
     *
     * @author chibei
     */
    public static void closeLisenter() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (jedisPool != null) {
                        jedisPool.close();
                    }
                } catch (Exception e) {
                    log.error("\n 方法[{}]", "RedisHandler-closeLisenter", e);
                }
            }
        });
    }

    /**
     * <p> Description: [从连接池获取Redis实例] </p>
     *
     * @author chibei
     */
    public synchronized Jedis getJedis() {
        Jedis jedis = null;
        if (jedisPool != null) {
            try {
                if (jedis == null) {
                    jedis = jedisPool.getResource();
                }
            } catch (Exception e) {
                log.error("\n 方法[{}]", "RedisHandler-getJedis", e);
            }
        }
        return jedis;
    }

    @Override
    public boolean set(String key, String value) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-set", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setnx(String key, String value) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            Long setnx = jedis.setnx(key, value);
            if (setnx != 0) {
                flag = true;
            }
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setnx", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setnxAndExpire(String key, String value, int seconds) {
        if (setnx(key, value)) {
            return setExpire(key, seconds);
        }
        return false;
    }

    @Override
    public boolean setHash(String key, String field, String value) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.hset(key, field, value);
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setHash", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setAndExpire(String key, String value, int seconds) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            Pipeline p = jedis.pipelined();
            p.set(key, value);
            p.expire(key, seconds);
            p.sync();
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setAndExpire", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setObject(String key, Object object) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.set(key.getBytes(), SerializeUtils.serialize(object));
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setObject", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setObject(String key, Object object, int seconds) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            Pipeline p = jedis.pipelined();
            p.set(key.getBytes(), SerializeUtils.serialize(object));
            p.expire(key, seconds);
            p.sync();
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setObject", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean del(String key) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-del", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = getJedis();
        Long value = null;
        try {
            value = jedis.incr(key);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-incr", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public Long incrBy(String key, long integer) {
        Jedis jedis = getJedis();
        Long value = null;
        try {
            value = jedis.incrBy(key, integer);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-incrBy", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public Long decr(String key) {
        Jedis jedis = getJedis();
        Long value = null;
        try {
            value = jedis.decr(key);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-decr", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public Long decrBy(String key, long integer) {
        Jedis jedis = getJedis();
        Long value = null;
        try {
            value = jedis.decrBy(key, integer);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-decrBy", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public String get(String key) {
        Jedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-get", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public String getHash(String key, String field) {
        Jedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.hget(key, field);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-getHash", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public Object getObject(String key) {
        Jedis jedis = getJedis();
        Object object = null;
        try {
            byte[] dataBytes = jedis.get(key.getBytes());
            if (dataBytes == null) {
                return null;
            }
            object = SerializeUtils.deserialize(dataBytes);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-getObject", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return object;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        Jedis jedis = getJedis();
        Set<String> keySet = null;
        try {
            keySet = jedis.keys(pattern);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-getKeys", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return keySet;
    }

    @Override
    public boolean flushAll() {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.flushAll();
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-flushAll", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean isExist(String key) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            flag = jedis.exists(key);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-isExist", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean setExpire(String key, int endTime) {
        boolean flag = false;
        Jedis jedis = getJedis();
        try {
            jedis.expire(key, endTime);
            flag = true;
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-setExpire", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public Long getTTL(String key) {
        Jedis jedis = getJedis();
        Long seconds = null;
        try {
            seconds = jedis.ttl(key);
        } catch (Exception e) {
            log.error("\n 方法[{}]", "RedisHandler-getTTL", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return seconds;
    }


}
