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

public class Weather extends RoutingProperties {

    public Weather() {
        super("/weather");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));


        Widget w = WidgetsEnum.CityWeather.getWidget();

        String city = jsonObject.getString(w.getParams().get(0).getName());

        JSONObject obj = w.doRequest(new HashMap<String, String>() {{
            put("@1", city);
        }});

        if (obj == null)
            return new JSONObject().put("res", "false");
        else return obj;
    }
}

