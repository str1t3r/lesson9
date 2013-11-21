package com.example.Weather;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 20.11.13
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
public class CitiesFragment extends ListFragment {
    private int selectedCity;

    private static final String CURRENT_CITY_KEY = "current_city";

    public ArrayList<String> citiesArrayList;
    private CitiesListChangeListener citiesListChangeListener;
    private ArrayAdapter<String> citiesArrayAdapter;

    public interface CitiesListChangeListener {
        public void onSelectedCityChanged(String cityName);
        public void onPreferredCityChanged(String cityName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceStateBundle) {
        super.onActivityCreated(savedInstanceStateBundle);

        if (savedInstanceStateBundle != null) {
            selectedCity = savedInstanceStateBundle.getInt(CURRENT_CITY_KEY);
        }

        citiesArrayList = new ArrayList<String>();

        setListAdapter(new CitiesArrayAdapter<String>(getActivity(), R.layout.city_list_item, citiesArrayList));

        ListView listView = getListView();
        citiesArrayAdapter = (ArrayAdapter<String>) getListAdapter();

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemLongClickListener(citiesOnItemLongClickListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_CITY_KEY, selectedCity);
    }

    public void addCity(String cityName, boolean select) {
        citiesArrayAdapter.add(cityName);
        citiesArrayAdapter.sort(String.CASE_INSENSITIVE_ORDER);

        if (select) {
            citiesListChangeListener.onSelectedCityChanged(cityName);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        citiesListChangeListener.onSelectedCityChanged(((TextView) v ).getText().toString());
        selectedCity = position;
    }


    public void setCitiesListChangeListener(CitiesListChangeListener listener) {
        citiesListChangeListener = listener;
    }

    private class CitiesArrayAdapter<T> extends ArrayAdapter<String> {
        private Context context;

        public CitiesArrayAdapter(Context context, int textViewResourseId, List<String>objects) {
            super(context, textViewResourseId, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView)super.getView(position, convertView, parent);
            if (isPreferredCity(textView.getText().toString())) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.btn_star_big_on, 0);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            return textView;
        }

        private boolean isPreferredCity(String cityName) {
            SharedPreferences sp = context.getSharedPreferences(WeatherActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            return cityName.equals(sp.getString(WeatherActivity.PREFERRED_CITY_NAME_KEY, null));
        }
    }

    private AdapterView.OnItemLongClickListener citiesOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            final Context context = view.getContext();
            final Resources resources = context.getResources();
            final String cityName = ((TextView) view).getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(resources.getString(R.string.dialog_prefix) + cityName + resources.getString(R.string.dialog_postfix));
            builder.setPositiveButton(resources.getString(R.string.dialog_preferred), new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    citiesListChangeListener.onPreferredCityChanged(cityName);
                    citiesArrayAdapter.notifyDataSetChanged();
                }
            });

            builder.setNeutralButton(resources.getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (citiesArrayAdapter.getCount() == 1) {
                        Toast errorToast = Toast.makeText(context, resources.getString(R.string.last_city_warning), Toast.LENGTH_LONG);
                        errorToast.setGravity(Gravity.CENTER, 0, 0);
                        errorToast.show();
                        return;
                    }
                    citiesArrayAdapter.remove(cityName);
                    SharedPreferences sp = context.getSharedPreferences(WeatherActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor preferenceEditor = sp.edit();
                    preferenceEditor.remove(cityName);
                    preferenceEditor.apply();

                    String preferredCity = sp.getString(WeatherActivity.PREFERRED_CITY_NAME_KEY, resources.getString(R.string.default_name));
                    if (cityName.equals(preferredCity)) {
                        citiesListChangeListener.onPreferredCityChanged(citiesArrayList.get(0));
                    } else if (cityName.equals(citiesArrayList.get(selectedCity))) {
                        citiesListChangeListener.onSelectedCityChanged(preferredCity);
                    }
                }
            });

            builder.setNegativeButton(resources.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.create().show();
            return true;
        }
    };



}
