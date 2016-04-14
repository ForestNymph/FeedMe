package pl.grudowska.feedme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AllSentFoodDataSource;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.EmailManager;
import pl.grudowska.feedme.utils.MailFormatterManager;

public class DailySummaryEmailIntentService extends IntentService {

    private ProductDataSource mAddedProductDataSource;
    private AllSentFoodDataSource mSentDataSource;

    public DailySummaryEmailIntentService() {
        super("DailySummaryEmailIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        // String dataString = workIntent.getDataString();

        Log.d(getClass().getSimpleName(), "Preparing and sending email");

        // Open DB's
        mAddedProductDataSource = new ProductDataSource(getApplicationContext());
        mAddedProductDataSource.open();
        mSentDataSource = new AllSentFoodDataSource(getApplicationContext());
        mSentDataSource.open();

        String content = MailFormatterManager.getAllAddedProductsToMail(getApplicationContext());
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        // If recently added product list is empty do nothing
        if (mAddedProductDataSource.getAllAddedProducts().size() == 0) {
            // do nothing
        } else {
            sendDailySummaryEmail(date, content);
            moveRecentlyAddedToSentDB(date, content, CalculateSummary.getTotalKcal(getApplicationContext()));
            clearRecentlyAddedDB();
        }
        // Close DB's
        mAddedProductDataSource.close();
        mSentDataSource.close();
    }

    private void sendDailySummaryEmail(String date, String content) {
        new EmailManager(getApplicationContext(), date, content);
    }

    private void moveRecentlyAddedToSentDB(String date, String content, int kcal) {
        mSentDataSource.createSentItem(date, content, kcal);
    }

    private void clearRecentlyAddedDB() {
        mAddedProductDataSource.deleteAllAddedProducts();
    }
}
