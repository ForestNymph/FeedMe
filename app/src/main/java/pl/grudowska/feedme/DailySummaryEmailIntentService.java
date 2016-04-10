package pl.grudowska.feedme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.grudowska.feedme.database.AllSentFoodDataSource;
import pl.grudowska.feedme.database.RecentlyAdded;
import pl.grudowska.feedme.database.RecentlyAddedDataSource;
import pl.grudowska.feedme.utils.EmailManager;

public class DailySummaryEmailIntentService extends IntentService {

    private RecentlyAddedDataSource mRecentlyDataSource;
    private AllSentFoodDataSource mSentDataSource;

    public DailySummaryEmailIntentService(String name) {
        super(name);
    }

    public DailySummaryEmailIntentService() {
        super("DailySummaryEmailIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        // String dataString = workIntent.getDataString();

        Log.d(getClass().getSimpleName(), "Prepare and sending email");

        // Open DB's
        mRecentlyDataSource = new RecentlyAddedDataSource(getApplicationContext());
        mRecentlyDataSource.open();
        mSentDataSource = new AllSentFoodDataSource(getApplicationContext());
        mSentDataSource.open();

        String content = getContentAddedRecentlyDB();
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        // If recently added product list is empty do nothing
        if (mRecentlyDataSource.getAllAddedProducts().size() == 0) {
            // do nothing
        } else {
            sendDailySummaryEmail(date, content);
            moveRecentlyAddedToSentDB(date, content);
            clearRecentlyAddedDB();
        }
        // Close DB's
        mRecentlyDataSource.close();
        mSentDataSource.close();
    }

    private String getContentAddedRecentlyDB() {

        List<RecentlyAdded> mValues = mRecentlyDataSource.getAllAddedProducts();
        String content = "";

        for (int i = 0; i < mValues.size(); ++i) {
            content += mValues.get(i).getProduct();
            content += mValues.get(i).getAmount();
            content += "\n";
        }
        return content;
    }

    private void sendDailySummaryEmail(String date, String content) {
        new EmailManager(getApplicationContext(), date, content);
    }

    private void moveRecentlyAddedToSentDB(String date, String content) {
        mSentDataSource.createSentItem(date, content);
    }

    private void clearRecentlyAddedDB() {
        mRecentlyDataSource.deleteAllItems();
    }
}
