package com.example.Weather;

import android.content.Context;
import android.content.res.Configuration;
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
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class ThreeDaysForecastFragment extends ForecastFragment {

    private static final String WOEID_CODE_KEY = "id_key";
    private static final int NUMBER_DAILY_FORECASTS = 3;

    private String woeidString;
    private View[] dailyForecastViews = new View[NUMBER_DAILY_FORECASTS];

    private TextView locationTextView;

    public static ThreeDaysForecastFragment newInstance(String woeldString) {
        ThreeDaysForecastFragment newThreeDaysForecastFragment = new ThreeDaysForecastFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putString(WOEID_CODE_KEY, woeldString);

        newThreeDaysForecastFragment.setArguments(argumentsBundle);
        return newThreeDaysForecastFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.woeidString = getArguments().getString(WOEID_CODE_KEY);
    }

    public String getWOEID() {
        return woeidString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.three_day_forecast_layout, null);

        locationTextView = (TextView) rootView.findViewById(R.id.location);
        LinearLayout containerLinearLayout = (LinearLayout) rootView.findViewById(R.id.containerLinearLayout);

        int id;
        if (container.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            id = R.layout.single_forecast_layout_landscape;
        } else {
            id = R.layout.single_forecast_layout_portrait;
        }
        View forecastView;

        for (int i = 0; i < NUMBER_DAILY_FORECASTS; i++) {
            forecastView = inflater.inflate(id, null);
            containerLinearLayout.addView(forecastView);
            dailyForecastViews[i] = forecastView;
        }

        new ReadLocationTask(woeidString, rootView.getContext(), new WeatherLocationLoadedListener(woeidString, rootView.getContext())).execute();

        Toast loadingToast = Toast.makeText(getActivity(), getResources().getString(R.string.loading_message), Toast.LENGTH_SHORT);
        loadingToast.setGravity(Gravity.CENTER, 0, 0);
        loadingToast.show();

        return rootView;
    }

    private class WeatherLocationLoadedListener implements ReadLocationTask.LocationLoadedListener {
        private String woeidString;
        private Context context;

        public WeatherLocationLoadedListener(String woeidString, Context context) {
            this.woeidString = woeidString;
            this.context = context;
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
            new ReadThreeDaysForecastTask(woeidString, weatherForecastListener, locationTextView.getContext()).execute();
        }

        ReadThreeDaysForecastTask.ThreeDaysForecastLoadedListener weatherForecastListener = new ReadThreeDaysForecastTask.ThreeDaysForecastLoadedListener() {
            @Override
            public void onForecastLoaded(DailyForecast[] forecasts) {
                for (int i = 0; i < NUMBER_DAILY_FORECASTS; i++) {
                    loadForecastIntoView(dailyForecastViews[i], forecasts[i]);
                }
            }
        };

        private void loadForecastIntoView(View view, DailyForecast dailyForecast) {
            if (!ThreeDaysForecastFragment.this.isAdded()) {
                return;
            } else if (dailyForecast == null || dailyForecast.getIconBitmap() == null) {
                Toast errorToast = Toast.makeText(context, context.getResources().getString(R.string.null_data_toast), Toast.LENGTH_LONG);
                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();
                return;
            }

            ImageView forecastImageView = (ImageView) view.findViewById(R.id.daily_forecast_bitmap);
            TextView dayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week);
            TextView descriptionTextView = (TextView) view.findViewById(R.id.daily_forecast_description);
            TextView highTemperatureTextView = (TextView) view.findViewById(R.id.high_temperature);
            TextView lowTemperatureTextView = (TextView) view.findViewById(R.id.low_temperature);

            forecastImageView.setImageBitmap(dailyForecast.getIconBitmap());
            dayOfWeekTextView.setText(dailyForecast.getDay());
            descriptionTextView.setText(dailyForecast.getDescription());
            highTemperatureTextView.setText(dailyForecast.getHighTemperature());
            lowTemperatureTextView.setText(dailyForecast.getLowTemperature());
        }
    }
}
