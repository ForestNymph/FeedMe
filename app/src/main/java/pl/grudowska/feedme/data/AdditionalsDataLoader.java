package pl.grudowska.feedme.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.databases.ProductType;
import pl.grudowska.feedme.databases.SupplementaryInfoDataSource;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class AdditionalsDataLoader {

    public static List<String> getTypeTitles(Context context) {
        List<String> titles = new ArrayList<>();
        List<ProductType> type = DatabaseManager.getTypesProductDB(context);

        for (int i = 0; i < type.size(); ++i) {
            titles.add(type.get(i).typeName);
        }
        return titles;
    }

    public static void inflateProductType(Context context) {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(context);
        dataSource.open();

        if (dataSource.getAllTypes().size() != 0) {
            dataSource.deleteAllTypes();
        }

        // Type objects db
        dataSource.createType("DIARY", R.drawable.diary);
        dataSource.createType("MEAT", R.drawable.meat);
        dataSource.createType("PREPARED FOOD", R.drawable.prepared);
        dataSource.createType("GRAINS", R.drawable.grains);
        dataSource.createType("VEGETABLES", R.drawable.vegetables);
        dataSource.createType("SWEETENED PRODUCTS", R.drawable.sweetened);
        dataSource.createType("BREAD", R.drawable.bread);
        dataSource.createType("SWEETS", R.drawable.sweets);
        dataSource.createType("OILS", R.drawable.oils);
        dataSource.createType("BEVERAGES", R.drawable.beverages);
        dataSource.createType("FRUITS", R.drawable.fruits);
        dataSource.createType("FISH", R.drawable.fish);
        dataSource.createType("CEREALS", R.drawable.cereals);
        dataSource.createType("EXTRAS", R.drawable.extras);
        dataSource.createType("MACARONI", R.drawable.macaroni);
        dataSource.close();

        inflateProductSummary(context);

    }

    public static void inflateProductSummary(Context context) {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(context);
        dataSource.open();

        if (dataSource.getAllSummaries().size() != 0) {
            dataSource.deleteAllSummaries();
        }
        // Summary objects db
        dataSource.createSummaryRange("energy", SharedPreferencesManager.loadDataInt(context, "limit", 2300), 2100);
        dataSource.createSummaryRange("protein", 2000, 160);
        dataSource.createSummaryRange("carbohydrates", 20, 10);
        dataSource.createSummaryRange("fat", 20, 10);
        dataSource.createSummaryRange("saturated", 20, 10);
        dataSource.createSummaryRange("monosaturated", 20, 10);
        dataSource.createSummaryRange("omega3", 20, 10);
        dataSource.createSummaryRange("omega6", 20, 10);
        dataSource.createSummaryRange("fiber", 40, 25);

        dataSource.close();
    }
}