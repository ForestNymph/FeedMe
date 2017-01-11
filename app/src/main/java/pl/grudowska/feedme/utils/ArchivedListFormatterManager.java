package pl.grudowska.feedme.utils;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.SummaryResult;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.Product;

public class ArchivedListFormatterManager {

    static public String createMailContent(Context context) {

        List<Product> values = DatabaseManager.getAllAddedProductsDB(context);

        Product prod;
        // get only needed data (product name, kcal and amount)
        String contentMail = "\nProducts:\n";
        for (int i = 0; i < values.size(); ++i) {
            prod = values.get(i);
            contentMail += prod.name;
            contentMail += "\n  ";
            contentMail += prod.getKcalRelatedWithAmount();
            contentMail += " kcal\n  ";
            contentMail += prod.amount;
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
