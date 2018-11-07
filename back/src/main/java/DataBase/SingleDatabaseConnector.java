package DataBase;

import Main.ServerMain;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SingleDatabaseConnector extends DataBaseConnector {

    public static Jedis getDatabase() {
        try {
            return ServerMain.getMain().getConnector().getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SingleDatabaseConnector(final String masterIp, final String password) {
        super(masterIp, password);
    }

    public Jedis getResource(){
        return this.cachePool.getResource();
    }

    public void initiateConnections() {

        final String[] mainParts = StringUtils.split(this.masterIp, ":");
        final int mainPort = mainParts.length > 1 ? Integer.decode(mainParts[1]) : 6379;


        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(300);
        config.setMaxTotal(500);
        config.setMinIdle(200);
        config.setMaxWaitMillis(100);
        config.setTestOnBorrow(true);

        this.cachePool = new JedisPool(config, mainParts[0], mainPort);

        this.cachePool.getResource().connect();

        System.out.println("[Database] Connection initialized.");

    }


}