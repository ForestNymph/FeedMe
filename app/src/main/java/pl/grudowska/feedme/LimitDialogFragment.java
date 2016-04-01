package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import pl.grudowska.feedme.util.SharedPreferencesManager;

public class LimitDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //http://stackoverflow.com/questions/13522321/how-to-put-my-custom-number-picker-in-a-dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        // get the layout inflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate limit_dialog layout for the dialog to a View
        View view = inflater.inflate(R.layout.limit_dialog, null);

        builder.setView(view);

        TextView limitCurrent = (TextView) view.findViewById(R.id.limit_current);
        int limitInt = SharedPreferencesManager.loadDataInt(getActivity(), "limit", 2400);

        String limitText = "Current: " + limitInt + " kCal";
        limitCurrent.setText(limitText);

        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.pickerView);
        picker.setMaxValue(3000);
        picker.setMinValue(1500);
        picker.setValue(limitInt);
        picker.setWrapSelectorWheel(true);

        // change divider color by reflection
        setDividerColor(picker);

        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        SharedPreferencesManager.saveDataInt(getActivity(), "limit", picker.getValue());
                        Log.e("New value", "" + picker.getValue());
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
                LimitDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    private void setDividerColor(NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, getResources().getDrawable(R.color.colorBrokeWhite));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
