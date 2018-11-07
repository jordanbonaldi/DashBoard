package Server;

import com.sun.net.httpserver.HttpServer;
import Main.ServerMain;

import java.io.IOException;
import java.net.InetSocketAddress;

import Routers.RoutingProperties;

/**
 * back
 * Created by Lucas Benkemoun on 06/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class ServerHandler {

    private HttpServer server;

    public static ServerHandler getInstance() {
        return ServerMain.getMain().getServer();
    }

    public ServerHandler() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public void addRoute(RoutingProperties properties) {
        this.server.createContext(properties.getName(), properties);
    }

    public void start() {
        this.server.setExecutor(null);
        this.server.start();
    }
}
