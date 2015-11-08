package parkingfacile.hexan.com.Controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import parkingfacile.hexan.com.Controller.Activity.MainActivity;
import parkingfacile.hexan.com.R;

/**
 * Created by james_000 on 9/24/2015.
 */
public class SelectTownDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.select_town_dialog, null);
        // Use the Builder class for convenient dialog construction
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS, 0);

        Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinner);
        spinner.setSelection(settings.getInt(MainActivity.VILLE_ID, 0));

        CheckBox checkBox = (CheckBox)dialogView.findViewById(R.id.checkBox);
        checkBox.setChecked(settings.getBoolean(MainActivity.GOOGLE_PARKING, false));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_town)
                .setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckBox checkBox = (CheckBox)dialogView.findViewById(R.id.checkBox);
                        Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinner);
                        ((MainActivity) getActivity()).onUserSelectValue(spinner.getSelectedItemPosition(), checkBox.isChecked());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}