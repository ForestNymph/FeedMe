package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class MailFormatterManager {

    static private List<Product> mValues;

    static public String getAllAddedProductsToMail(Context context) {

        initializeData(context);

        int sizeOfWhitespace = getSizeOfLongestProduct() + 3;
        String content = "";

        for (int i = 0; i < mValues.size(); ++i) {
            content += mValues.get(i).getName();
            content += getWhiteSpace(sizeOfWhitespace - mValues.get(i).getName().length());
            content += mValues.get(i).getAmount();
            content += " gramm\n";
        }
        return content;
    }

    static private void initializeData(Context context) {
        ProductDataSource mAddedProductsDataSource = new ProductDataSource(context);
        mAddedProductsDataSource.open();
        mValues = mAddedProductsDataSource.getAllAddedProducts();
        mAddedProductsDataSource.close();
    }

    static private int getSizeOfLongestProduct() {
        int longest_product = 0;
        for (int i = 0; i < mValues.size(); ++i) {
            String next = mValues.get(i).getName();
            if (next.length() > longest_product) {
                longest_product = next.length();
            }
        }
        return longest_product;
    }

    static private String getWhiteSpace(int size) {
        StringBuilder out = new StringBuilder();
        while (size-- > 0) {
            out.append(" ");
        }
        return out.toString();
    }
}
