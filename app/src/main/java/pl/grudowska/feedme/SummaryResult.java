package pl.grudowska.feedme;

public class SummaryResult {

    private String mResultType;
    private String mSimpleName;
    private int mAmount;
    private String mUnit;

    public SummaryResult(String name, String simpleName, int amount, String unit) {
        mResultType = name;
        mSimpleName = simpleName;
        mAmount = amount;
        mUnit = unit;
    }

    public String getResultType() {
        return mResultType;
    }

    public void setResultType(String name) {
        mResultType = name;
    }

    public String getSimpleName() {
        return mSimpleName;
    }

    public void setSimpleName(String name) {
        mSimpleName = name;
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
