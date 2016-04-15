package pl.grudowska.feedme.alghoritms;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.SummaryResult;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class CalculateSummary {

    public static List<SummaryResult> calculate(Context context) {
        ProductDataSource dataSource = new ProductDataSource(context);
        dataSource.open();
        List<Product> addedProducts = dataSource.getAllAddedProducts();
        dataSource.close();

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
        results.add(new SummaryResult("Total Energy: ", (int) kcal, " kcal"));
        results.add(new SummaryResult("Total Weight: ", (int) amount, " gram"));
        results.add(new SummaryResult("Total Protein (B): ", (int) protein, " gram"));
        results.add(new SummaryResult("Total Carbohydrate (W): ", (int) carbohydrates, " gram"));
        results.add(new SummaryResult("Total Fat (T): ", (int) fats, " gram"));
        results.add(new SummaryResult("Total Saturated fat: ", (int) saturated, " gram"));
        results.add(new SummaryResult("Total Monosaturated: ", (int) monosaturated, " gram"));
        results.add(new SummaryResult("Total Omega3: ", (int) omega3, " gram"));
        results.add(new SummaryResult("Total Omega6: ", (int) omega6, " gram"));
        results.add(new SummaryResult("Total Fiber (N): ", (int) fiber, " gram"));

        return results;
    }

    static public int getTotalKcal(Context context) {

        ProductDataSource dataSource = new ProductDataSource(context);
        dataSource.open();
        List<Product> addedProducts = dataSource.getAllAddedProducts();
        dataSource.close();

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
