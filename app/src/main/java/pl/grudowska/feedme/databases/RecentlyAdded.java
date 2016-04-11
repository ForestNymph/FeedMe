package pl.grudowska.feedme.databases;

public class RecentlyAdded {
    private long mId;
    private String mProduct;
    private int mAmount;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        this.mProduct = product;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        this.mAmount = amount;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mProduct + " " + mAmount;
    }
}
