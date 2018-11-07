package DataBase;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisAPI {

    public interface Connector {
        public Jedis getConnector();
    }

    Connector						c;
    private Map<String, String>	cache	= new HashMap<>();
    String							dbtype;

    private String				name;

    public RedisAPI(final String db, final String name, final Connector c) {
        this.name = name.toLowerCase();
        this.c = c;
        this.dbtype = db;
        this.refresh();
    }

    public boolean contains(final String key) {
        return this.cache.containsKey(key);
    }

    public String get(final String key) {
        if (!this.cache.containsKey(key))
            return null;
        return this.cache.get(key);
    }

    public Map<String, String> getAll() {
        return this.cache;
    }

    public int getInt(final String key) {
        if (this.cache.get(key) == null)
            return 0;
        return Integer.parseInt(this.cache.get(key));
    }

    public void refresh() {
        final Jedis j = this.c.getConnector();
        this.cache = j.hgetAll(this.dbtype + ":" + this.name);
    }

    public void remove() {
        final Jedis j = this.c.getConnector();
        j.del(this.dbtype + ":" + this.name);
        this.refresh();
    }

    public void removeKey(final String key) {
        final Jedis j = this.c.getConnector();
        j.hdel(this.dbtype + ":" + this.name, key);
        this.refresh();
    }

    public String set(final String key, final String value) {
        final Jedis j = this.c.getConnector();
        j.hset(this.dbtype + ":" + this.name, key, value);
        this.refresh();
        return value;
    }

    public int setInt(final String key, final int a) {
        return Integer.parseInt(this.set(key, String.valueOf(a)));
    }

}