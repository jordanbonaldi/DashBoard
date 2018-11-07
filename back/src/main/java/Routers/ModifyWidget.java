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

public class ModifyWidget extends RoutingProperties {

    public ModifyWidget() {
        super("/modifywidget");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        String email = jsonObject.getString("email");
        JSONObject sv = jsonObject.getJSONObject("widget");
        JSONArray array = sv.getJSONArray("params");

        List<String> params = new ArrayList<>();

        array.forEach(e -> {
           JSONObject obj = (JSONObject)e;
           params.add(obj.getString("value"));
        });

        User user = UsersManager.getManager().getUserByEmail(email);

        Widget widget = WidgetsEnum.getServiceByName(sv.getString("name"));


        JSONObject json = new JSONObject();

        if (widget != null) {
            Object o = sv.get("refresh");
            if (o instanceof Integer)
                widget.setRefresh((int)o);
            else
                widget.setRefresh(Integer.valueOf((String)o));

            user.setParamsForWidgets(widget, params, sv.getInt("id"));
        }

        if (widget == null) {
            json.put("res", "false");
            return json;
        }
        return user.getServicesDatas();
    }
}