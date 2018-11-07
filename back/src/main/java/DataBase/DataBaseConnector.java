package DataBase;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class DataBaseConnector {

    protected JedisPool       cachePool;
    protected final String	masterIp;
    protected final String    password;

    public DataBaseConnector(final String masterIp, final String password) {
        this.masterIp = masterIp;
        this.password = password;

        System.out.println("[Database] Initializing connection.");
        try {
            this.initiateConnections();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void killConnections() {
        this.cachePool.destroy();
    }

    public void disable() {
        System.out.println("[Disabling Connector] Removing pools...");
        this.killConnections();
    }

    public abstract Jedis getResource() throws Exception;

    public abstract void initiateConnections() throws Exception;

}