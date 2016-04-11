package pl.grudowska.feedme.databases;

public class MainType {

    private long mId;
    private String mTypeName;
    private int mResImage;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String product) {
        this.mTypeName = product;
    }

    public int getResImage() {
        return mResImage;
    }

    public void setImage(int amount) {
        this.mResImage = amount;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mTypeName + " " + mResImage;
    }
}
