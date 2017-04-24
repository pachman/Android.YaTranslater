package com.example.alexander.yatranslator.retrofit;

import android.content.Context;
import android.util.Log;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.models.SupportLanguages;
import com.example.alexander.yatranslator.models.TranslatedPhrase;
import io.reactivex.Observable;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class YandexTranslateClient implements TranslateClient {
    private static final String translateApiKey = "trnsl.1.1.20170322T195409Z.2b9c4508c62c6808.a6fc70f9fd22b9b152fadc394a7d2d55ebfd04cf";
    private static final String baseUrl = "https://translate.yandex.net/api/v1.5/";
    private static final int timeoutInSeconds = 30;
    private static final int maxStaleInDays = 3;
    private static final int maxAgeInMinutes = 3;
    private static final int cacheSize = 5 * 1024 * 1024; // 5 MiB
    private static final String cacheControlHeader = "Cache-Control";
    private static final String cacheStorageName = R.string.app_name + "_CacheStorage";

    private final TranslateApi translateApi;

    public YandexTranslateClient(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), cacheStorageName);
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        translateApi = retrofit.create(TranslateApi.class);
    }

    @Override
    public Observable<TranslatedPhrase> translate(String text, String direction) {
        return translateApi.translate(translateApiKey, direction, text)
                .map(data -> {
                    TranslatedPhrase translatedPhrase = new TranslatedPhrase();
                    translatedPhrase.setLang(data.getLang());
                    translatedPhrase.setText(data.getText());

                    return translatedPhrase;
                });
    }

    @Override
    public Observable<SupportLanguages> getLanguages(String uiLang) {
        return translateApi.getLangs(translateApiKey, uiLang)
                .map(data -> {
                    SupportLanguages supportLanguages = new SupportLanguages();
                    supportLanguages.setLangs(data.getLangs());
                    supportLanguages.setDirs(data.getDirs());
                    Log.d("[Debug Lang]", "API getLanguages " + uiLang + " " + data.getLangs() + " " + data.getDirs());
                    return supportLanguages;
                });
    }

    @Override
    public SupportLanguages getConstantLanguages() {
        HashMap<String, String> langs = new HashMap<>();
        langs.put("af", "Африкаанс");
        langs.put("am", "Амхарский");
        langs.put("ar", "Арабский");
        langs.put("az", "Азербайджанский");
        langs.put("ba", "Башкирский");
        langs.put("be", "Белорусский");
        langs.put("bg", "Болгарский");
        langs.put("bn", "Бенгальский");
        langs.put("bs", "Боснийский");
        langs.put("ca", "Каталанский");
        langs.put("ceb", "Себуанский");
        langs.put("cs", "Чешский");
        langs.put("cy", "Валлийский");
        langs.put("da", "Датский");
        langs.put("de", "Немецкий");
        langs.put("el", "Греческий");
        langs.put("en", "Английский");
        langs.put("eo", "Эсперанто");
        langs.put("es", "Испанский");
        langs.put("et", "Эстонский");
        langs.put("eu", "Баскский");
        langs.put("fa", "Персидский");
        langs.put("fi", "Финский");
        langs.put("fr", "Французский");
        langs.put("ga", "Ирландский");
        langs.put("gd", "Шотландский (гэльский)");
        langs.put("gl", "Галисийский");
        langs.put("gu", "Гуджарати");
        langs.put("he", "Иврит");
        langs.put("hi", "Хинди");
        langs.put("hr", "Хорватский");
        langs.put("ht", "Гаитянский");
        langs.put("hu", "Венгерский");
        langs.put("hy", "Армянский");
        langs.put("id", "Индонезийский");
        langs.put("is", "Исландский");
        langs.put("it", "Итальянский");
        langs.put("ja", "Японский");
        langs.put("jv", "Яванский");
        langs.put("ka", "Грузинский");
        langs.put("kk", "Казахский");
        langs.put("km", "Кхмерский");
        langs.put("kn", "Каннада");
        langs.put("ko", "Корейский");
        langs.put("ky", "Киргизский");
        langs.put("la", "Латынь");
        langs.put("lb", "Люксембургский");
        langs.put("lo", "Лаосский");
        langs.put("lt", "Литовский");
        langs.put("lv", "Латышский");
        langs.put("mg", "Малагасийский");
        langs.put("mhr", "Марийский");
        langs.put("mi", "Маори");
        langs.put("mk", "Македонский");
        langs.put("ml", "Малаялам");
        langs.put("mn", "Монгольский");
        langs.put("mr", "Маратхи");
        langs.put("mrj", "Горномарийский");
        langs.put("ms", "Малайский");
        langs.put("mt", "Мальтийский");
        langs.put("my", "Бирманский");
        langs.put("ne", "Непальский");
        langs.put("nl", "Голландский");
        langs.put("no", "Норвежский");
        langs.put("pa", "Панджаби");
        langs.put("pap", "Папьяменто");
        langs.put("pl", "Польский");
        langs.put("pt", "Португальский");
        langs.put("ro", "Румынский");
        langs.put("ru", "Русский");
        langs.put("si", "Сингальский");
        langs.put("sk", "Словацкий");
        langs.put("sl", "Словенский");
        langs.put("sq", "Албанский");
        langs.put("sr", "Сербский");
        langs.put("su", "Сунданский");
        langs.put("sv", "Шведский");
        langs.put("sw", "Суахили");
        langs.put("ta", "Тамильский");
        langs.put("te", "Телугу");
        langs.put("tg", "Таджикский");
        langs.put("th", "Тайский");
        langs.put("tl", "Тагальский");
        langs.put("tr", "Турецкий");
        langs.put("tt", "Татарский");
        langs.put("udm", "Удмуртский");
        langs.put("uk", "Украинский");
        langs.put("ur", "Урду");
        langs.put("uz", "Узбекский");
        langs.put("vi", "Вьетнамский");
        langs.put("xh", "Коса");
        langs.put("yi", "Идиш");
        langs.put("zh", "Китайский");

        LinkedList<String> dirs = new LinkedList<>();
        dirs.add("az-ru");
        dirs.add("be-bg");
        dirs.add("be-cs");
        dirs.add("be-de");
        dirs.add("be-en");
        dirs.add("be-es");
        dirs.add("be-fr");
        dirs.add("be-it");
        dirs.add("be-pl");
        dirs.add("be-ro");
        dirs.add("be-ru");
        dirs.add("be-sr");
        dirs.add("be-tr");
        dirs.add("bg-be");
        dirs.add("bg-ru");
        dirs.add("bg-uk");
        dirs.add("ca-en");
        dirs.add("ca-ru");
        dirs.add("cs-be");
        dirs.add("cs-en");
        dirs.add("cs-ru");
        dirs.add("cs-uk");
        dirs.add("da-en");
        dirs.add("da-ru");
        dirs.add("de-be");
        dirs.add("de-en");
        dirs.add("de-es");
        dirs.add("de-fr");
        dirs.add("de-it");
        dirs.add("de-ru");
        dirs.add("de-tr");
        dirs.add("de-uk");
        dirs.add("el-en");
        dirs.add("el-ru");
        dirs.add("en-be");
        dirs.add("en-ca");
        dirs.add("en-cs");
        dirs.add("en-da");
        dirs.add("en-de");
        dirs.add("en-el");
        dirs.add("en-es");
        dirs.add("en-et");
        dirs.add("en-fi");
        dirs.add("en-fr");
        dirs.add("en-hu");
        dirs.add("en-it");
        dirs.add("en-lt");
        dirs.add("en-lv");
        dirs.add("en-mk");
        dirs.add("en-nl");
        dirs.add("en-no");
        dirs.add("en-pt");
        dirs.add("en-ru");
        dirs.add("en-sk");
        dirs.add("en-sl");
        dirs.add("en-sq");
        dirs.add("en-sv");
        dirs.add("en-tr");
        dirs.add("en-uk");
        dirs.add("es-be");
        dirs.add("es-de");
        dirs.add("es-en");
        dirs.add("es-ru");
        dirs.add("es-uk");
        dirs.add("et-en");
        dirs.add("et-ru");
        dirs.add("fi-en");
        dirs.add("fi-ru");
        dirs.add("fr-be");
        dirs.add("fr-de");
        dirs.add("fr-en");
        dirs.add("fr-ru");
        dirs.add("fr-uk");
        dirs.add("hr-ru");
        dirs.add("hu-en");
        dirs.add("hu-ru");
        dirs.add("hy-ru");
        dirs.add("it-be");
        dirs.add("it-de");
        dirs.add("it-en");
        dirs.add("it-ru");
        dirs.add("it-uk");
        dirs.add("lt-en");
        dirs.add("lt-ru");
        dirs.add("lv-en");
        dirs.add("lv-ru");
        dirs.add("mk-en");
        dirs.add("mk-ru");
        dirs.add("nl-en");
        dirs.add("nl-ru");
        dirs.add("no-en");
        dirs.add("no-ru");
        dirs.add("pl-be");
        dirs.add("pl-ru");
        dirs.add("pl-uk");
        dirs.add("pt-en");
        dirs.add("pt-ru");
        dirs.add("ro-be");
        dirs.add("ro-ru");
        dirs.add("ro-uk");
        dirs.add("ru-az");
        dirs.add("ru-be");
        dirs.add("ru-bg");
        dirs.add("ru-ca");
        dirs.add("ru-cs");
        dirs.add("ru-da");
        dirs.add("ru-de");
        dirs.add("ru-el");
        dirs.add("ru-en");
        dirs.add("ru-es");
        dirs.add("ru-et");
        dirs.add("ru-fi");
        dirs.add("ru-fr");
        dirs.add("ru-hr");
        dirs.add("ru-hu");
        dirs.add("ru-hy");
        dirs.add("ru-it");
        dirs.add("ru-lt");
        dirs.add("ru-lv");
        dirs.add("ru-mk");
        dirs.add("ru-nl");
        dirs.add("ru-no");
        dirs.add("ru-pl");
        dirs.add("ru-pt");
        dirs.add("ru-ro");
        dirs.add("ru-sk");
        dirs.add("ru-sl");
        dirs.add("ru-sq");
        dirs.add("ru-sr");
        dirs.add("ru-sv");
        dirs.add("ru-tr");
        dirs.add("ru-uk");
        dirs.add("sk-en");
        dirs.add("sk-ru");
        dirs.add("sl-en");
        dirs.add("sl-ru");
        dirs.add("sq-en");
        dirs.add("sq-ru");
        dirs.add("sr-be");
        dirs.add("sr-ru");
        dirs.add("sr-uk");
        dirs.add("sv-en");
        dirs.add("sv-ru");
        dirs.add("tr-be");
        dirs.add("tr-de");
        dirs.add("tr-en");
        dirs.add("tr-ru");
        dirs.add("tr-uk");
        dirs.add("uk-bg");
        dirs.add("uk-cs");
        dirs.add("uk-de");
        dirs.add("uk-en");
        dirs.add("uk-es");
        dirs.add("uk-fr");
        dirs.add("uk-it");
        dirs.add("uk-pl");
        dirs.add("uk-ro");
        dirs.add("uk-ru");
        dirs.add("uk-sr");
        dirs.add("uk-tr");

        SupportLanguages item = new SupportLanguages();
        item.setLangs(langs);
        item.setDirs(dirs);

        return item;
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(maxStaleInDays, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);
        };
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(maxAgeInMinutes, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .header(YandexTranslateClient.cacheControlHeader, cacheControl.toString())
                    .build();
        };
    }
}
