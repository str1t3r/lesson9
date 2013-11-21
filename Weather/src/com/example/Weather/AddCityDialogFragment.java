package com.example.Weather;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 20.11.13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class AddCityDialogFragment extends DialogFragment implements View.OnClickListener{

    EditText editText;
    CheckBox checkBox;

    public interface DialogFinishedListener {
        void onDialogFinished(String woeidString, boolean preferred);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_city_dialog, container, false);
        editText = (EditText) rootView.findViewById(R.id.add_city_edit_text);
        checkBox = (CheckBox) rootView.findViewById(R.id.add_city_checkbox);

        if (savedInstanceState != null) {
            editText.setText(savedInstanceState.getString(getResources().getString(R.string.dialog_bundle_key)));
        }

        getDialog().setTitle(R.string.dialog_title);

        Button button = (Button) rootView.findViewById(R.id.add_city_button);
        button.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(getResources().getString(R.string.dialog_bundle_key), editText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_city_button) {
            DialogFinishedListener listener = (DialogFinishedListener) getActivity();
            listener.onDialogFinished(editText.getText().toString(), checkBox.isChecked());
            dismiss();
        }
    }
}
