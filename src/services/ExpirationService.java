/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.mypharmacy.app.DrugDetails;
import com.mypharmacy.app.R;
import com.mypharmacy.dao.DrugDAOImpl;
import com.mypharmacy.models.DrugModel;

/**
 * @author: Haythem Khiri
 */
public class ExpirationService extends Service {

    private static final int MY_NOTIFICATION_ID = 1;

    private int mNotificationCount;
    private Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    private DrugModel drug;
    private DrugDAOImpl drugDAO;

    @Override
    public void onCreate() {
        mNotificationIntent = new Intent(getApplicationContext(), DrugDetails.class);
        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        drugDAO = new DrugDAOImpl(getApplicationContext());



        // Define the Notification's expanded message and Intent
        Notification.Builder notificationBuilder = new Notification.Builder(
                getApplicationContext())
                .setTicker(String.format(getString(R.string.expiration_notification_msg), drug.getName(), ++mNotificationCount))
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.expiration_notification_title))
                .setContentText(String.format(getString(R.string.expiration_notification_msg), drug.getName(), mNotificationCount))
                .setContentIntent(mContentIntent).setSound(soundURI)
                .setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
    }

}
