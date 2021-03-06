package pl.grudowska.feedme.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.DailyRecap;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.databases.ProductType;
import pl.grudowska.feedme.databases.RecapInfoDataSource;
import pl.grudowska.feedme.databases.SummaryRange;

public class DatabaseManager {

    public static List<Product> getAllProductsDB(Context context) {
        ProductDataSource dataSource = new ProductDataSource(context);
        List<Product> values = new ArrayList<>();
        try {
            dataSource.openDataBase();
            values = dataSource.getAllProducts();
        } catch (ProductDataSource.DatabaseNotExistException e) {
            e.printStackTrace();
            Toast.makeText(context, "Database needs to be upadated", Toast.LENGTH_SHORT).show();
        }
        dataSource.close();

        return values;
    }

    public static ArrayList<Product> getAllAddedProductsDB(Context context) {
        AddedProductDataSource dataSource = new AddedProductDataSource(context);
        dataSource.open();
        ArrayList<Product> values = dataSource.getAllAddedProducts();
        dataSource.close();

        return values;
    }

    public static List<ProductType> getTypesAllProductsDB(Context context) {
        RecapInfoDataSource dataSource = new RecapInfoDataSource(context);
        dataSource.open();
        List<ProductType> values = dataSource.getTypesAllProducts();
        dataSource.close();

        return values;
    }

    public static List<SummaryRange> getAllSummariesDB(Context context) {
        RecapInfoDataSource dataSource = new RecapInfoDataSource(context);
        dataSource.open();
        List<SummaryRange> values = dataSource.getAllSummaries();
        dataSource.close();

        return values;
    }

    public static ArrayList<DailyRecap> getAllDailyRecapsDB(Context context) {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(context);
        dataSource.open();
        ArrayList<DailyRecap> values = dataSource.getAllArchivedDailyRecaps();
        dataSource.close();

        return values;
    }

    public static List<Product> getArchivedProductsByDateDB(Context context, String date) {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(context);
        dataSource.open();
        List<Product> values = dataSource.getArchivedProductsByDate(date);
        dataSource.close();

        return values;
    }

    public static String getContentMailByDateDB(Context context, String date) {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(context);
        dataSource.open();
        String value = dataSource.getContentMailByDate(date);
        dataSource.close();

        return value;
    }
}
