package com.example.Weather;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 20.11.13
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class SingleForecastFragment extends ForecastFragment {
    private String woeidString;

    private static final String LOCATION_KEY = "location";
    private static final String TEMPERATURE_KEY = "temperature";
    private static final String HUMIDITY_KEY = "humidity";
    private static final String PRECIPITATION_KEY = "precipitation";
    private static final String PRESSURE_KEY = "pressure";
    private static final String WIND_KEY = "wind";
    private static final String IMAGE_KEY = "image";

    private static final String WOEID_CODE_KEY = "id_key";

    private View forecastView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView locationTextView;
    private TextView precipitationTextView;
    private ImageView conditionImageView;
    private TextView pressureTextView;
    private TextView windTextView;
    private Context context;
    private Bitmap conditionsBitmap;

    public static SingleForecastFragment newInstance(String woeidString) {
        SingleForecastFragment fragment = new SingleForecastFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putString(WOEID_CODE_KEY, woeidString);

        fragment.setArguments(argumentsBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.woeidString = getArguments().getString(WOEID_CODE_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(LOCATION_KEY, locationTextView.getText().toString());
        outState.putString(TEMPERATURE_KEY, temperatureTextView.getText().toString());
        outState.putString(HUMIDITY_KEY, humidityTextView.getText().toString());
        outState.putString(PRECIPITATION_KEY, precipitationTextView.getText().toString());
        outState.putString(PRESSURE_KEY, pressureTextView.getText().toString());
        outState.putString(WIND_KEY, windTextView.getText().toString());
        outState.putParcelable(IMAGE_KEY, conditionsBitmap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_fragment, null);
        int id;
        LinearLayout containterLinearLayout = (LinearLayout) rootView.findViewById(R.id.forecast_layout);
        if (container.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
             id = R.layout.forecast_fragment_layout_landscape;
         } else {
             id = R.layout.forecast_fragment_layout;
        }
        forecastView = inflater.inflate(id, null);
        containterLinearLayout.addView(forecastView);
        locationTextView = (TextView) rootView.findViewById(R.id.location);
        temperatureTextView = (TextView) rootView.findViewById(R.id.temperature);
        humidityTextView = (TextView) rootView.findViewById(R.id.humidity);
        precipitationTextView = (TextView) rootView.findViewById(R.id.precipitation);
        pressureTextView = (TextView) rootView.findViewById(R.id.pressure);
        windTextView = (TextView) rootView.findViewById(R.id.wind);
        conditionImageView = (ImageView) rootView.findViewById(R.id.forecast_image);

        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            Toast loadingToast = Toast.makeText(context, context.getResources().getString(R.string.loading_message), Toast.LENGTH_SHORT);
            loadingToast.setGravity(Gravity.CENTER, 0, 0);
            loadingToast.show();


            new ReadLocationTask(woeidString, context, new WeatherLocationLoadedListener(woeidString)).execute();
        } else {
            conditionImageView.setImageBitmap((Bitmap) savedInstanceState.getParcelable(IMAGE_KEY));
            locationTextView.setText(savedInstanceState.getString(LOCATION_KEY));
            temperatureTextView.setText(savedInstanceState.getString(TEMPERATURE_KEY));
            humidityTextView.setText(savedInstanceState.getString(HUMIDITY_KEY));
            precipitationTextView.setText(savedInstanceState.getString(PRECIPITATION_KEY));
            pressureTextView.setText(savedInstanceState.getString(PRESSURE_KEY));
            windTextView.setText(savedInstanceState.getString(WIND_KEY));
        }
    }

    ReadForecastTask.ForecastListener weatherForecastListener = new ReadForecastTask.ForecastListener() {
        @Override
        public void onForecastLoaded(Bitmap imageBitmap, String temperatureString, String pressureString, String humidityString, String precipitationString, String windString) {
            if (!SingleForecastFragment.this.isAdded()) {
                return;
            } else if (imageBitmap == null) {
                Toast errorToast = Toast.makeText(context, context.getResources().getString(R.string.null_data_toast), Toast.LENGTH_LONG);
                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();
                return;
            }
            Resources resources = SingleForecastFragment.this.getResources();

            conditionImageView.setImageBitmap(imageBitmap);
            conditionsBitmap = imageBitmap;
            temperatureTextView.setText(temperatureString + (char)0x00b0 + resources.getString(R.string.temperature_unit));
            humidityTextView.setText(humidityString + (char)0x0025);
            precipitationTextView.setText(precipitationString);
            pressureTextView.setText(pressureString);
            windTextView.setText(windString);
        }
    };

    private class WeatherLocationLoadedListener implements ReadLocationTask.LocationLoadedListener {
        private String woeidString;

        public WeatherLocationLoadedListener(String woeidString) {
            this.woeidString = woeidString;
        }

        @Override
        public void onLocationLoaded(String cityName, String countryName) {
            if (cityName == null) {
                Toast errorToast = Toast.makeText(context, context.getResources().getString(R.string.null_data_toast), Toast.LENGTH_LONG);
                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();
                return;
            }

            locationTextView.setText(cityName + ", " + countryName);
            new ReadForecastTask(woeidString, weatherForecastListener, locationTextView.getContext()).execute();
        }
    }
    public String getWOEID() {
        return woeidString;
    }
}
