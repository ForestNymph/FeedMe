package pl.grudowska.feedme.databases;

public class AllSentFood {

    private long mId;
    private String mDate;
    private String mContent;
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

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
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
        return mDate + " " + mContent + " " + mKcal;
    }
}
