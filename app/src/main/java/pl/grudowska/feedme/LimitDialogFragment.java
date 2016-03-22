package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

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

        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.pickerView);
        picker.setMaxValue(3000);
        picker.setMinValue(1500);
        picker.setValue(2400);
        picker.setWrapSelectorWheel(true);

        // change divider color by reflection
        setDividerColor(picker, R.color.colorAccent);

        builder.setPositiveButton(R.string.dialog_about_confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
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
