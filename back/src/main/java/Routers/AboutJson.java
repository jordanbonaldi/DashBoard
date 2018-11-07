package Routers;

import Services.ServicesEnum;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * back
 * Created by Lucas Benkemoun on 06/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class AboutJson extends RoutingProperties {

    public AboutJson() {
        super("/about.json");
    }

    @Override
    public JSONObject view(HttpExchange t) {
        JSONObject json = new JSONObject();

        JSONObject server = new JSONObject();


        JSONArray array = new JSONArray();

        Arrays.asList(ServicesEnum.values()).forEach(e -> {
            JSONObject srv = new JSONObject();
            srv.put("name", e.getService().getName());
            JSONArray widgets = new JSONArray();
            e.getService().getWidgets().forEach(w -> {
                JSONObject widget = new JSONObject();
                widget.put("name", w.getName());
                widget.put("route", w.getRouter().getName());
                widget.put("description", w.getDesc());
                widget.put("refresh", w.getRefresh());

                JSONArray params = new JSONArray();

                w.getParams().forEach(p -> {
                    JSONObject param = new JSONObject();

                    param.put("name", p.getName());
                    param.put("type", p.getType());
                    param.put("value", "");

                    params.put(param);

                });

                widget.put("params", params);

                widgets.put(widget);
            });
            srv.put("widgets", widgets);
            array.put(srv);
        });

        server.put("current_time", System.currentTimeMillis());

        server.put("services", array);

        json.put("server", server);

        JSONObject client = new JSONObject();

        client.put("host", t.getLocalAddress().getHostString());

        json.put("client", client);


        return json;
    }
}
