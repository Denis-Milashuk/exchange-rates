package urlConnectionClasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Denis.Currency;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UrlUpdater {
    private static UrlUpdater urlUpdater;
    private final static String MAIN_URL = "https://www.nbrb.by/api/exrates/rates/";
    private final static String DEFAULT_PARAMETERS = "parammode=2&periodicity=0";

    private UrlUpdater(){
    }

    public static UrlUpdater getInstance(){
        if(urlUpdater == null){
            urlUpdater = new UrlUpdater();
        }
        return urlUpdater;
    }

    public Rate getDataByDateAndCurrency(Calendar courseDate, Currency currency) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'ondate='yyyy-MM-dd");
        ObjectMapper mapper = new ObjectMapper();
        Rate rate = null;
        for (int updateRetryIndex = 0; updateRetryIndex < 10; updateRetryIndex++) {
            URL url = new URL(MAIN_URL + currency + "?" + dateFormat.format(courseDate.getTime()) + "&" + DEFAULT_PARAMETERS);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(200);
            connection.setReadTimeout(200);
            rate = mapper.readValue(url, Rate.class);
            updateRetryIndex = 10;
        }
        if(rate == null){
            System.out.println("URL update error");
            throw new NullPointerException();
        }
        return rate;
    }
}
