package Routers;

import Handler.User.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONObject;

import Handler.User.UsersManager;

import static Utils.Utils.convertStreamToString;
import static Utils.Utils.digest;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class Connection extends RoutingProperties {

    public Connection() {
        super("/connection");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        String email = jsonObject.getString("email");
        String password = digest(jsonObject.getString("password"));

        JSONObject json = new JSONObject();

        Object u = UsersManager.getManager().connectUser(email, password);

        if (u instanceof String) {
            System.out.println("Connection failed");
            json.put("res", "false");
            json.put("message", u);
            return json;
        }
        else {
            json.put("res", "true");
            json.put("datas", ((User)u).getServicesDatas());
            return json;
        }

    }
}