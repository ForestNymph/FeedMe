package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.SummaryResult;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.Product;

public class ArchivedListFormatterManager {

    static private List<Product> mValues;

    static public String createContentNames(Context context) {

        mValues = DatabaseManager.getAddedProductsDB(context);

        String allNames = "";
        for (int i = 0; i < mValues.size(); ++i) {
            allNames += mValues.get(i).getName();
            allNames += "\n";
        }
        return allNames;
    }

    static public String createContentAmounts(Context context) {

        mValues = DatabaseManager.getAddedProductsDB(context);

        String allAmounts = "";
        for (int i = 0; i < mValues.size(); ++i) {
            allAmounts += mValues.get(i).getAmount();
            allAmounts += " g\n";
        }
        return allAmounts;
    }

    static public String createMailContent(Context context) {

        mValues = DatabaseManager.getAddedProductsDB(context);

        Product prod;
        // get only needed data (product name, kcal and amount)
        String contentMail = "\nProducts:\n";
        for (int i = 0; i < mValues.size(); ++i) {
            prod = mValues.get(i);
            contentMail += prod.getName();
            contentMail += "\n  ";
            contentMail += prod.getKcalRelatedWithAmount();
            contentMail += " kcal\n  ";
            contentMail += prod.getAmount();
            contentMail += " g\n";
        }
        contentMail += addSummary(context);
        return contentMail;
    }

    static private String addSummary(Context context) {
        List<SummaryResult> summary = CalculateSummary.calculate(context);
        String result = "\nSummary:\n";
        SummaryResult res;
        for (int i = 0; i < summary.size(); ++i) {
            res = summary.get(i);
            result += res.getResultType();
            result += " ";
            result += res.getAmount();
            result += " ";
            result += res.getUnit();
            result += "\n";
        }
        return result;
    }
}
