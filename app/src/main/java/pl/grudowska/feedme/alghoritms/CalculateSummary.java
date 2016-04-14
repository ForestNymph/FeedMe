package pl.grudowska.feedme.alghoritms;

import android.content.Context;

import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class CalculateSummary {

    public static String calculate(Context context) {
        ProductDataSource dataSource = new ProductDataSource(context);
        dataSource.open();
        List<Product> addedProducts = dataSource.getAllAddedProducts();
        dataSource.close();

        double constant = 100; // 100
        double factor = 0;

        double kcal = 0;
        double protein = 0;
        double carbohydrates = 0;
        double roughage = 0;
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
            roughage += addedProducts.get(i).getRoughage() * factor;
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
        return createTextSummary(kcal, protein, carbohydrates, roughage,
                fats, saturated, monosaturated, omega3, omega6, amount);
    }

    static private String createTextSummary(double kcal, double protein, double carbohydrates,
                                            double roughage, double fats, double saturated, double monosaturated,
                                            double omega3, double omega6, double amount) {

        String summary = "Total Energy: " + String.format("%.1f", kcal) + " kcal\n"
                + "Total Weight: " + String.format("%.1f", amount) + " gramm\n"
                + "Total Protein: (B): " + String.format("%.1f", protein) + " gramm\n"
                + "Total Carbohydrate (W): " + String.format("%.1f", carbohydrates) + " gramm\n"
                + "Total Fat (T): " + String.format("%.1f", fats) + " gramm\n"
                + "   Total Saturated fat: " + String.format("%.1f", saturated) + " gramm\n"
                + "   Total Monosaturated: " + String.format("%.1f", monosaturated) + " gramm\n"
                + "   Total Omega3: " + String.format("%.1f", omega3) + " gramm\n"
                + "   Total Omega6: " + String.format("%.1f", omega6) + " gramm\n"
                + "Total Fiber (N): " + String.format("%.1f", roughage) + " gramm\n";

        return summary;
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
