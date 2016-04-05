package pl.grudowska.feedme.database;

public class AllSentFood {
    private long mId;
    private String mDate;
    private String mContent;

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

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mDate + " " + mContent;
    }
}
