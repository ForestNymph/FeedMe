package pl.grudowska.feedme.util;

import android.content.Context;
import android.widget.Toast;

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
                        Toast.makeText(context, "Message was sent successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(context, "Message was not sent", Toast.LENGTH_LONG).show();
                    }
                })
                .send();
    }
}
