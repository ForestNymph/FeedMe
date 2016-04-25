package pl.grudowska.feedme.databases;

public class ArchivedProduct {

    public long id;
    public String date;
    public String contentMail;
    public String contentName;
    public String contentAmount;
    public int kcal;

    @Override
    public String toString() {
        return date + " " + contentName + " " + contentAmount + " " + kcal;
    }
}
