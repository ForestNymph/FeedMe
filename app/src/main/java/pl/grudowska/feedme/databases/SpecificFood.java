package pl.grudowska.feedme.databases;

public class SpecificFood {

    private long mId;
    private String mType;
    private String mName;
    private int mDef1;
    private int mDef2;
    private int mDef3;
    private double mKcal;
    private double mProtein;
    private double mCarbohydrates;
    private double mRoughage;
    private double mFatsUnsaturated;
    private double mFatsSaturated;
    private double mFatsMonounsaturated;
    private double mOmega3;
    private double mOmega6;

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

    public double getRoughage() {
        return mRoughage;
    }

    public void setRoughage(double roughage) {
        mRoughage = roughage;
    }

    public double getFatsUnsaturated() {
        return mFatsUnsaturated;
    }

    public void setFatsUnsaturated(double fatsUnsaturated) {
        mFatsUnsaturated = fatsUnsaturated;
    }

    public double getFatsSaturated() {
        return mFatsSaturated;
    }

    public void setFatsSaturated(double fatsSaturated) {
        this.mFatsSaturated = fatsSaturated;
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

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mType + " " + mName + " " + mDef1 + " " + mDef2 + " " + mDef3 + " "
                + mKcal + " " + mProtein + " " + mCarbohydrates + " " + mRoughage + " "
                + mFatsMonounsaturated + " " + mFatsSaturated + " " + mFatsUnsaturated + " "
                + mOmega3 + " " + mOmega6;
    }
}

