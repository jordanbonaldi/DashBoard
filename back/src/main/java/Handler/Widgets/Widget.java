package Handler.Widgets;

import Routers.RoutingProperties;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static Utils.Utils.getRequest;
import static Utils.Utils.postRequest;

public class Widget {

    private class AtomicString{
        String atomic;

        public AtomicString(String atom)
        {
            this.atomic = atom;
        }

        public String getAtomic() {
            return atomic;
        }

        public void reset(String atom) {
            this.atomic = atom;
        }

        public void replace(String a, String b) {
            this.atomic = this.atomic.replaceAll(a, b);
        }
    }

    public interface SpecificAction {
        public JSONObject action(HashMap<String, String> params);
    }

    public static class Params {
        private final String name;
        private final String type;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Params(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    public static class Positions{
        private final int x;
        private final int y;

        public Positions(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }


    }

    private final String name;
    private final String desc;
    private final String request;
    private int id;
    private int refresh;

    private Positions position;

    private Positions size;

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Positions getPosition() {
        return position;
    }

    public void setPosition(Positions position) {
        this.position = position;
    }

    public Positions getSize() {
        return size;
    }

    private final SpecificAction action;

    private final RoutingProperties router;

    private final List<Params> params;


    public Widget(String name, String desc, String request, RoutingProperties router, SpecificAction action, int refresh, Positions size, Params ... params) {
        this.name = name;
        this.request = request;
        this.router = router;
        this.action = action;
        this.desc = desc;
        this.refresh = refresh;
        this.params = Arrays.asList(params);
        this.size = size;
        this.position = new Positions(450, 350);
    }

    public Widget(String name, String desc, String request, RoutingProperties router, SpecificAction action, int refresh, Positions size, List<Params> params) {
        this.name = name;
        this.request = request;
        this.router = router;
        this.action = action;
        this.desc = desc;
        this.refresh = refresh;
        this.params = params;
        this.size = size;
        this.position = new Positions(450, 350);
    }

    public Widget(Widget w) {
        this(w.getName(), w.getDesc(), w.getReq(), w.getRouter(), w.getAction(), w.getRefresh(), w.getSize(), w.getParams());
    }

    public String getReq() {
        return request;
    }

    public List<Params> getParams() {
        return params;
    }

    public String getDesc() {
        return desc;
    }

    public SpecificAction getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    private String getHandledRequest(HashMap<String, String> params) {
        AtomicString atomic = new AtomicString(this.request);
        params.forEach(atomic::replace);

        return atomic.getAtomic();
    }

    public JSONObject doRequest(HashMap<String, String> params) {
        try {
            return new JSONObject(getRequest(getHandledRequest(params)));
        } catch (Exception e) {
            return null;
        }
    }


    public RoutingProperties getRouter() {
        return router;
    }
}
