package Handler.Services;

import Handler.Widgets.Widget;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Service {

    private final String name;

    private List<Widget> widgets;

    public Service(String name) {
        this.name = name;
        this.widgets = new ArrayList<>();
    }

    public Service addWidget(Widget w) {
        this.widgets.add(w);
        return this;
    }

    public Service addWidgets(Widget ...w) {
        this.widgets.addAll(Arrays.asList(w));
        return this;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public String getName() {
        return this.name;
    }



}
