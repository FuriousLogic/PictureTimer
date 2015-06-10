package uk.co.furiouslogic.picturetimer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private int NOTIFY_ID = 1;
    private int mNotifyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnNotify_Click(View view) {
        String title = "Booyah";
        String text = "Just bein' epic, m'Blood!";

        Intent intent = new Intent(this, SimpleTextActivity.class);
        intent.setAction("Notify");
        intent.putExtra(SimpleTextActivity.TITLE_EXTRA, title);
        intent.putExtra(SimpleTextActivity.BODY_TEXT_EXTRA, text);

        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    private NotificationCompat.Builder initBasicBuilder(String title, String text, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_notify_kitty_round)
                .setContentTitle(title)
                .setContentText(text);
        builder.setAutoCancel(true);

        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        }

        return builder;
    }

    public void btnNotifyPersonal_Click(View view) {
        String title = "Booyah";
        String text = "Boom shak-a-lak!";

        Intent intent = new Intent(this, SimpleTextActivity.class);
        intent.setAction("Notify");
        intent.putExtra(SimpleTextActivity.TITLE_EXTRA, title);
        intent.putExtra(SimpleTextActivity.BODY_TEXT_EXTRA, text);

        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);

        //Personalise
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_fwankwin));

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void btnNotifyMulti_Click(View view) {
        String title = "Notify";
        String text = "You have multiple notifications!";
        String detailText1 = "Just bein' epic, m'Blood!";
        String detailText2 = "Boom shak-a-lak!";
        mNotifyCount++;

        ArrayList<String> textValues = new ArrayList<>();
        textValues.add(detailText1);
        textValues.add(detailText2);

        Intent intent = new Intent(this, SimpleListActivity.class);
        intent.setAction("Notify");
        intent.putExtra(SimpleListActivity.TITLE_EXTRA, title);
        intent.putExtra(SimpleListActivity.TEXT_VALUES_EXTRA, textValues);

        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);

        //Personalise
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_notify_kitty_multi))
                .setNumber(mNotifyCount)
                .setTicker("You have another value");

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void btnNotifyBigText_Click(View view) {
        String title = "What-Ho";
        String text = "How do you do";
        String bigTitle = "This is the Big Title";
        String bigSummary = "This is the Big Summary";
        String notificationText = "Hello.  I am here to show you a load of text in a notification!";
        mNotifyCount++;

        Intent intent = new Intent(this, SimpleTextActivity.class);
        intent.setAction("Notify");
        intent.putExtra(SimpleTextActivity.TITLE_EXTRA, title);
        intent.putExtra(SimpleTextActivity.BODY_TEXT_EXTRA, text);

        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);

        //Add the Big Tet Style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(bigTitle)
                .setSummaryText(bigSummary)
                .bigText(notificationText);
        builder.setStyle(bigTextStyle);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void btnNotifyBigPicture_Click(View view) {
        String title = "Meow";
        String text = "Never mind .. just being a cat";
        String bigTitle = "Growing Up";
        String bigSummary = "This is me in my box now";

        // Create the Intent to display the picture in an Activity
        Intent intent = new Intent(this, SimplePictureActivity.class);
        intent.setAction("NotifyBigPicture");
        intent.putExtra(SimplePictureActivity.TITLE_EXTRA, bigTitle);
        intent.putExtra(SimplePictureActivity.IMAGE_RESOURCE_ID_EXTRA, R.drawable.fwankwin_grows_into_his_box);

        // Create Builder with basic notification info
        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);

        // Add the Big Picture Style
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(bigTitle)
                .setSummaryText(bigSummary)
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.fwankwin_grows_into_his_box));

        builder.setStyle(bigPictureStyle);

        // Construct the Notification
        Notification notification = builder.build();

        // Display the Notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

}
