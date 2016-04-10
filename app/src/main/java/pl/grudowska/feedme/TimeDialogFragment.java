package pl.grudowska.feedme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class TimeDialogFragment extends DialogFragment {

    private String mHourStr, mMinuteStr;
    private int mHourInt, mMinuteInt;
    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View timeDialogView = inflater.inflate(R.layout.time_dialog, null);

        TextView time = (TextView) timeDialogView.findViewById(R.id.time_current);
        String timeText = SharedPreferencesManager.loadDataString(getActivity(), "time_hour_str", "00") + ":"
                + (SharedPreferencesManager.loadDataString(getActivity(), "time_minute_str", "00"));
        time.append(timeText);

        TimePicker picker = (TimePicker) timeDialogView.findViewById(R.id.picker_time_view);
        picker.setIs24HourView(true);

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHourStr = hourOfDay + "";
                mMinuteStr = (minute < 10) ? "0" + minute : minute + "";
                mHourInt = hourOfDay;
                mMinuteInt = minute;
            }
        });

        builder.setView(timeDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferencesManager.saveDataString(getActivity(), "time_hour_str", mHourStr);
                                SharedPreferencesManager.saveDataString(getActivity(), "time_minute_str", mMinuteStr);
                                SharedPreferencesManager.saveDataInt(getActivity(), "time_hour_int", mHourInt);
                                SharedPreferencesManager.saveDataInt(getActivity(), "time_minute_int", mMinuteInt);

                                startDailySummaryEmailService();
                            }
                        }

                ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
                TimeDialogFragment.this.getDialog().cancel();
            }
        });

        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
            }
        });

        return mDialog;
    }

    private void startDailySummaryEmailService() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(getActivity(), DailySummaryEmailIntentService.class);
        PendingIntent pending = PendingIntent.getService(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int hour = SharedPreferencesManager.loadDataInt(getActivity(), "time_hour_int", 24);
        int minute = SharedPreferencesManager.loadDataInt(getActivity(), "time_minute_int", 00);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);
    }
}
