package com.example.Weather;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 21.11.13
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
public class DailyForecast {
    public static final int DAY_INDEX = 0;
    public static final int PREDICTION_INDEX = 1;
    public static final int HIGH_TEMP_INDEX = 2;
    public static final int LOW_TEMP_INDEX = 3;
    public static final int ICON_CODE_INDEX = 4;

    final private String[] forecast;
    final private Bitmap iconBitmap;

    public DailyForecast(String[] forecast, Bitmap iconBitmap) {
        this.forecast = forecast;
        this.iconBitmap = iconBitmap;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public String getDay() {
        return forecast[DAY_INDEX];
    }

    public String getDescription() {
        return forecast[PREDICTION_INDEX];
    }

    public String getHighTemperature() {
        return forecast[HIGH_TEMP_INDEX];
    }

    public String getLowTemperature() {
        return forecast[LOW_TEMP_INDEX];
    }
}
