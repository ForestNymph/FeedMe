package pl.grudowska.feedme.alghoritms;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.SummaryResult;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.DatabaseManager;

public class CalculateSummary {

    public static List<SummaryResult> calculate(Context context) {

        List<Product> addedProducts = DatabaseManager.getAddedProductsDB(context);

        if (addedProducts.size() == 0) {
            return null;
        } else {
            double constant = 100;
            double factor = 0;
            double kcal = 0;
            double protein = 0;
            double carbohydrates = 0;
            double fiber = 0;
            double fats = 0;
            double saturated = 0, checkFatsData = 0;
            double monosaturated = 0;
            double omega3 = 0;
            double omega6 = 0;
            double amount = 0;

            for (int i = 0; i < addedProducts.size(); ++i) {
                factor = addedProducts.get(i).getAmount() / constant;

                kcal += addedProducts.get(i).getKcal() * factor;
                protein += addedProducts.get(i).getProtein() * factor;
                carbohydrates += addedProducts.get(i).getCarbohydrates() * factor;
                fiber += addedProducts.get(i).getFiber() * factor;
                fats += addedProducts.get(i).getFats() * factor;
                amount += addedProducts.get(i).getAmount();
                checkFatsData = addedProducts.get(i).getFatsSaturated();
                if (checkFatsData == -1) {
                    // do not calculate fats, no data
                } else {
                    saturated += addedProducts.get(i).getFatsSaturated() * factor;
                    monosaturated += addedProducts.get(i).getFatsMonounsaturated() * factor;
                    omega3 += addedProducts.get(i).getOmega3() * factor;
                    omega6 += addedProducts.get(i).getOmega6() * factor;
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

        List<Product> addedProducts = DatabaseManager.getAddedProductsDB(context);

        double constant = 100;
        double factor = 0;
        double kcal = 0;

        for (int i = 0; i < addedProducts.size(); ++i) {
            factor = addedProducts.get(i).getAmount() / constant;
            kcal += addedProducts.get(i).getKcal() * factor;
        }
        return (int) kcal;
    }
}
