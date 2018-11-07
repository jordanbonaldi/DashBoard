package Handler.User;

import DataBase.RedisAPI;
import DataBase.SingleDatabaseConnector;
import Handler.Services.Service;
import Handler.Widgets.Widget;
import Services.ServicesEnum;
import Services.WidgetsEnum;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class User {

    private String email;

    private String password;

    private String pseudo;

    private Jedis jedis;

    private UUID uuid;

    private RedisAPI api;

    public User(String email) {
        this.email = email;
        this.jedis = SingleDatabaseConnector.getDatabase();

        this.buildUUID();
        this.enableAPI();
    }

    private void buildUUID() {
        this.uuid = UUID.nameUUIDFromBytes(this.email.getBytes());
    }

    public boolean isKnown() {
        return this.jedis.exists("user:" + this.uuid.toString());
    }

    private void enableAPI() {
        this.api = new RedisAPI("user", this.uuid.toString(), () -> this.jedis);
    }

    public void newValue(String value, String key){
        this.api.set(value, key);
    }

    public String getValue(String value) {
        return this.api.get(value);
    }

    public boolean isPasswordMatch(String password) {
        if (this.password == null)
            this.password = this.api.get("password");
        return this.password.equals(password);
    }

    public void addService(Service service) {
        String sv;

        if (this.api.contains("services")) {
            sv = this.api.get("services");
            sv = sv + "@" + service.getName();
        } else
            sv = service.getName();

        this.api.set("services", sv);
    }

    public void removeService(Service service) {
        List<Service> services = this.getServices();
        if (services.size() == 0)
            return;
        else if (services.size() == 1) {
            this.api.removeKey("services");
            return;
        }
        System.out.println(services.size());
        services = services.stream().filter(e -> e != service).collect(Collectors.toList());
        System.out.println(services.size());
        StringBuilder sb = new StringBuilder();
        AtomicInteger i = new AtomicInteger(0);
        services.forEach(e -> {
            if (i.get() == 0)
                sb.append(e.getName());
            else
                sb.append("@").append(e.getName());
            i.incrementAndGet();
        });
        this.api.set("services", sb.toString());
    }

    public void removeWidget(int id) {
        System.out.println("Removing id : " + id);
        this.api.removeKey("widgets_" + id);
    }

    public List<Service> getServices() {
        List<Service> list = new ArrayList<>();
        if (!this.api.contains("services"))
            return list;
        list.addAll(Arrays.stream(this.api.get("services").split("@")).map(ServicesEnum::getServiceByName).collect(Collectors.toList()));

        return list;
    }

    public Widget getWidget(int nb) {
        return WidgetsEnum.getServiceByName(this.api.get("widgets_" + nb));
    }

    public List<Widget> getWidgets() {
        List<Widget> list = new ArrayList<>();
        int max = this.api.getInt("widgets_nb");

        for (int i = 0; i <= max; i++)
            if (this.api.contains("widgets_" +  i)) {
                Widget clone = new Widget(WidgetsEnum.getServiceByName(this.api.get("widgets_" + i)));
                clone.setId(i);
                if (this.api.contains("widgets_" +  i + "_refresh"))
                    clone.setRefresh(this.api.getInt("widgets_" +  i + "_refresh"));
                list.add(clone);
            }

        return list;
    }

    private List<Widget> getWidgetInService(Service service) {
        return this.getWidgets()
               .stream()
               .filter(e ->
                       service.getWidgets().stream().filter(a -> a.getName().equalsIgnoreCase(e.getName())).findFirst().orElse(null) != null
               )
               .collect(Collectors.toList());
    }

    public JSONObject getServicesDatas() {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();

        this.getServices().forEach(e -> {
            JSONObject srv = new JSONObject();
            srv.put("name", e.getName());
            JSONArray widgets = new JSONArray();
            this.getWidgetInService(e).forEach(w -> {
                JSONObject widget = new JSONObject();
                widget.put("name", w.getName());
                widget.put("route", w.getRouter().getName());
                widget.put("description", w.getDesc());
                widget.put("id", w.getId());
                widget.put("refresh", w.getRefresh());

                Widget.Positions pos = this.getPositionOfWidget(w, w.getId());

                widget.put("x", pos.getX());
                widget.put("y", pos.getY());

                widget.put("w", w.getSize().getX());
                widget.put("h", w.getSize().getY());

                JSONArray params = new JSONArray();

                this.getParamsForWidget(w).forEach(p -> {
                    JSONObject param = new JSONObject();

                    param.put("name", p.getName());
                    param.put("value", p.getValue());
                    param.put("type", p.getType());

                    params.put(param);

                });

                widget.put("params", params);

                widgets.put(widget);
            });
            srv.put("widgets", widgets);
            srv.put("last", this.api.getInt("widgets_nb"));
            array.put(srv);
        });
        obj.put("datas", array);
        return obj;
    }

    public Widget.Positions getPositionOfWidget(Widget w, int nb) {
        if (this.api.contains(w.getName()+ "_" + nb + "_x") &&
                this.api.contains(w.getName()+ "_" + nb + "_y")) {
            return new Widget.Positions(this.api.getInt(w.getName() + "_" + nb + "_x"), this.api.getInt(w.getName() + "_" + nb + "_y"));
        }
        return w.getPosition();
    }

    public void setPositionOfWidget(Widget w, int nb, Widget.Positions pos) {
        this.api.setInt(w.getName()+ "_" + nb + "_x", pos.getX());
        this.api.setInt(w.getName()+ "_" + nb + "_y", pos.getY());
    }

    public void setParamsForWidgets(Widget w, List<String> params, int nb) {
        AtomicInteger i = new AtomicInteger(0);

        this.api.setInt("widgets_" +  nb + "_refresh", w.getRefresh());

        if (w.getParams().size() == 1) {
            this.api.set(w.getName() + "_"+nb+"_params_" + i.get(), params.get(0));
            return;
        }

        params.forEach(e -> this.api.set(w.getName() + "_"+nb+"_params_" + i.getAndIncrement(), e));
    }

    private List<Widget.Params> getParamsForWidget(Widget w) {
        List<Widget.Params> params = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(0);
        int nb = w.getId();

        if (w.getParams().size() == 1) {
            Widget.Params pa = w.getParams().get(i.get());
            Widget.Params clone = new Widget.Params(pa.getName(), pa.getType());
            clone.setValue(this.api.get(w.getName() + "_"+nb+"_params_" + i.get()));
            params.add(clone);
        } else
            w.getParams().forEach(e -> {
                Widget.Params clone = new Widget.Params(e.getName(), e.getType());
                System.out.println(this.api.get(w.getName() + "_"+nb+"_params_" + i.get()));
                clone.setValue(this.api.get(w.getName() + "_"+nb+"_params_" + i.getAndIncrement()));
                params.add(clone);
            });

        return params;
    }

    public void addWidgets(Widget widget, List<String> params) {
        if (!this.api.contains("widgets_nb"))
            this.api.setInt("widgets_nb", 1);
        else
            this.api.setInt("widgets_nb", this.api.getInt("widgets_nb") + 1);

        this.setParamsForWidgets(widget, params, this.api.getInt("widgets_nb"));
        this.api.set("widgets_" + this.api.getInt("widgets_nb"), widget.getName());
        this.api.setInt("widgets_" + this.api.getInt("widgets_nb") + "_refresh", widget.getRefresh());
    }

    public void persists() {
        this.newValue("email", this.email);
        this.newValue("pseudo", this.pseudo);
        this.newValue("password", this.password);
    }

    public String getEmail() {
        if (this.email == null)
            this.email = this.api.get("email");
        return email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPseudo() {
        if (pseudo == null)
            this.pseudo = this.api.get("pseudo");

        return pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
