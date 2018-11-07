package Routers;

import Handler.User.User;
import Handler.User.UsersManager;
import Handler.Widgets.Widget;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class resizeWidgets extends RoutingProperties {

    public resizeWidgets() {
        super("/resizewidgets");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        System.out.println("wwww");

        String email = jsonObject.getString("email");
        JSONObject sv = jsonObject.getJSONObject("widget");
        String name = sv.getString("name");
        int x = jsonObject.getInt("x");
        int y  = jsonObject.getInt("y");

        System.out.println("Resizing to " + x + " " + y);

        User user = UsersManager.getManager().getUserByEmail(email);

        Widget widget = WidgetsEnum.getServiceByName(name);

        JSONObject json = new JSONObject();

        if (widget != null) {
            Widget.Positions pos = new Widget.Positions(x, y);
            user.setPositionOfWidget(widget, sv.getInt("id"), pos);
        }

        if (widget == null) {
            json.put("res", "false");
            return json;
        }
        return user.getServicesDatas();
    }
}