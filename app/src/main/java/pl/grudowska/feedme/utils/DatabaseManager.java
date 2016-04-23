package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.databases.ArchivedProduct;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.SupplementaryInfoDataSource;
import pl.grudowska.feedme.databases.ProductType;
import pl.grudowska.feedme.databases.SummaryRange;

public class DatabaseManager {

    public static List<ArchivedProduct> getArchivedProductsDB(Context context) {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(context);
        dataSource.open();
        List<ArchivedProduct> values = dataSource.getAllArchivedLists();
        dataSource.close();

        return values;
    }

    public static List<Product> getAddedProductsDB(Context context) {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(context);
        dataSource.open();
        List<Product> values = dataSource.getAllAddedProducts();
        dataSource.close();

        return values;
    }

    public static List<ProductType> getTypesProductDB(Context context) {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(context);
        dataSource.open();
        List<ProductType> values = dataSource.getAllTypes();
        dataSource.close();

        return values;
    }

    public static List<SummaryRange> getSummariesProductDB(Context context) {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(context);
        dataSource.open();
        List<SummaryRange> values = dataSource.getAllSummaries();
        dataSource.close();

        return values;
    }
}
