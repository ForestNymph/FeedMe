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
        mAddedProductDataSource = new AddedProductDataSource(getApplicationContext());
        mAddedProductDataSource.open();
        mArchivedDataSource = new ArchivedProductDataSource(getApplicationContext());
        mArchivedDataSource.open();

        // If recently added product list is empty do nothing
        if (mAddedProductDataSource.getAllAddedProducts().size() != 0) {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

            // To maintain application performance, save the e-mail text permanently in database
            // to re-send without the need to generate it again
            mContentMail = ArchivedListFormatterManager.createMailContent(getApplicationContext());

            mTotalKcal = CalculateSummary.getTotalKcal(getApplicationContext());

            sendDailySummaryEmail();
            archiveAddedList();
            clearAddedDB();
            sendClearAdapterBroadcast();
        }
        // Close DB's
        mAddedProductDataSource.close();
        mArchivedDataSource.close();
    }

    private void sendDailySummaryEmail() {
        new EmailManager(getApplicationContext(), mDate, mContentMail);
    }

    private void archiveAddedList() {
        List<Product> products = DatabaseManager.getAllAddedProductsDB(getApplicationContext());
        for (int i = 0; i < products.size(); ++i) {
            mArchivedDataSource.createArchivedProduct(mDate, products.get(i));
        }
        mArchivedDataSource.createDailyRecap(mDate, mTotalKcal, mContentMail);
    }

    private void clearAddedDB() {
        mAddedProductDataSource.clearAll();
    }

    private void sendClearAdapterBroadcast() {
        sendBroadcast(new Intent("AddedFoodActivity.clearAdapter"));
    }
}
