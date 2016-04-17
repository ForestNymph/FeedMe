package pl.grudowska.feedme.databases;

public class Product {

    private long mId;
    private String mType;
    private String mName;
    private int mDef1;
    private int mDef2;
    private int mDef3;
    private double mKcal;
    private double mProtein;
    private double mCarbohydrates;
    private double mFiber;
    private double mFats;
    private double mFatsSaturated;
    private double mFatsMonounsaturated;
    private double mOmega3;
    private double mOmega6;
    private double mAmount;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getDef1() {
        return mDef1;
    }

    public void setDef1(int def1) {
        mDef1 = def1;
    }

    public int getDef2() {
        return mDef2;
    }

    public void setDef2(int def2) {
        mDef2 = def2;
    }

    public int getDef3() {
        return mDef3;
    }

    public void setDef3(int def3) {
        mDef3 = def3;
    }

    public double getKcal() {
        return mKcal;
    }

    public void setKcal(double kcal) {
        mKcal = kcal;
    }

    public double getProtein() {
        return mProtein;
    }

    public void setProtein(double protein) {
        mProtein = protein;
    }

    public double getCarbohydrates() {
        return mCarbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        mCarbohydrates = carbohydrates;
    }

    public double getFiber() {
        return mFiber;
    }

    public void setFiber(double fiber) {
        mFiber = fiber;
    }

    public double getFats() {
        return mFats;
    }

    public void setFats(double fats) {
        mFats = fats;
    }

    public double getFatsSaturated() {
        return mFatsSaturated;
    }

    public void setFatsSaturated(double fatsSaturated) {
        mFatsSaturated = fatsSaturated;
    }

    public double getFatsMonounsaturated() {
        return mFatsMonounsaturated;
    }

    public void setFatsMonounsaturated(double fatsMonounsaturated) {
        mFatsMonounsaturated = fatsMonounsaturated;
    }

    public double getOmega3() {
        return mOmega3;
    }

    public void setOmega3(double omega3) {
        mOmega3 = omega3;
    }

    public double getOmega6() {
        return mOmega6;
    }

    public void setOmega6(double omega6) {
        mOmega6 = omega6;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public int getKcalRelatedWithAmount() {
        return (int) (mAmount / 100 * mKcal);
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mName + " " + mAmount;
    }
}
