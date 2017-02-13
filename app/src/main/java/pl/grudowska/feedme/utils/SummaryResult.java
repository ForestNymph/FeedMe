package pl.grudowska.feedme.utils;

public class SummaryResult {

    final private String mUnit;
    final private String mResultType;
    final private String mSimpleName;
    final private int mAmount;

    public SummaryResult(String name, String simpleName, int amount, String unit) {
        mResultType = name;
        mSimpleName = simpleName;
        mAmount = amount;
        mUnit = unit;
    }

    public String getResultType() {
        return mResultType;
    }

    public String getSimpleName() {
        return mSimpleName;
    }

    public int getAmount() {
        return mAmount;
    }

    public String getUnit() {
        return mUnit;
    }
}
