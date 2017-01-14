package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.Product;

public class SearchEngine {

    final private List<Product> mProducts;
    final private List<Product> mResult;

    public SearchEngine(Context context) {
        mProducts = DatabaseManager.getAllProductsDB(context);
        mResult = new ArrayList<>();
    }

    // TODO case sensitive
    public List<Product> search(String word) {
        Product product;
        mResult.clear();
        for (int i = 0; i < mProducts.size(); ++i) {
            product = mProducts.get(i);
            if ((product.name).contains(word)) {
                mResult.add(product);
            }
        }
        return mResult;
    }
}
