package Routers;

import Handler.User.User;
import Handler.User.UsersManager;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class getAll extends RoutingProperties {

    public getAll() {
        super("/getall");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        String request = convertStreamToString(t.getRequestBody());

        JSONObject jsonObject = new JSONObject(request);

        String email = jsonObject.getString("email");

        JSONObject json = new JSONObject();

        User u = UsersManager.getManager().getUserByEmail(email);

        if (u == null) {
            json.put("pseudo", "false");
            return json;
        }

        return u.getServicesDatas();
    }
}
