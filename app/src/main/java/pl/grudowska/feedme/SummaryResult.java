package pl.grudowska.feedme;

public class SummaryResult {

    private String mResultType;
    private int mAmount;
    private String mUnit;

    public SummaryResult(String name, int amount, String unit) {
        mResultType = name;
        mAmount = amount;
        mUnit = unit;
    }

    public String getResultType() {
        return mResultType;
    }

    public void setResultType(String name) {
        mResultType = name;
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

    public void setUnit(String unit) {
        mUnit = unit;
    }
}
