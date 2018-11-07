package Routers;

import Handler.Services.Service;
import Handler.User.User;
import Handler.User.UsersManager;
import Services.ServicesEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import static Utils.Utils.convertStreamToString;

/**
 * back
 * Created by Lucas Benkemoun on 07/10/18.
 * Copyright © 2018 Lucas Benkemoun. All rights reserved.
 */

public class removeServices extends RoutingProperties {

    public removeServices() {
        super("/removeservice");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        String email = jsonObject.getString("email");
        String sv = jsonObject.getString("service");

        User user = UsersManager.getManager().getUserByEmail(email);

        Service service = ServicesEnum.getServiceByName(sv);

        JSONObject json = new JSONObject();

        if (service != null) {
            user.removeService(service);
        }

        if (service == null) {
            json.put("res", "false");
            return json;
        }

        return user.getServicesDatas();
    }
}