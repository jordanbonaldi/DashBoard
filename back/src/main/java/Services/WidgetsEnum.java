package Services;

import Handler.Services.Service;
import Handler.Widgets.Widget;
import Routers.WidgetsRouters.*;
import Utils.Utils;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static Utils.Utils.getRequest;

public enum WidgetsEnum {

    CityWeather(
            new Widget(
                    "CityWeather",
                    "Affichage de la meteo en fonction d'une ville",
                    "http://api.openweathermap.org/data/2.5/weather?q=@1&appid=60b927b88df31d77863d7e5834970f6c",
                    new Weather(),
                    null,
                    30,
                    new Widget.Positions(450, 200),
                    new Widget.Params("Ville", "string")
            )
    ),
    MinecraftStatus(
            new Widget(
                    "MinecraftStatus",
                    "Information sur un serveur MineCraft",
                    "https://mcapi.us/server/status?ip=@1&port=@2",
                    new MinecraftStatus(),
                    null,
                    10,
                    new Widget.Positions(450, 180),
                    new Widget.Params("Adresse IP du serveur", "string"),
                    new Widget.Params("Port du serveur (par defaut: 25565)", "integer")
            )
    ),
    YoutubeChannelInformation(
            new Widget(
                    "Youtube Channel Information",
                    "Information sur une chaine",
                    "",
                    new YoutubeChannelInformation(),
                    (e) -> {
                        String api = e.get("@1");
                        HttpRequestInitializer init = a -> {};

                        YouTube bd = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), init)
                                .setApplicationName("subcount").build();

                        YouTube.Channels.List searchengine;
                        Channel channel;
                        ChannelStatistics stats;

                        try {
                            searchengine = bd.channels().list("statistics, brandingSettings, snippet");
                            searchengine.setForUsername(e.get("@2"));
                            searchengine.setKey(api);
                            channel = searchengine.execute().getItems().stream().findFirst().orElse(null);
                            stats = channel.getStatistics();

                        } catch (Exception a) {
                            return null;
                        }

                        JSONObject obj = new JSONObject();

                        obj.put("subs", stats.getSubscriberCount());
                        obj.put("views", stats.getViewCount());
                        obj.put("videos", stats.getVideoCount());
                        obj.put("thumbnail", channel.getSnippet().getThumbnails().getDefault());
                        obj.put("country", channel.getSnippet().getCountry());

                        System.out.println(obj.toString());

                        return obj;
                    },
                    10,
                    new Widget.Positions(400, 160),
                    new Widget.Params("Nom de la chaine", "string")
            )
    ),
    YoutubeVideoInformation(
            new Widget(
                    "Youtube Video Information",
                    "Information sur une video",
                    "",
                    new YoutubeVideoInformation(),
                    (e) -> {
                        String api = e.get("@1");
                        HttpRequestInitializer init = a -> {};

                        System.out.println("toto");

                        YouTube bd = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), init)
                                .setApplicationName("videoinfo").build();

                        Video video = null;

                        try {
                            HashMap<String, String> parameters = new HashMap<>();
                            parameters.put("part", "snippet,contentDetails,statistics");
                            parameters.put("id", e.get("@2"));

                            YouTube.Videos.List videosListByIdRequest = bd.videos().list(parameters.get("part").toString());
                            if (parameters.containsKey("id") && parameters.get("id") != "") {
                                videosListByIdRequest.setId(parameters.get("id").toString());
                            }

                            videosListByIdRequest.setKey(api);

                            VideoListResponse response = videosListByIdRequest.execute();

                            video = response.getItems().stream().findFirst().orElse(null);



                        } catch (Exception a) {
                            System.out.println(a.toString());
                            return null; }

                        JSONObject obj = new JSONObject();

                        VideoStatistics stats = video.getStatistics();



                        obj.put("comments", stats.getCommentCount());
                        obj.put("views", stats.getViewCount());
                        obj.put("like", stats.getLikeCount());
                        obj.put("dislike", stats.getDislikeCount());
                        obj.put("miniature", "https://img.youtube.com/vi/" + e.get("@2") + "/hqdefault.jpg");

                        return obj;
                    },
                    10,
                    new Widget.Positions(400, 160),
                    new Widget.Params("ID de la video", "string")
            )
    ),
    CryptoCurrencyInformation(
            new Widget(
                    "CryptoCurrency information",
                    "Information sur une Cryptomonnaie",
                    "https://min-api.cryptocompare.com/data/top/exchanges/full?fsym=@1&tsym=@2",
                    new CryptoCurrency(),
                    null,
                    10,
                    new Widget.Positions(450, 180),
                    new Widget.Params("Nom de la CryptoCurrency", "string"),
                    new Widget.Params("Nom de la monnaie FIAT ou CRYPTO", "string")
            )
    ),
    FreeBonjourMadame(
            new Widget(
                    "BonjourMadame",
                    "Derniere photo de charme de BonjourMadame",
                    "https://bonjourmadame.xorhak.io/api/latest",
                    new BonjourMadame(),
                    null,
                    86400,
                    new Widget.Positions(400, 600)
            )
    ),
    FreeBonjourMadameRandom(
            new Widget(
                    "BonjourMadame Random",
                    "Des photos de charmes aleatoire du site BonjourMadame",
                    "https://bonjourmadame.xorhak.io/api/random",
                    new BonjourMadameRandom(),
                    null,
                    10,
                    new Widget.Positions(400, 600),
                    new Widget.Params("btn", "string")
            )
    ),
    TvShowsInformation(
            new Widget(
                    "TV Show Information",
                    "Information sur serie",
                    "",
                    new TvShowsInformation(),
                    (e) -> {
                        String api = e.get("@1");
                        String query = e.get("@2").replaceAll(" ", "%20");

                        int showid;

                        JSONObject tvshow = null;

                        try {
                            String response = getRequest(
                                    "https://api.themoviedb.org/3/search/tv?api_key=" + api + "&language=en-US&query=" + query + "&page=1");
                            JSONObject obj = new JSONObject(response);
                            JSONObject show = obj.getJSONArray("results").getJSONObject(0);
                            showid = show.getInt("id");

                            response = getRequest(
                                    "https://api.themoviedb.org/3/tv/"+showid+"?api_key="+api+"&language=en-US");
                            tvshow = new JSONObject(response);
                        } catch (Exception a) {
                            return null;
                        }

                        return tvshow;
                    },
                    30,
                    new Widget.Positions(400, 420),
                    new Widget.Params("Nom de la serie", "string")
            )
    );

    private final Widget widget;

    WidgetsEnum(Widget widget) {
        this.widget = widget;
    }

    public static Widget getServiceByName(String name) {
        return Arrays.stream(values()).filter(e -> e.getWidget().getName().equalsIgnoreCase(name)).map(WidgetsEnum::getWidget).findFirst().orElse(null);
    }

    public Widget getWidget() {
        return widget;
    }
}
