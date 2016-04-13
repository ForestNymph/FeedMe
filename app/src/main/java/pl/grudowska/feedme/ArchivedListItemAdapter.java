package pl.grudowska.feedme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.AllSentFood;
import pl.grudowska.feedme.databases.AllSentFoodDataSource;
import pl.grudowska.feedme.utils.EmailManager;

public class ArchivedListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;
    private List<AllSentFood> mValues;

    public ArchivedListItemAdapter(final Context context) {
        super(context, R.layout.content_archived_card_content, R.id.card_title, R.id.card_content);
        mContext = context;

        // Get all data from SQL DB
        AllSentFoodDataSource sentDataSource = new AllSentFoodDataSource(context);
        sentDataSource.open();
        mValues = sentDataSource.getAllSentLists();
        sentDataSource.close();

        for (int i = 0; i < mValues.size(); ++i) {
            add(i);
        }
    }

    @NonNull
    @Override
    public View getTitleView(final int position, View convertView, @NonNull final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_archived_card_title, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.card_title_date_tv);
            viewHolder.kcal_summary_tv = (TextView) convertView.findViewById(R.id.card_title_kcal_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.date_tv.setText(mValues.get(position).getDate());
        viewHolder.kcal_summary_tv.setText(mValues.get(position).getKcal());

        return convertView;
    }

    @NonNull
    @Override
    public View getContentView(final int position, View convertView, @NonNull final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_archived_card_content, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.fullday_list_tv = (TextView) convertView.findViewById(R.id.card_content_sent_lists_tv);
            viewHolder.resend_btn = (Button) convertView.findViewById(R.id.resend_btn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fullday_list_tv.setText(mValues.get(position).getContent());
        setListeners(viewHolder, position);

        return convertView;
    }

    private void setListeners(final ViewHolder viewHolder, final int position) {
        viewHolder.resend_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new EmailManager(mContext, mValues.get(position).getDate(), mValues.get(position).getContent());
                Toast.makeText(mContext, R.string.sent_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView date_tv;
        TextView kcal_summary_tv;
        TextView fullday_list_tv;
        Button resend_btn;
    }
}
