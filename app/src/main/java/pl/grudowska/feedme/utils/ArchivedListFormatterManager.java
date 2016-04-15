package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.SummaryResult;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class ArchivedListFormatterManager {

    static private List<Product> mValues;

    static public String createContentNames(Context context) {

        initializeDataSource(context);

        String allNames = "";
        for (int i = 0; i < mValues.size(); ++i) {
            allNames += mValues.get(i).getName();
            allNames += "\n";
        }
        return allNames;
    }

    static public String createContentAmounts(Context context) {

        initializeDataSource(context);

        String allAmounts = "";
        for (int i = 0; i < mValues.size(); ++i) {
            allAmounts += mValues.get(i).getAmount();
            allAmounts += " gramm\n";
        }
        return allAmounts;
    }

    static public String createContentFull(Context context) {

        initializeDataSource(context);

        String content = "";
        for (int i = 0; i < mValues.size(); ++i) {
            content += mValues.get(i).getName();
            content += " ";
            content += mValues.get(i).getAmount();
            content += " gramm\n";
        }
        return content;
    }

    static public String createMailContent(Context context) {

        initializeDataSource(context);

        // get only needed data (product name and amount)
        String contentMail = "";
        for (int i = 0; i < mValues.size(); ++i) {
            contentMail += mValues.get(i).getName();
            contentMail += " ";
            contentMail += mValues.get(i).getAmount();
            contentMail += " gramm\n";
        }
        contentMail += addSummary(context);
        return contentMail;
    }

    static private String addSummary(Context context) {
        List<SummaryResult> summary = CalculateSummary.calculate(context);
        String result = "/nSummary:/n";
        for (int i = 0; i < summary.size(); ++i) {
            SummaryResult res = summary.get(i);
            result += res.getResultType();
            result += " ";
            result += res.getAmount();
            result += " ";
            result += res.getUnit();
            result += "\n";
        }
        return result;
    }

    static private void initializeDataSource(Context context) {
        ProductDataSource mAddedProductsDataSource = new ProductDataSource(context);
        mAddedProductsDataSource.open();
        mValues = mAddedProductsDataSource.getAllAddedProducts();
        mAddedProductsDataSource.close();
    }
}
