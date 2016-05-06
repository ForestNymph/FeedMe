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

import java.util.List;

import pl.grudowska.feedme.databases.DailyRecap;
import pl.grudowska.feedme.utils.DatabaseManager;

public class ArchivedArrayAdapter extends ArrayAdapter<DailyRecap> {

    private final Context mContext;
    private List<DailyRecap> mTitleBar;

    ArchivedArrayAdapter(final Context context) {
        mContext = context;
        mTitleBar = DatabaseManager.getAllArchivedDatesWithCaloriesSummaryDB(mContext);

        for (int i = 0; i < mTitleBar.size(); ++i) {
            add(mTitleBar.get(i));
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_archived_card_title, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.archived_date_tv);
            viewHolder.totalkcal_tv = (TextView) convertView.findViewById(R.id.archived_totalkcal_tv);
            viewHolder.details_btn = (Button) convertView.findViewById(R.id.archived_details_btn);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.date_tv.setText(mTitleBar.get(position).date);
        String kcalValue = String.valueOf(mTitleBar.get(position).totalKcal + " kcal");
        viewHolder.totalkcal_tv.setText(kcalValue);
        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArchivedListDialogFragment list = new ArchivedListDialogFragment();
                FragmentActivity activity = (FragmentActivity) (mContext);
                FragmentManager fm = activity.getSupportFragmentManager();
                list.show(fm, mTitleBar.get(position).date);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        if (mTitleBar == null) {
            return 0;
        }
        return mTitleBar.size();
    }

    @Override
    public void clear() {
        mTitleBar.removeAll(mTitleBar);
        notifyDataSetChanged();
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView date_tv;
        TextView totalkcal_tv;
        Button details_btn;
    }
}
