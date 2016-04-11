package pl.grudowska.feedme.data;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.database.MainType;
import pl.grudowska.feedme.database.MainTypeDataSource;

public class MainTypeDataLoader {

    public static List<String> getTypeTitles(Context context) {
        MainTypeDataSource mMainTypeDataSource = new MainTypeDataSource(context);
        mMainTypeDataSource.open();
        List<String> titles = new ArrayList<>();
        List<MainType> type = mMainTypeDataSource.getAllAddedTypes();
        mMainTypeDataSource.close();

        for (int i = 0; i < type.size(); ++i) {
            titles.add(type.get(i).getTypeName());
        }
        return titles;
    }

    public static void inflateMainTypeDB(Context context) {
        MainTypeDataSource mMainTypeDataSource = new MainTypeDataSource(context);
        mMainTypeDataSource.open();

        // clear db
        if (mMainTypeDataSource.getAllAddedTypes().size() != 0) {
            mMainTypeDataSource.deleteAllItems();
        }

        // load new modified db
        mMainTypeDataSource.createMainType("BREAD", R.drawable.bread);
        mMainTypeDataSource.createMainType("VEGETABLES", R.drawable.vegetables);
        mMainTypeDataSource.createMainType("MEAT", R.drawable.meat);
        mMainTypeDataSource.createMainType("DIARY", R.drawable.diary);
        mMainTypeDataSource.createMainType("FRUITS", R.drawable.fruits);
        mMainTypeDataSource.createMainType("EXTRAS", R.drawable.extras);
        mMainTypeDataSource.createMainType("FISH", R.drawable.fish);
        mMainTypeDataSource.createMainType("PRODUCTS SWEETENED", R.drawable.sweetened);
        mMainTypeDataSource.createMainType("CEREALS", R.drawable.cereals);
        mMainTypeDataSource.createMainType("SWEETS", R.drawable.sweets);
        mMainTypeDataSource.createMainType("GRAINS", R.drawable.grains);
        mMainTypeDataSource.createMainType("BEVERAGES", R.drawable.beverages);
        mMainTypeDataSource.createMainType("PREPARED FOOD", R.drawable.prepared);
        mMainTypeDataSource.createMainType("OILS", R.drawable.oils);
        mMainTypeDataSource.createMainType("MACARONI", R.drawable.macaroni);

        mMainTypeDataSource.close();
    }
}
