package Routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * back
 * Created by Lucas Benkemoun on 06/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public abstract class RoutingProperties implements HttpHandler {

    private String name;

    public RoutingProperties(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        String response = this.view(t).toString();

        t.sendResponseHeaders(200, response.length());

        OutputStream os = t.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }

    public abstract JSONObject view(HttpExchange t);
}
