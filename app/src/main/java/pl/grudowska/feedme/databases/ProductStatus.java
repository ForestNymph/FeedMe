package pl.grudowska.feedme.databases;

public class ProductStatus {

    public long id;
    public String date;
    public String name;
    public int status;

    public enum Status {
        NEW,
        UPDATE,
        DELETE,
        NONE
    }
}
