package Routers;

import Handler.Services.Service;
import Handler.User.User;
import Handler.User.UsersManager;
import Handler.Widgets.Widget;
import Services.ServicesEnum;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class removeWidgets extends RoutingProperties {

    public removeWidgets() {
        super("/removewidget");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        String email = jsonObject.getString("email");
        JSONObject sv = jsonObject.getJSONObject("widget");

        User user = UsersManager.getManager().getUserByEmail(email);

        Widget widget = WidgetsEnum.getServiceByName(sv.getString("name"));

        JSONObject json = new JSONObject();

        if (widget != null) {
            user.removeWidget(sv.getInt("id"));
        }

        if (widget == null) {
            json.put("res", "false");
            return json;
        }

        return user.getServicesDatas();
    }
}