import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class JedisUtil {
    private static final String HOST = "localhost";
    private static JedisPool jedisPool;
    private static Jedis jedis;

    public static Jedis getJedis() {
        if (jedisPool == null) {
            jedisPool = new JedisPool(new JedisPoolConfig(), HOST);
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    public static void destroyPool() {
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }

    private JedisUtil() {}
}
