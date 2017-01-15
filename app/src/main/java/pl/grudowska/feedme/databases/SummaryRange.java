package pl.grudowska.feedme.databases;

public class SummaryRange {
    public long id;
    public String typeName;
    public int maxRange;
    public int minRange;

    @Override
    public String toString() {
        return id + " type: " + typeName + " max: " + maxRange + " min: " + minRange;
    }
}
