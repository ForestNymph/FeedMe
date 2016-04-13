package pl.grudowska.feedme.databases;

public class ProductType {

    private long mId;
    private String mTypeName;
    private int mResImage;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String type) {
        mTypeName = type;
    }

    public int getResImage() {
        return mResImage;
    }

    public void setImage(int image) {
        mResImage = image;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return mTypeName + " " + mResImage;
    }
}
