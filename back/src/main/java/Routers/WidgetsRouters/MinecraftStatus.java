package Routers.WidgetsRouters;

import Handler.Widgets.Widget;
import Routers.RoutingProperties;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static Utils.Utils.convertStreamToString;
import static Utils.Utils.postRequest;

public class MinecraftStatus extends RoutingProperties {

    public MinecraftStatus() {
        super("/mcstatus");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));
        Widget w = WidgetsEnum.MinecraftStatus.getWidget();
        String ip = jsonObject.getString(w.getParams().get(0).getName());
        String port = jsonObject.getString(w.getParams().get(1).getName());

        System.out.println(ip);
        System.out.println(port);

        JSONObject obj = w.doRequest(new HashMap<String, String>() {{
            put("@1", ip);
            put("@2", port);
        }});

        if (obj == null)
            return new JSONObject().put("res", "false");

        obj.remove("motd_extra");
        obj.remove("motd_formatted");
        obj.remove("motd");

        return obj;
    }
}

