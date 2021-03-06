package pl.grudowska.feedme.dialogFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.data.AdditionalsDataLoader;
import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class LimitDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //http://stackoverflow.com/questions/13522321/how-to-put-my-custom-number-picker-in-a-dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // get the layout inflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate limit_dialog layout for the dialog to a View
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.limit_dialog, null);

        builder.setView(view);

        TextView limitCurrent = (TextView) view.findViewById(R.id.limit_current);
        int limitInt = SharedPreferencesManager.loadDataInt(getActivity(), "limit", 2300);

        String limitText = " " + limitInt + " kCal";
        limitCurrent.append(limitText);

        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.picker_number_view);
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
                        AdditionalsDataLoader.inflateProducsRanges(getActivity());
                        Toast.makeText(getActivity(), "Calories limit updated", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getDialog().cancel();
            }
        });

        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
            }
        });
        return mDialog;
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if ("mSelectionDivider".equals(pf.getName())) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, getResources().getDrawable(R.color.colorBrokenWhite));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
