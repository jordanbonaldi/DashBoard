package Routers.WidgetsRouters;

import Handler.Widgets.Widget;
import Routers.RoutingProperties;
import Services.WidgetsEnum;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.util.HashMap;

import static Utils.Utils.convertStreamToString;

public class TvShowsInformation extends RoutingProperties {

    public TvShowsInformation() {
        super("/tvshows");
    }

    @Override
    public JSONObject view(HttpExchange t) {

        JSONObject jsonObject = new JSONObject(convertStreamToString(t.getRequestBody()));

        Widget w = WidgetsEnum.TvShowsInformation.getWidget();

        String channelid = jsonObject.getString(w.getParams().get(0).getName());

        System.out.println(channelid);

        JSONObject obj = w.getAction().action(new HashMap<String, String>() {{
            put("@1", "3a02d86df669d3a760d4fdcbf1d2ecae");
            put("@2", channelid);
        }});

        if (obj == null)
            return new JSONObject().put("res", "false");

        return obj;
    }
}

