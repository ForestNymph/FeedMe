package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.grudowska.feedme.utils.SharedPreferencesManager;

import static android.app.PendingIntent.getService;

public class TimeDialogFragment extends DialogFragment {

    private AlertDialog mDialog;
    private TimePicker mPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View timeDialogView = inflater.inflate(R.layout.time_dialog, null);

        TextView time = (TextView) timeDialogView.findViewById(R.id.time_current);
        String timeText = " " + SharedPreferencesManager.loadDataString(getActivity(), "time_hour_str", "23") + ":"
                + (SharedPreferencesManager.loadDataString(getActivity(), "time_minute_str", "59"));
        time.append(timeText);

        mPicker = (TimePicker) timeDialogView.findViewById(R.id.picker_time_view);
        mPicker.setIs24HourView(true);

        final CheckBox checkbox = (CheckBox) timeDialogView.findViewById(R.id.checkbox_view);
        checkbox.setChecked(SharedPreferencesManager.loadDataBoolean(getActivity(), "checkbox_state", false));
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox.isChecked()) {
                    SharedPreferencesManager.saveDataBoolean(getActivity(), "checkbox_state", true);
                } else {
                    SharedPreferencesManager.saveDataBoolean(getActivity(), "checkbox_state", false);
                }
            }
        });

        builder.setView(timeDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int minute, hour;

                                if (Build.VERSION.SDK_INT >= 23) {
                                    hour = mPicker.getHour();
                                    minute = mPicker.getMinute();
                                } else {
                                    //noinspection deprecation
                                    hour = mPicker.getCurrentHour();
                                    //noinspection deprecation
                                    minute = mPicker.getCurrentMinute();
                                }
                                SharedPreferencesManager.saveDataString(getActivity(),
                                        "time_hour_str", String.valueOf(hour));
                                SharedPreferencesManager.saveDataString(getActivity(),
                                        "time_minute_str", (minute < 10) ? "0" + minute : String.valueOf(minute));
                                SharedPreferencesManager.saveDataInt(getActivity(), "time_hour_int", hour);
                                SharedPreferencesManager.saveDataInt(getActivity(), "time_minute_int", minute);

                                // if sending daily summaries is ON - get the current time from the picker
                                // and set it for sending daily summaries time
                                if (checkbox.isChecked()) {
                                    startDailySummaryEmailService();
                                } else {
                                    stopDailySummaryEmailService();
                                }
                            }
                        }
                ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TimeDialogFragment.this.getDialog().cancel();
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

    // To check all setting alarms by app
    // adb shell dumpsys alarm | grep pl.grudowska.feedme
    // Logs reading:
    // https://stackoverflow.com/questions/28742884/how-to-read-adb-shell-dumpsys-alarm-output

    // ref. https://stackoverflow.com/questions/11681095/cancel-an-alarmmanager-pendingintent-in-another-pendingintent
    // To stop service: cancel pending intent, cancel the alarm using alarm manager
    private void stopDailySummaryEmailService() {
        Intent alarmIntent = new Intent(getActivity(), DailySummaryEmailIntentService.class);
        PendingIntent pending = PendingIntent.getService(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pending);
        pending.cancel();
    }

    // PendingIntent.FLAG_UPDATE_CURRENT will return a reference to the existing one already created,
    // or create one if it doesn't currently exist
    // ref. https://stackoverflow.com/questions/9823408/cancelling-a-pendingintent
    private void startDailySummaryEmailService() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(getActivity(), DailySummaryEmailIntentService.class);
        PendingIntent pending = getService(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int hour = SharedPreferencesManager.loadDataInt(getActivity(), "time_hour_int", 23);
        int minute = SharedPreferencesManager.loadDataInt(getActivity(), "time_minute_int", 59);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // setInexactRepeating(), Android synchronizes repeating alarms from multiple apps
        // and fires them at the same time This reduces the total number of times
        // the system must wake the device, thus reducing drain on the battery
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);
    }
}
