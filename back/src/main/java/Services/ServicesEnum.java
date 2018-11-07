package Services;

import Handler.Services.Service;
import Handler.Widgets.Widget;

import java.util.Arrays;

public enum ServicesEnum {

    WeatherService(new Service("Weather").addWidget(WidgetsEnum.CityWeather.getWidget())),
    MineCraftService(new Service("MineCraft").addWidget(WidgetsEnum.MinecraftStatus.getWidget())),
    YoutubeService(new Service("Youtube")
            .addWidgets(
                    WidgetsEnum.YoutubeChannelInformation.getWidget(),
                    WidgetsEnum.YoutubeVideoInformation.getWidget()
            )),
    BonjourMadameService(new Service("BonjourMadame").addWidgets(WidgetsEnum.FreeBonjourMadame.getWidget(), WidgetsEnum.FreeBonjourMadameRandom.getWidget())),
    CryptoCurrencyService(new Service("CryptoCurrency").addWidget(WidgetsEnum.CryptoCurrencyInformation.getWidget())),
    TvShows(new Service("TvShows").addWidget(WidgetsEnum.TvShowsInformation.getWidget()));

    private final Service service;

    ServicesEnum(Service service) {
        this.service = service;
    }

    public static Service getServiceByName(String name) {
        return Arrays.stream(values()).filter(e -> e.getService().getName().equalsIgnoreCase(name)).map(ServicesEnum::getService).findFirst().orElse(null);
    }

    public Service getService() {
        return service;
    }
}
