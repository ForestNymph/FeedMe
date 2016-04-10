package pl.grudowska.feedme.utils;

import android.content.Context;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

public class EmailManager {

    public EmailManager(final Context context, String date, String content) {

        String mailFrom = SharedPreferencesManager.loadDataString(context, "mailFrom", "test@test.com");
        String password = SharedPreferencesManager.loadDataString(context, "password", "321");
        String mailTo = SharedPreferencesManager.loadDataString(context, "mailTo", "test@test.com");

        BackgroundMail.newBuilder(context)
                .withUsername(mailFrom)
                .withPassword(password)
                .withMailto(mailTo)
                .withSubject("[FeedMe] " + date)
                .withBody(content)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                    }
                })
                .send();
    }
}
