package pl.grudowska.feedme;

import android.app.IntentService;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.EmailManager;

public class DailySummaryEmailIntentService extends IntentService {

    private AddedProductDataSource mAddedProductDataSource;
    private ArchivedProductDataSource mArchivedDataSource;
    private String mDate;
    private int mTotalKcal;
    private String mContentMail;

    public DailySummaryEmailIntentService() {
        super("DailySummaryEmailIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        // String dataString = workIntent.getDataString();

        // Open DB's
        mAddedProductDataSource = new AddedProductDataSource(getApplicationContext(), false);
        mAddedProductDataSource.open();
        mArchivedDataSource = new ArchivedProductDataSource(getApplicationContext());
        mArchivedDataSource.open();

        // If recently added product list is empty do nothing
        if (mAddedProductDataSource.getAllAddedProducts().size() == 0) {
            // do nothing
        } else {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

            // To maintain application performance, save the e-mail text permanently in database
            // to re-send without the need to generate it again
            mContentMail = ArchivedListFormatterManager.createMailContent(getApplicationContext());

            mTotalKcal = CalculateSummary.getTotalKcal(getApplicationContext());

            // Log.d(getClass().getSimpleName(), "Preparing and sending email");

            sendDailySummaryEmail();
            archiveRecentlyAddedList();
            clearRecentlyAddedDB();
            sendClearAdapterBroadcast();
        }
        // Close DB's
        mAddedProductDataSource.close();
        mArchivedDataSource.close();
    }

    private void sendDailySummaryEmail() {
        new EmailManager(getApplicationContext(), mDate, mContentMail);
    }

    private void archiveRecentlyAddedList() {
        List<Product> products = DatabaseManager.getAddedProductsDB(getApplicationContext());
        for (int i = 0; i < products.size(); ++i) {
            mArchivedDataSource.createArchivedProduct(mDate, products.get(i));
        }
        mArchivedDataSource.createDailyRecap(mDate, mTotalKcal, mContentMail);
    }

    private void clearRecentlyAddedDB() {
        mAddedProductDataSource.clearAll();
    }

    private void sendClearAdapterBroadcast() {
        sendBroadcast(new Intent("RecentlyAddedFoodActivity.clearAdapter"));
    }
}
