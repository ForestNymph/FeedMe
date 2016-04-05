package pl.grudowska.feedme;

import android.app.IntentService;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.grudowska.feedme.database.AllSentFoodDataSource;
import pl.grudowska.feedme.database.RecentlyAdded;
import pl.grudowska.feedme.database.RecentlyAddedDataSource;
import pl.grudowska.feedme.util.EmailManager;

public class DailySummaryEmailIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DailySummaryEmailIntentService(String name) {
        super(name);
    }

    public DailySummaryEmailIntentService() {
        super("DailySummaryEmailIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        // Log.d(getClass().getSimpleName(), "Prepare and sending email");

        RecentlyAddedDataSource recentlyDataSource = new RecentlyAddedDataSource(getApplicationContext());
        recentlyDataSource.open();
        AllSentFoodDataSource sentDataSource = new AllSentFoodDataSource(getApplicationContext());
        sentDataSource.open();

        List<RecentlyAdded> mValues = recentlyDataSource.getAllAddedProducts();
        String content = "";

        for (int i = 0; i < mValues.size(); ++i) {
            content += mValues.get(i).getProduct();
            content += mValues.get(i).getAmount();
            content += "\n";
        }
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        new EmailManager(getApplicationContext(), date, content);

        //sentDataSource.createSentItem(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), content);

        recentlyDataSource.deleteAll();

        // close db
        recentlyDataSource.close();
        sentDataSource.close();



/*        String content = getContentAddedRecentlyDB();
        sendDailySummaryEmail(content);
        moveRecentlyAddedToSentDB(content);
        clearRecentlyAddedDB();*/
    }

    private String getContentAddedRecentlyDB() {

        RecentlyAddedDataSource recentlyDataSource = new RecentlyAddedDataSource(getApplicationContext());
        recentlyDataSource.open();
        AllSentFoodDataSource sentDataSource = new AllSentFoodDataSource(getApplicationContext());
        sentDataSource.open();

        List<RecentlyAdded> mValues = recentlyDataSource.getAllAddedProducts();
        String content = "";

        for (int i = 0; i < mValues.size(); ++i) {
            content += mValues.get(i).getProduct();
            content += mValues.get(i).getAmount();
            content += "\n";
        }

        recentlyDataSource.deleteAll();
        // close db
        recentlyDataSource.close();
        sentDataSource.close();

        return content;
    }

    private void sendDailySummaryEmail(String content) {
        new EmailManager(getApplicationContext(), content, content);
    }

    private void moveRecentlyAddedToSentDB(String content) {

    }

    private void clearRecentlyAddedDB() {

    }
}
