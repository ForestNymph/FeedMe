package pl.grudowska.feedme.databases;

public class Product {

    public long id;
    public String type;
    public String name;
    public int def1;
    public int def2;
    public int def3;
    public double kcal;
    public double protein;
    public double carbohydrates;
    public double fiber;
    public double fats;
    public double fatsSaturated;
    public double fatsMonounsaturated;
    public double omega3;
    public double omega6;
    public double amount;

    public int getKcalRelatedWithAmount() {
        return (int) (amount / 100 * kcal);
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name + " " + amount;
    }
}
