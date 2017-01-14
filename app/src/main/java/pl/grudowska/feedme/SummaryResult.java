package pl.grudowska.feedme;

public class SummaryResult {

    final private String mUnit;
    private String mResultType;
    private String mSimpleName;
    private int mAmount;

    public SummaryResult(String name, String simpleName, int amount, String unit) {
        mResultType = name;
        mSimpleName = simpleName;
        mAmount = amount;
        mUnit = unit;
    }

    public String getResultType() {
        return mResultType;
    }

    String getSimpleName() {
        return mSimpleName;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    public String getUnit() {
        return mUnit;
    }
}
