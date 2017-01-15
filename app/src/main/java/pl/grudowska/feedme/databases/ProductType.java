package pl.grudowska.feedme.databases;

public class ProductType {

    public long id;
    public String typeName;
    public int resImage;

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return id + " " + typeName + " " + resImage;
    }
}
