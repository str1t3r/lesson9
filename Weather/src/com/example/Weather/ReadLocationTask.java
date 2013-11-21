package com.example.Weather;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
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
 * Time: 19:25
 * To change this template use File | Settings | File Templates.
 */
public class ReadLocationTask extends AsyncTask<Object, Object, String> {
    private static final String TAG = "ReadLocationTask.java";

    private String woeidString;
    private Context context;
    private Resources resources;

    private String cityName;
    private String countryName;

    private LocationLoadedListener weatherLocationLoadedListener;

    public interface LocationLoadedListener {
        public void onLocationLoaded(String cityName, String countryName);
    }

    public ReadLocationTask(String woeidString, Context context, LocationLoadedListener listener) {
        this.woeidString = woeidString;
        this.context = context;
        weatherLocationLoadedListener = listener;
        this.resources = context.getResources();
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
            NodeList list = doc.getElementsByTagName("yweather:location");
            Node node = list.item(0);
            Element e = (Element) node;
            cityName = e.getAttribute("city");
            countryName = e.getAttribute("country");
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (cityName != null) {
            weatherLocationLoadedListener.onLocationLoaded(cityName, countryName);
        } else {
            Toast errorToast = Toast.makeText(context, resources.getString(R.string.woeid_error), Toast.LENGTH_LONG);
            errorToast.setGravity(Gravity.CENTER, 0, 0);
            errorToast.show();
        }
    }
}
