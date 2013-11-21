package com.example.Weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 20.11.13
 * Time: 23:47
 * To change this template use File | Settings | File Templates.
 */
public class ReadThreeDaysForecastTask extends AsyncTask<Object, Object, String> {

    private static final String TAG = "ReadThreeDayForecastTasj.java";

    private String woeidString;
    private ThreeDaysForecastLoadedListener weatherThreeDayForecastListener;
    private Resources resources;
    private DailyForecast[] forecasts;
    private static final int NUMBER_OF_DAYS = 3;

    public interface ThreeDaysForecastLoadedListener {
        public void onForecastLoaded(DailyForecast[] forecasts);
    }

    public ReadThreeDaysForecastTask(String woeid, ThreeDaysForecastLoadedListener listener, Context context) {
        this.woeidString = woeid;
        this.resources = context.getResources();
        this.weatherThreeDayForecastListener = listener;
        this.forecasts = new DailyForecast[NUMBER_OF_DAYS];
    }

    @Override
    protected String doInBackground(Object... objects) {
        try {
            URL url = new URL(resources.getString(R.string.url_prefix) + URLEncoder.encode(woeidString, "UTF-8") + resources.getString(R.string.url_postfix));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            BufferedInputStream is = new BufferedInputStream(url.openStream());
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("yweather:forecast");
            for (int i = 0; i < NUMBER_OF_DAYS; i++) {
                Node node = nodeList.item(i);
                Element e = (Element) node;
                forecasts[i] = readDailyForecast(e);
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
        return null;
    }

    private DailyForecast readDailyForecast(Element e) {
        String[] dailyForecast = new String[5];
        dailyForecast[DailyForecast.ICON_CODE_INDEX] = e.getAttribute("code");
        Bitmap iconBitmap = IconSeeker.setIcon(Integer.parseInt(dailyForecast[DailyForecast.ICON_CODE_INDEX]), resources);
        dailyForecast[DailyForecast.DAY_INDEX] = e.getAttribute("day");
        dailyForecast[DailyForecast.PREDICTION_INDEX] = e.getAttribute("text");
        dailyForecast[DailyForecast.HIGH_TEMP_INDEX] = e.getAttribute("high");
        dailyForecast[DailyForecast.LOW_TEMP_INDEX] = e.getAttribute("low");
        return new DailyForecast(dailyForecast, iconBitmap);
    }

    @Override
    protected void onPostExecute(String s) {
        weatherThreeDayForecastListener.onForecastLoaded(forecasts);
    }
}
