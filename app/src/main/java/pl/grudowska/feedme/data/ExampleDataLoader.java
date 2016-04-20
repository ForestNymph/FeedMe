package pl.grudowska.feedme.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.databases.ProductType;
import pl.grudowska.feedme.utils.DatabaseManager;

public class ExampleDataLoader {

    public static List<String> getTypeTitles(Context context) {
        List<String> titles = new ArrayList<>();
        List<ProductType> type = DatabaseManager.getTypesProductDB(context);

        for (int i = 0; i < type.size(); ++i) {
            titles.add(type.get(i).getTypeName());
        }
        return titles;
    }

    public static void inflateProductType(Context context) {
        ProductDataSource dataSource = new ProductDataSource(context);
        dataSource.open();

        if (dataSource.getAllTypes().size() != 0) {
            dataSource.deleteAll();
        }

        dataSource.createType("BREAD", R.drawable.bread);
        dataSource.createType("VEGETABLES", R.drawable.vegetables);
        dataSource.createType("MEAT", R.drawable.meat);
        dataSource.createType("DIARY", R.drawable.diary);
        dataSource.createType("FRUITS", R.drawable.fruits);
        dataSource.createType("EXTRAS", R.drawable.extras);
        dataSource.createType("FISH", R.drawable.fish);
        dataSource.createType("PRODUCTS SWEETENED", R.drawable.sweetened);
        dataSource.createType("CEREALS", R.drawable.cereals);
        dataSource.createType("SWEETS", R.drawable.sweets);
        dataSource.createType("GRAINS", R.drawable.grains);
        dataSource.createType("BEVERAGES", R.drawable.beverages);
        dataSource.createType("PREPARED FOOD", R.drawable.prepared);
        dataSource.createType("OILS", R.drawable.oils);
        dataSource.createType("MACARONI", R.drawable.macaroni);

        // Example data

        dataSource.createProduct("DIARY", "smietana", 100, 200, 300, 134.000000, 2.700000, 3.900000, 0.000000, 12.000000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("GRAINS", "ryz dziki", 100, 200, 300, 357.000000, 14.700000, 74.000000, 6.200000, 1.100000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("FRUITS", "awokado", 100, 200, 300, 160.000000, 2.000000, 8.530000, 6.700000, 14.660000, 2.100000, 9.799000, 0.111000, 1.705000, 0.000000);
        dataSource.createProduct("PREPARED FOOD", "frytki", 100, 200, 300, 331.000000, 3.500000, 41.700000, 0.000000, 17.400000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("SWEETS", "czekolada luksusowa wedel mleczna", 100, 200, 300, 579.000000, 9.100000, 48.000000, 3.700000, 38.000000, 14.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("CEREALS", "otreby zytnie", 100, 200, 300, 281.000000, 15.000000, 26.000000, 0.000000, 4.300000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("PRODUCTS SWEETENED", "ksylitol", 100, 200, 300, 240.000000, 0.000000, 100.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("GRAINS", "fasola czerwona", 100, 200, 300, 288.000000, 21.400000, 45.900000, 0.000000, 2.700000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("PRODUCTS SWEETENED", "dzem", 100, 200, 300, 278.000000, 0.400000, 68.900000, 1.100000, 0.100000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("PREPARED FOOD", "zurek z kielbasa i jajkiem", 100, 200, 300, 109.000000, 5.000000, 5.000000, 0.000000, 8.000000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("FISH", "ostrygi swieze", 100, 200, 300, 81.000000, 9.400000, 4.900000, 0.000000, 0.000000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("VEGETABLES", "imbir", 100, 200, 300, 80.000000, 1.800000, 17.800000, 2.000000, 0.700000, 0.200000, 0.200000, 0.034000, 0.120000, 0.000000);
        dataSource.createProduct("DIARY", "mleko (2.0%)", 100, 200, 300, 49.000000, 3.100000, 4.700000, 0.000000, 2.000000, 1.300000, 0.500000, 0.020000, 0.080000, 0.000000);
        dataSource.createProduct("EXTRAS", "monster beef", 100, 200, 300, 374.242424, 29.700000, 0.500000, 0.000000, 0.500000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("BEVERAGES", "sok z limonki", 100, 200, 300, 27.000000, 0.440000, 9.010000, 0.400000, 0.100000, 0.010000, 0.010000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("OILS", "olej wielkopolski", 100, 200, 300, 900.000000, 0.000000, 0.000000, 0.000000, 100.000000, 8.000000, 63.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("BREAD", "chleb zytni", 100, 200, 300, 200.000000, 6.000000, 51.000000, 8.000000, 2.000000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("PREPARED FOOD", "ziarno i serek light", 100, 200, 300, 116.880000, 7.530000, 7.715000, 1.500000, 6.815000, 1.983000, 1.537000, 1.067000, 1.226000, 0.000000);
        dataSource.createProduct("PREPARED FOOD", "lasagne", 100, 200, 300, 134.000000, 7.000000, 15.000000, 1.700000, 4.900000, 2.300000, 1.800000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("CEREALS", "otreby owsiane", 100, 200, 300, 331.000000, 15.600000, 41.900000, 26.000000, 5.500000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("GRAINS", "ryz jasminowy", 100, 200, 300, 346.000000, 7.600000, 77.000000, 1.500000, 0.500000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("GRAINS", "fasola mung", 100, 200, 300, 347.000000, 23.860000, 62.620000, 0.000000, 1.150000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("MACARONI", "makaron pelne ziarno", 100, 200, 300, 357.000000, 15.200000, 68.000000, 0.000000, 1.300000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("MEAT", "zeberka", 100, 200, 300, 263.000000, 19.100000, 0.000000, 0.000000, 20.100000, 8.100000, 8.600000, 0.000000, 0.493000, 0.000000);
        dataSource.createProduct("MEAT", "piers z indyka grillowana", 100, 200, 300, 120.000000, 24.400000, 0.000000, 0.000000, 1.400000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);
        dataSource.createProduct("CEREALS", "zarodki pszenne", 100, 200, 300, 349.000000, 27.500000, 31.500000, 14.000000, 9.400000, -1.000000, 0.000000, 0.000000, 0.000000, 0.000000);

        dataSource.close();
    }
}
