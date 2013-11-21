package com.example.Weather;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 21.11.13
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public class IconSeeker {

    public static Bitmap setIcon(int id, Resources resources)  {
        Bitmap temp = null;
        switch (id) {
            case 0:
                temp = BitmapFactory.decodeResource(resources, R.drawable.na);
                break;
            case 1:
                temp = BitmapFactory.decodeResource(resources, R.drawable.na);
                break;
            case 2:
                temp = BitmapFactory.decodeResource(resources, R.drawable.na);
                break;
            case 3:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders_day);
                break;
            case 4:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders_day);
                break;
            case 5:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow_and_rain);
                break;
            case 6:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow_and_rain);
                break;
            case 7:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow_and_rain);
                break;
            case 8:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers_day);
                break;
            case 9:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers_day);
                break;
            case 10:
                temp = BitmapFactory.decodeResource(resources, R.drawable.rain);
                break;
            case 11:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers);
                break;
            case 12:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers);
                break;
            case 13:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snowing);
                break;
            case 14:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow);
                break;
            case 15:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow);
                break;
            case 16:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow);
                break;
            case 17:
                temp = BitmapFactory.decodeResource(resources, R.drawable.hail);
                break;
            case 18:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers);
                break;
            case 19:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mist);      //im professional painter
                break;
            case 20:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mist);
                break;
            case 21:
                temp = BitmapFactory.decodeResource(resources, R.drawable.haze);
                break;
            case 22:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mist);
                break;
            case 23:
                temp = BitmapFactory.decodeResource(resources, R.drawable.wind);
                break;
            case 24:
                temp = BitmapFactory.decodeResource(resources, R.drawable.wind);
                break;
            case 25:
                temp = BitmapFactory.decodeResource(resources, R.drawable.ice_freeze);
                break;
            case 26:
                temp = BitmapFactory.decodeResource(resources, R.drawable.clouds);
                break;
            case 27:
                temp = BitmapFactory.decodeResource(resources, R.drawable.cloudy_night);
                break;
            case 28:
                temp = BitmapFactory.decodeResource(resources, R.drawable.cloudy_day);
                break;
            case 29:
                temp = BitmapFactory.decodeResource(resources, R.drawable.cloudy_night);
                break;
            case 30:
                temp = BitmapFactory.decodeResource(resources, R.drawable.cloudy_day);
                break;
            case 31:
                temp = BitmapFactory.decodeResource(resources, R.drawable.clear);
                break;
            case 32:
                temp = BitmapFactory.decodeResource(resources, R.drawable.sunny);
                break;
            case 33:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mostly_clear_night);
                break;
            case 34:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mostly_clear);
                break;
            case 35:
                temp = BitmapFactory.decodeResource(resources, R.drawable.rain);
                break;
            case 36:
                temp = BitmapFactory.decodeResource(resources, R.drawable.sunny);
                break;
            case 37:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders_day);
                break;
            case 38:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders);
                break;
            case 39:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders);
                break;
            case 40:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers);
                break;
            case 41:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snowing);
                break;
            case 42:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snowing);
                break;
            case 43:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snowing);
                break;
            case 44:
                temp = BitmapFactory.decodeResource(resources, R.drawable.mostly_clear);
                break;
            case 45:
                temp = BitmapFactory.decodeResource(resources, R.drawable.showers);
                break;
            case 46:
                temp = BitmapFactory.decodeResource(resources, R.drawable.snow);
                break;
            case 47:
                temp = BitmapFactory.decodeResource(resources, R.drawable.thunders);
                break;
        }
        return temp;
    }

}
