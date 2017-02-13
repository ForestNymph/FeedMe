package pl.grudowska.feedme.alghoritms;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.dialogFragments.WarningLimitDialogFragment;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.SharedPreferencesManager;
import pl.grudowska.feedme.utils.SummaryResult;

public class CalculateSummary {

    public static List<SummaryResult> calculate(Context context) {

        List<Product> addedProducts = DatabaseManager.getAllAddedProductsDB(context);

        if (addedProducts.isEmpty()) {
            return null;
        } else {
            double constant = 100;
            double factor;
            double kcal = 0;
            double protein = 0;
            double carbohydrates = 0;
            double fiber = 0;
            double fats = 0;
            double saturated = 0, checkFatsData;
            double monosaturated = 0;
            double omega3 = 0;
            double omega6 = 0;
            double amount = 0;

            for (int i = 0; i < addedProducts.size(); ++i) {
                factor = addedProducts.get(i).amount / constant;

                kcal += addedProducts.get(i).kcal * factor;
                protein += addedProducts.get(i).protein * factor;
                carbohydrates += addedProducts.get(i).carbohydrates * factor;
                fiber += addedProducts.get(i).fiber * factor;
                fats += addedProducts.get(i).fats * factor;
                amount += addedProducts.get(i).amount;
                checkFatsData = addedProducts.get(i).fatsSaturated;
                if (checkFatsData != -1) {
                    saturated += addedProducts.get(i).fatsSaturated * factor;
                    monosaturated += addedProducts.get(i).fatsMonounsaturated * factor;
                    omega3 += addedProducts.get(i).omega3 * factor;
                    omega6 += addedProducts.get(i).omega6 * factor;
                }
            }
            return createTextSummary(kcal, protein, carbohydrates, fiber,
                    fats, saturated, monosaturated, omega3, omega6, amount);
        }
    }

    static private List<SummaryResult> createTextSummary(double kcal, double protein, double carbohydrates,
                                                         double fiber, double fats, double saturated, double monosaturated,
                                                         double omega3, double omega6, double amount) {

        List<SummaryResult> results = new ArrayList<>();
        results.add(new SummaryResult("Energy: ", "energy", (int) kcal, " kcal"));
        results.add(new SummaryResult("Weight: ", "weight", (int) amount, " g"));
        results.add(new SummaryResult("Protein (B): ", "protein", (int) protein, " g"));
        results.add(new SummaryResult("Carbohydrate (W): ", "carbohydrates", (int) carbohydrates, " g"));
        results.add(new SummaryResult("Fat (T): ", "fat", (int) fats, " g"));
        results.add(new SummaryResult("Saturated fat: ", "saturated", (int) saturated, " g"));
        results.add(new SummaryResult("Monosaturated: ", "monosaturated", (int) monosaturated, " g"));
        results.add(new SummaryResult("Omega3: ", "omega3", (int) omega3, " g"));
        results.add(new SummaryResult("Omega6: ", "omega6", (int) omega6, " g"));
        results.add(new SummaryResult("Fiber (N): ", "fiber", (int) fiber, " g"));

        return results;
    }

    static public int getTotalKcal(Context context) {

        List<Product> addedProducts = DatabaseManager.getAllAddedProductsDB(context);

        double constant = 100;
        double factor;
        double kcal = 0;

        for (int i = 0; i < addedProducts.size(); ++i) {
            factor = addedProducts.get(i).amount / constant;
            kcal += addedProducts.get(i).kcal * factor;
        }
        return (int) kcal;
    }

    static public void warningIfCalorieLimitExceeded(Context context) {
        if (CalculateSummary.getTotalKcal(context) > SharedPreferencesManager.loadDataInt(context, "limit", 2300)) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            WarningLimitDialogFragment dialog = new WarningLimitDialogFragment();
            dialog.show(fm, "");
        }
    }
}
