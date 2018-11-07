package Main;

/*
 * epitech.project
 * Created by Lucas Benkemoun on 04/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

import DataBase.DataBaseConnector;
import DataBase.RedisServer;
import DataBase.SingleDatabaseConnector;
import Handler.User.UsersManager;
import Handler.Widgets.Widget;
import Routers.*;
import Routers.WidgetsRouters.Weather;
import Server.ServerHandler;
import Services.ServicesEnum;
import Services.WidgetsEnum;

import java.util.Arrays;

public class ServerMain {

    private ServerHandler server;

    private static ServerMain main;

    private DataBaseConnector connector;

    private UsersManager um;

    public UsersManager getUm() {
        return um;
    }

    public DataBaseConnector getConnector() {
        return connector;
    }

    public static ServerMain getMain() {
        return main;
    }

    public ServerHandler getServer() {
        return server;
    }

    private void mainHandle() throws Exception {

        this.connector = new SingleDatabaseConnector(RedisServer.host + ":" + RedisServer.port, RedisServer.auth);

        this.um = new UsersManager();

        this.server = new ServerHandler();
        this.server.addRoute(new AboutJson());
        this.server.addRoute(new CreateAccount());
        this.server.addRoute(new Connection());
        this.server.addRoute(new IsKnown());
        this.server.addRoute(new getPseudo());
        this.server.addRoute(new addServices());
        this.server.addRoute(new addWidgets());
        this.server.addRoute(new getAll());
        this.server.addRoute(new removeServices());
        this.server.addRoute(new removeWidgets());
        this.server.addRoute(new ModifyWidget());
        this.server.addRoute(new resizeWidgets());
        Arrays.asList(ServicesEnum.values()).forEach(e -> e.getService().getWidgets().forEach(w -> this.server.addRoute(w.getRouter())));
        this.server.start();
    }

    public static void main(String[] args) throws Exception {
        main = new ServerMain();

        main.mainHandle();
    }
}
