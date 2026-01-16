
package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static JedisPool pool;
    static Gson gson = new Gson();

    public static void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        
        pool = new JedisPool(poolConfig, "localhost", 6379);
        
        System.out.println("Redis: Inserting 10,000 students...");
        try (Jedis jedis = pool.getResource()) {
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Student Name " + i, "Computer Science");
                jedis.set(id, gson.toJson(s));
            }
        }
        System.out.println("Redis: 10,000 students inserted successfully!");
    }

    public static Student get(String id) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(id);
            return json != null ? gson.fromJson(json, Student.class) : null;
        }
    }
}
