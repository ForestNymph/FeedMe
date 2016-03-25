package pl.grudowska.feedme;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import pl.grudowska.feedme.util.BitmapCache;


public class SpecificFoodTypeListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;
    private final BitmapCache mMemoryCache;
    // dummy data
    ArrayList<String> mFoodName = new ArrayList<>(Arrays.asList("Product One", "Product Two", "Product Three",
            "Product 1", "Product 2", "Product 3",
            "Product 4", "Product 5", "Product 6",
            "Product 7", "Product 8", "Product 9",
            "Product 10", "Product 11", "Product 12",
            "Product 13", "Product 14", "Product 15",
            "Product 16", "Product 17", "Product 18",
            "Product 19", "Product 20", "Product 21",
            "Product 22", "Product 23", "Product 24",
            "Product 25", "Product 26", "Product 27"));

    public SpecificFoodTypeListItemAdapter(final Context context) {
        super(context, R.layout.activity_specific_food_item_card, R.id.card_title, R.id.card_content);
        mContext = context;
        mMemoryCache = new BitmapCache();

        for (int i = 0; i < mFoodName.size(); ++i) {
            add(i);
        }
    }

    @NonNull
    @Override
    public View getTitleView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = new TextView(mContext);
        }
        tv.setText(mFoodName.get(position));
        return tv;
    }

    @NonNull
    @Override
    public View getContentView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        // Button button = (Button) parent.findViewById(R.id.button_add);
        // Button buttonView = (Button) convertView;

        ImageView imageView = (ImageView) convertView;
        if (imageView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

//        if (buttonView == null) {
//            buttonView = new Button(mContext);
//
//        }

        int imageResId;
        switch (getItem(position) % mFoodName.size()) {
            case 0:
                imageResId = R.drawable.bread;
                break;
            case 1:
                imageResId = R.drawable.fish;
                break;
            case 2:
                imageResId = R.drawable.grains;
                break;
            default:
                imageResId = R.drawable.diary;
        }

        Bitmap bitmap = getBitmapFromMemCache(imageResId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
            addBitmapToMemoryCache(imageResId, bitmap);
        }
        imageView.setImageBitmap(bitmap);

        return imageView;
    }

    private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }
}
