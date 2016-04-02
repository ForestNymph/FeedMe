package pl.grudowska.feedme.util;

import android.content.Context;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailManager {

    public EmailManager(final Context context) {

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        String mailFrom = SharedPreferencesManager.loadDataString(context, "mailFrom", "test@test.com");
        String password = SharedPreferencesManager.loadDataString(context, "password", "321");
        String mailTo = SharedPreferencesManager.loadDataString(context, "mailTo", "test@test.com");

        BackgroundMail.newBuilder(context)
                .withUsername(mailFrom)
                .withPassword(password)
                .withMailto(mailTo)
                .withSubject("[FeedMe] " + date)
                .withBody("this is the body\n" +
                        "and more body\n" +
                        "and more")
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
