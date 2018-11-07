package Routers;

import java.util.ArrayList;
import java.util.List;

import Server.ServerHandler;

/**
 * back
 * Created by Lucas Benkemoun on 06/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class RouterManagers {

    List<RoutingProperties> routes;


    public RouterManagers() {
        this.routes = new ArrayList<>();
    }

    public void add(RoutingProperties route) {
        this.routes.add(route);
        ServerHandler.getInstance().addRoute(route);
    }

}
