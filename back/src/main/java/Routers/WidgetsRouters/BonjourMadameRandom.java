package Routers.WidgetsRouters;

import Handler.Widgets.Widget;
import Routers.RoutingProperties;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class BonjourMadameRandom extends RoutingProperties {

    public BonjourMadameRandom() {
        super("/bonjourmadamerandom");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        Widget w = WidgetsEnum.FreeBonjourMadameRandom.getWidget();

        JSONObject obj = w.doRequest(new HashMap<String, String>() {{}});

        if (obj == null)
            return new JSONObject().put("res", "false");
        else return obj;
    }
}

