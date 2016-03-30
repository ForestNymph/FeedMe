/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.util.Arrays;
import java.util.List;

import pl.grudowska.feedme.util.BitmapCache;

public class MainFoodTypeListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private final BitmapCache mMemoryCache;
    private final List<String> mFoodType;

    MainFoodTypeListItemAdapter(final Context context) {
        mContext = context;
        mMemoryCache = new BitmapCache();
        // get list of food type from resources and convert to Array
        mFoodType = Arrays.asList(context.getResources().getStringArray(R.array.typefood_array));
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.content_main_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.activity_card_food_textview);
            view.setTag(viewHolder);

            viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_card_food_imageview);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // viewHolder.textView.setText(mContext.getString(R.string.card_number, getItem(position) + 1));
        viewHolder.textView.setText(mFoodType.get(getItem(position) % mFoodType.size()));
        setImageView(viewHolder, position);

        return view;
    }

    private void setImageView(final ViewHolder viewHolder, final int position) {
        int imageResId;
        switch (getItem(position) % mFoodType.size()) {
            case 0:
                imageResId = R.drawable.bread;
                break;
            case 1:
                imageResId = R.drawable.vegetables;
                break;
            case 2:
                imageResId = R.drawable.meat;
                break;
            case 3:
                imageResId = R.drawable.diary;
                break;
            case 4:
                imageResId = R.drawable.fruits;
                break;
            case 5:
                imageResId = R.drawable.fish;
                break;
            case 6:
                imageResId = R.drawable.sweets;
                break;
            case 7:
                imageResId = R.drawable.grains;
                break;
            case 8:
                imageResId = R.drawable.rice;
                break;
            case 9:
                imageResId = R.drawable.macaroni;
                break;
            default:
                imageResId = R.drawable.fruits;
        }

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