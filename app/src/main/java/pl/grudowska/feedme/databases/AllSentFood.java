package pl.grudowska.feedme.databases;

public class AllSentFood {

    private long mId;
    private String mDate;
    private String mContent;
    private String mKcal;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getKcal() {
        return mKcal;
    }

    public void setKcal(String kcal) {
        this.mKcal = kcal;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mDate + " " + mContent + " " + mKcal;
    }
}
