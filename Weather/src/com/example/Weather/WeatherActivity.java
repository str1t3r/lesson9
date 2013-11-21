package com.example.Weather;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class WeatherActivity extends Activity implements AddCityDialogFragment.DialogFinishedListener{
    private static final int CURRENT_CONDITIONS_TAB = 0;

    public static final String PREFERRED_CITY_NAME_KEY = "preferred_city_name";
    private static final String PREFERRED_CITY_WOEID_KEY = "preferred_city_woeid";

    private static final String CURRENT_TAB_KEY = "current_tab";

    private static final String LAST_SELECTED_KEY = "last_selected";

    public static final String SHARED_PREFERENCES_NAME = "weather_viewer_shared_preferences";

    private SharedPreferences weatherSharedPreferences;

    private CitiesFragment listCitiesFragment;

    private int currentTab;
    private String lastSelectedCity;

    private Map<String, String> citiesMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listCitiesFragment = (CitiesFragment) getFragmentManager().findFragmentById(R.id.fragment);
        listCitiesFragment.setCitiesListChangeListener(citiesListChangeListener);

        citiesMap = new HashMap<String, String>();

        weatherSharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        setupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_city_item) {
            showAddCityDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceStateBundle) {
         savedInstanceStateBundle.putInt(CURRENT_TAB_KEY, currentTab);
         savedInstanceStateBundle.putString(LAST_SELECTED_KEY, lastSelectedCity);
         super.onSaveInstanceState(savedInstanceStateBundle);
     }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceStateBundle) {
        super.onRestoreInstanceState(savedInstanceStateBundle);
        currentTab = savedInstanceStateBundle.getInt(CURRENT_TAB_KEY);
        lastSelectedCity = savedInstanceStateBundle.getString(LAST_SELECTED_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (citiesMap.isEmpty()) {
            loadSavedCities();
        }

        if (citiesMap.isEmpty()) {
            addSampleCities();
        }

        getActionBar().selectTab(getActionBar().getTabAt(currentTab));
        loadSelectedForecast();
    }

    private CitiesFragment.CitiesListChangeListener citiesListChangeListener = new CitiesFragment.CitiesListChangeListener() {

        @Override
        public void onSelectedCityChanged(String cityName) {
            selectForecast(cityName);
        }

        @Override
        public void onPreferredCityChanged(String cityName) {
            setPreferred(cityName);
        }
    };

    private void loadSelectedForecast() {
        if (lastSelectedCity != null) {
            selectForecast(lastSelectedCity);
        } else {
            String cityName = weatherSharedPreferences.getString(PREFERRED_CITY_NAME_KEY, getResources().getString(R.string.default_name));
            selectForecast(cityName);
        }
    }

    public void setPreferred(String cityName) {
        String cityWOEIDString = citiesMap.get(cityName);
        SharedPreferences.Editor preferredCityEditor = weatherSharedPreferences.edit();
        preferredCityEditor.putString(PREFERRED_CITY_NAME_KEY, cityName);
        preferredCityEditor.putString(PREFERRED_CITY_WOEID_KEY, cityWOEIDString);
        preferredCityEditor.apply();
        lastSelectedCity = null;
        loadSelectedForecast();
    }

    private void loadSavedCities() {
        Map<String, ? > cities = weatherSharedPreferences.getAll();
        for (String cityName : cities.keySet()) {
            if (!(PREFERRED_CITY_NAME_KEY.equals(cityName) || PREFERRED_CITY_WOEID_KEY.equals(cityName))) {
                addCity(cityName, (String) cities.get(cityName), false);
            }
        }
    }

    private void addSampleCities() {
        String[] sampleCitiesNameArray = getResources().getStringArray(R.array.default_cities_names);
        String[] sampleCitiesWOEIDArray = getResources().getStringArray(R.array.default_city_woeid);
        setPreferred(sampleCitiesNameArray[0]);
        for (int i = 0; i < sampleCitiesNameArray.length; i++) {
            addCity(sampleCitiesNameArray[i], sampleCitiesWOEIDArray[i], false);
        }
    }

    public void addCity(String city, String woeid, boolean select) {
        citiesMap.put(city, woeid);
        listCitiesFragment.addCity(city, select);
        SharedPreferences.Editor preferenceEditor = weatherSharedPreferences.edit();
        preferenceEditor.putString(city, woeid);
        preferenceEditor.apply();
    }

    public void selectForecast(String name) {
        lastSelectedCity = name;
        String woeid = citiesMap.get(name);
        if (woeid == null) return;

        ForecastFragment currentForecastFragment = (ForecastFragment)
                getFragmentManager().findFragmentById(R.id.forecast_replacer);

        if (currentForecastFragment == null ||
                !(currentForecastFragment.getWOEID().equals(woeid) && correctTab(currentForecastFragment))) {
            if (currentTab == CURRENT_CONDITIONS_TAB) {
                currentForecastFragment = SingleForecastFragment.newInstance(woeid);
            } else {
                currentForecastFragment = ThreeDaysForecastFragment.newInstance(woeid);
            }

            FragmentTransaction forecastFragmentTransaction = getFragmentManager().beginTransaction();
            forecastFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            forecastFragmentTransaction.replace(R.id.forecast_replacer, currentForecastFragment);

            forecastFragmentTransaction.commit();
        }
    }

    private boolean correctTab(ForecastFragment forecastFragment) {
        if (currentTab == CURRENT_CONDITIONS_TAB) {
            return (forecastFragment instanceof SingleForecastFragment);
        } else {
            return (forecastFragment instanceof ThreeDaysForecastFragment);
        }
    }

    private void selectTab(int pos) {
        currentTab = pos;
        loadSelectedForecast();
    }

    private void showAddCityDialog() {
        AddCityDialogFragment newAddCityDialogFragment = new AddCityDialogFragment();

        FragmentManager thisFragmentManager = getFragmentManager();
        FragmentTransaction addCityFragmentTransition = thisFragmentManager.beginTransaction();

        newAddCityDialogFragment.show(addCityFragmentTransition, "");
    }

    @Override
    public void onDialogFinished(String woeidString, boolean preferred) {
        getCityNameFromWOEID(woeidString, preferred);
    }

    private void getCityNameFromWOEID(String woeidString, boolean preferred) {
        if (citiesMap.containsValue(woeidString)) {
            Toast errorToast = Toast.makeText(WeatherActivity.this, WeatherActivity.this.getResources().getString(R.string.duplicate_error), Toast.LENGTH_LONG);
            errorToast.setGravity(Gravity.CENTER, 0, 0);
            errorToast.show();
        } else {
            new ReadLocationTask(woeidString, this, new CityNameLocationLoadedListener(woeidString, preferred)).execute();
        }
    }

    private class CityNameLocationLoadedListener implements ReadLocationTask.LocationLoadedListener {
        private String woeidString;
        private boolean preferred;

        public CityNameLocationLoadedListener(String woeid, boolean pref) {
            this.woeidString = woeid;
            this.preferred = pref;
        }

        @Override
        public void onLocationLoaded(String cityName, String countryName) {
            if (cityName != null) {
                addCity(cityName, woeidString, !preferred);
                if (preferred) {
                    setPreferred(cityName);
                }
            } else {
                Toast errorToast = Toast.makeText(WeatherActivity.this, WeatherActivity.this.getResources().getString(R.string.woeid_error), Toast.LENGTH_LONG);
                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();
            }
        }
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab currentConditionsTab = actionBar.newTab();
        currentConditionsTab.setText(getResources().getString(R.string.current_conditions));
        currentConditionsTab.setTabListener(weatherTabListener);
        actionBar.addTab(currentConditionsTab);

        ActionBar.Tab threeDaysForecastTab = actionBar.newTab();
        threeDaysForecastTab.setText(getResources().getString(R.string.three_days_forecast));
        threeDaysForecastTab.setTabListener(weatherTabListener);
        actionBar.addTab(threeDaysForecastTab);

        currentTab = CURRENT_CONDITIONS_TAB;
    }

    ActionBar.TabListener weatherTabListener = new ActionBar.TabListener() {

        @Override
        public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {}

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction arg1) {
            selectTab(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    };
}
