package pl.grudowska.feedme;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

import pl.grudowska.feedme.databases.DailyRecap;

public class ArchivedArrayAdapter extends ArrayAdapter<DailyRecap> {

    private Context mContext;

    ArchivedArrayAdapter(Context context, ArrayList<DailyRecap> recaps) {
        super(recaps);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;
        final DailyRecap recap = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_archived_title_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.archived_date_tv);
            viewHolder.totalkcal_tv = (TextView) convertView.findViewById(R.id.archived_totalkcal_tv);
            viewHolder.details_btn = (Button) convertView.findViewById(R.id.archived_details_btn);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArchivedListDialogFragment list = new ArchivedListDialogFragment();
                FragmentActivity activity = (FragmentActivity) (mContext);
                FragmentManager fm = activity.getSupportFragmentManager();
                list.show(fm, recap.date);
            }
        });
        viewHolder.date_tv.setText(recap.date);
        String kcalValue = String.valueOf(recap.totalKcal + " kcal");
        viewHolder.totalkcal_tv.setText(kcalValue);

        return convertView;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView date_tv;
        TextView totalkcal_tv;
        Button details_btn;
    }
}
