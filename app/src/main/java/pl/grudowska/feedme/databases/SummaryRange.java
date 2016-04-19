package pl.grudowska.feedme.databases;

public class SummaryRange {
    private long mId;
    private String mTypeName;
    private int mMaxRange;
    private int mMinRange;

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        this.mTypeName = typeName;
    }

    public int getMaxRange() {
        return mMaxRange;
    }

    public void setMaxRange(int maxRange) {
        this.mMaxRange = maxRange;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getMinRange() {
        return mMinRange;
    }

    public void setMinRange(int minRange) {
        this.mMinRange = minRange;
    }
}
