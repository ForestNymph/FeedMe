package pl.grudowska.feedme.databases;

public class ArchivedProduct {

    private long mId;
    private String mDate;
    private String mFullContent;
    private String mContentName;
    private String mContentAmount;
    private int mKcal;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getContentName() {
        return mContentName;
    }

    public void setContentName(String name) {
        mContentName = name;
    }

    public String getContentAmount() {
        return mContentAmount;
    }

    public void setContentAmount(String amount) {
        mContentAmount = amount;
    }

    public String getContentFull() {
        return mFullContent;
    }

    public void setContentFull(String content) {
        mFullContent = content;
    }

    public int getKcal() {
        return mKcal;
    }

    public void setKcal(int kcal) {
        mKcal = kcal;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mDate + " " + mContentName + " " + mContentAmount + " " + mKcal;
    }
}
