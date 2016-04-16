package pl.grudowska.feedme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.EmailManager;

public class DailySummaryEmailIntentService extends IntentService {

    private ProductDataSource mAddedProductDataSource;
    private ArchivedProductDataSource mSentDataSource;

    public DailySummaryEmailIntentService() {
        super("DailySummaryEmailIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        // String dataString = workIntent.getDataString();

        // Open DB's
        mAddedProductDataSource = new ProductDataSource(getApplicationContext());
        mAddedProductDataSource.open();
        mSentDataSource = new ArchivedProductDataSource(getApplicationContext());
        mSentDataSource.open();

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String contentMail = ArchivedListFormatterManager.createMailContent(getApplicationContext());
        String contentName = ArchivedListFormatterManager.createContentNames(getApplicationContext());
        String contentAmount = ArchivedListFormatterManager.createContentAmounts(getApplicationContext());
        int totalKcal = CalculateSummary.getTotalKcal(getApplicationContext());

        // If recently added product list is empty do nothing
        if (mAddedProductDataSource.getAllAddedProducts().size() == 0) {
            // do nothing
        } else {
            Log.d(getClass().getSimpleName(), "Preparing and sending email");
            sendDailySummaryEmail(date, contentMail);
            archiveRecentlyAddedList(date, contentMail, contentName, contentAmount, totalKcal);
            clearRecentlyAddedDB();
        }
        // Close DB's
        mAddedProductDataSource.close();
        mSentDataSource.close();
    }

    private void sendDailySummaryEmail(String date, String content) {
        new EmailManager(getApplicationContext(), date, content);
    }

    private void archiveRecentlyAddedList(String date, String contentMail, String contentName, String contentAmount, int totalKcal) {
        mSentDataSource.createArchivedItem(date, contentMail, contentName, contentAmount, totalKcal);
    }

    private void clearRecentlyAddedDB() {
        mAddedProductDataSource.deleteAllAddedProducts();
    }
}
