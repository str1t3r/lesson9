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
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
public class ReadForecastTask extends AsyncTask<Object, Object, String> {
    private String woeidString;
    private Resources resources;

    private ForecastListener weatherForecastListener;
    private static final String TAG = "ReadForecastTask.java";

    private String temperatureString;
    private String pressureString;
    private String humidityString;
    private String precipitationString;
    private String windString;
    private Bitmap iconBitmap;

  //  private int bitmapSampleSize = -1;
    private int condition;
    public interface ForecastListener {
        public void onForecastLoaded(Bitmap imageBitmap, String temperatureString, String pressureString, String humidityString, String precipitationString, String windString);
    }

    public ReadForecastTask(String woeidString, ForecastListener listener, Context context) {
        this.woeidString = woeidString;
        this.weatherForecastListener = listener;
        this.resources = context.getResources();
    }

  /*  public void setSampleSize(int sampleSize) {
        this.bitmapSampleSize = sampleSize;
    }    */

    @Override
    protected String doInBackground(Object... objects) {
        try {
            URL url = new URL(resources.getString(R.string.url_prefix) + URLEncoder.encode(woeidString, "UTF-8") + resources.getString(R.string.url_postfix));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            BufferedInputStream is = new BufferedInputStream(url.openStream());
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("yweather:atmosphere");
            Node node = nodeList.item(0);
            Element e = (Element) node;
            humidityString = e.getAttribute("humidity");
            pressureString = e.getAttribute("pressure") + " " + "mb";
            nodeList = doc.getElementsByTagName("yweather:wind");
            node = nodeList.item(0);
            e = (Element) node;
            windString = e.getAttribute("speed") + " " + "km / h";
            nodeList = doc.getElementsByTagName("yweather:condition");
            node = nodeList.item(0);
            e = (Element) node;
            temperatureString = e.getAttribute("temp");
            precipitationString = e.getAttribute("text");
            condition = Integer.parseInt(e.getAttribute("code"));
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        iconBitmap = IconSeeker.setIcon(condition, resources);
        weatherForecastListener.onForecastLoaded(iconBitmap, temperatureString, pressureString, humidityString, precipitationString, windString);
    }

}
