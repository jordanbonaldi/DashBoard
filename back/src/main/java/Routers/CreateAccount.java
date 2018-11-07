package Routers;

import Handler.User.User;
import Handler.User.UsersManager;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONObject;

import static Utils.Utils.convertStreamToString;
import static Utils.Utils.digest;

/**
 * back
 * Created by Lucas Benkemoun on 06/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class CreateAccount extends RoutingProperties {

    public CreateAccount() {
        super("/create_account");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        String request = convertStreamToString(t.getRequestBody());

        JSONObject jsonObject = new JSONObject(request);

        String email = jsonObject.getString("email");
        String password = digest(jsonObject.getString("password"));
        String pseudo = jsonObject.getString("pseudo");

        User user = UsersManager.getManager().getUser(email, password, pseudo);


        if (user.isKnown()) {
            JSONObject json = new JSONObject();
            json.put("res", "false");
            json.put("message", "Known User");
            return json;
        }

        System.out.println("Persisting on : " + user.getUuid().toString());

        user.persists();

        JSONObject json = new JSONObject();
        json.put("res", "true");
        json.put("datas", user.getServicesDatas());
        return json;
    }
}