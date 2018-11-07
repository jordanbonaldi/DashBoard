package Routers.WidgetsRouters;

import Handler.Widgets.Widget;
import Routers.RoutingProperties;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.util.HashMap;

import static Utils.Utils.convertStreamToString;

public class CryptoCurrency extends RoutingProperties {

    public CryptoCurrency() {
        super("/crypto");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        Widget w = WidgetsEnum.CryptoCurrencyInformation.getWidget();

        String name = jsonObject.getString(w.getParams().get(0).getName());
        String fiat = jsonObject.getString(w.getParams().get(1).getName());

        System.out.println(name);


        JSONObject obj = w.doRequest(new HashMap<String, String>() {{
            put("@1", name);
            put("@2", fiat);
        }});

        if (obj == null)
            return new JSONObject().put("res", "false");

        return obj;
    }
}

