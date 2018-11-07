package Routers.WidgetsRouters;

import Handler.Widgets.Widget;
import Routers.RoutingProperties;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.util.HashMap;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class BonjourMadame extends RoutingProperties {

    public BonjourMadame() {
        super("/bonjourmadame");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        Widget w = WidgetsEnum.FreeBonjourMadame.getWidget();

        JSONObject obj = w.doRequest(new HashMap<String, String>() {{}});

        if (obj == null)
            return new JSONObject().put("res", "false");
        else return obj;
    }
}

