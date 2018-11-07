package Routers;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONObject;

import Handler.User.User;
import Handler.User.UsersManager;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class IsKnown extends RoutingProperties {

    public IsKnown() {
        super("/is_known");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        String request = convertStreamToString(t.getRequestBody());

        JSONObject jsonObject = new JSONObject(request);

        String email = jsonObject.getString("email");

        System.out.println(email);

        JSONObject json = new JSONObject();

        User u = UsersManager.getManager().getUserByEmail(email);

        System.out.println(u);

        if (u == null) {
            json.put("res", "false");
            return json;
        }

        json.put("res", "true");
        System.out.println(json.toString());
        json.put("datas", u.getServicesDatas());
        return json;
    }
}
