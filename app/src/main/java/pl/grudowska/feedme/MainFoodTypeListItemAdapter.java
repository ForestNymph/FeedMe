package pl.grudowska.feedme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.database.MainType;
import pl.grudowska.feedme.database.MainTypeDataSource;
import pl.grudowska.feedme.utils.BitmapCache;

public class MainFoodTypeListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private final BitmapCache mMemoryCache;
    private List<MainType> mMainTypes;

    MainFoodTypeListItemAdapter(final Context context) {

        mContext = context;
        mMemoryCache = new BitmapCache();

        // Uncomment if data in MainTypeDataLoader class was modified
        // MainTypeDataLoader.inflateMainTypeDB(mContext);

        // get size of database
        MainTypeDataSource mMainTypeDataSource = new MainTypeDataSource(context);
        mMainTypeDataSource.open();
        mMainTypes = mMainTypeDataSource.getAllAddedTypes();
        mMainTypeDataSource.close();

        for (int i = 0; i < mMainTypes.size(); i++) {
            add(i);
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_main_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.activity_card_food_textview);
            convertView.setTag(viewHolder);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.activity_card_food_imageview);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mMainTypes.get(getItem(position)).getTypeName());
        setImageView(viewHolder, position);

        return convertView;
    }

    private void setImageView(final ViewHolder viewHolder, final int position) {
        int imageResId = mMainTypes.get(getItem(position)).getmResImage();

        Bitmap bitmap = getBitmapFromMemCache(imageResId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
            addBitmapToMemoryCache(imageResId, bitmap);
        }
        viewHolder.imageView.setImageBitmap(bitmap);
    }

    private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}