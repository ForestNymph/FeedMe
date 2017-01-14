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

import pl.grudowska.feedme.databases.ProductType;
import pl.grudowska.feedme.utils.BitmapCache;
import pl.grudowska.feedme.utils.DatabaseManager;

class MainFoodTypeArrayAdapter extends ArrayAdapter<ProductType> {

    private final Context mContext;
    private final BitmapCache mMemoryCache;

    MainFoodTypeArrayAdapter(final Context context) {

        mContext = context;
        mMemoryCache = new BitmapCache();

        createDatabase();
    }

    void createDatabase() {
        List<ProductType> mProductType = DatabaseManager.getTypesAllProductsDB(mContext);
        for (int i = 0; i < mProductType.size(); ++i) {
            add(mProductType.get(i));
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_main_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.activity_card_food_textview);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.activity_card_food_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position).typeName);
        setImageView(viewHolder, position);

        return convertView;
    }

    private void setImageView(final ViewHolder viewHolder, final int position) {
        int imageResId = getItem(position).resImage;

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