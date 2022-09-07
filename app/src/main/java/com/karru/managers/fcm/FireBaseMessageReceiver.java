package com.karru.managers.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChattingActivity;
import com.karru.landing.home.model.AdvertiseData;
import com.karru.landing.home.model.InvoiceDetailsModel;
import com.karru.managers.booking.RxInvoiceDetailsObserver;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.heride.rider.R;
import com.karru.landing.MainActivity;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.utility.Utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.karru.utility.Constants.ADVERTISE_DETAILS;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.IS_APP_BACKGROUND;
import static com.karru.utility.Constants.IS_CHAT_OPEN;

/**
 * <h1>FireBaseMessageReceiver</h1>
 * This class is for handling the messages those were came from FCM server.
 * @author 3embed
 * @since 6 Apr 2017.
 */
public class FireBaseMessageReceiver extends FirebaseMessagingService{
    PendingIntent intent = null;
    private static final String TAG = "FireBase_Message";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Utility.printLog(TAG+" onMessageReceived data "+remoteMessage.getData());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            //sendNotification(remoteMessage.getData());
            displayCustomNotification(remoteMessage.getData());
        }
    }

    private void displayCustomNotification(Map<String, String> pushData)
    {
        String dataObject = pushData.get("data");
        if(pushData.get("action") == null)
            return;
        int action = Integer.parseInt(pushData.get("action"));
        Utility.printLog(TAG+" dataObject "+dataObject);
        String  msg = String.valueOf(pushData.get("msg"));
        Utility.printLog( "sendNotification  msg " + msg + " bid to send "  );
        String title = String.valueOf(pushData.get("title"));

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);;

        if (notifManager == null)
        {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }

        Intent notificationIntent = null;
        BookingDetailsDataModel bookingDetailsDataModel;
        switch (action)
        {
            case 6://on the way
            case 7: // arrived
            case 9: //begin journey
                bookingDetailsDataModel = new Gson().fromJson(dataObject, BookingDetailsDataModel.class);
                if(!IS_APP_BACKGROUND)
                    RxLiveBookingDetailsObserver.getInstance().publishBookingDetails(bookingDetailsDataModel);
                notificationIntent = new Intent(this, LiveTrackingActivity.class);
                taskStackBuilder.addParentStack(LiveTrackingActivity.class);
                taskStackBuilder.addNextIntent(notificationIntent);
                notificationIntent.putExtra(BOOKING_ID,bookingDetailsDataModel.getBookingId());
                break;

            case 16:  //chat message
                if(!IS_CHAT_OPEN)
                {
                    bookingDetailsDataModel = new Gson().fromJson(dataObject, BookingDetailsDataModel.class);
                    notificationIntent = new Intent(this, ChattingActivity.class);
                    taskStackBuilder.addParentStack(ChattingActivity.class);
                    taskStackBuilder.addNextIntent(notificationIntent);
                    notificationIntent.putExtra("BID",bookingDetailsDataModel.getBid());
                    notificationIntent.putExtra("DRIVER_ID",bookingDetailsDataModel.getFromID());
                    notificationIntent.putExtra("DRIVER_NAME",bookingDetailsDataModel.getName());
                }
                break;

            //12 invoice for the booking
            case 12:
                JSONObject object;
                try
                {
                    object = new JSONObject(dataObject);
                    if(object.has("invoice"))
                    {
                        String invoiceObj = object.getString("invoice");
                        String bookingId = object.getString("bookingId");
                        Utility.printLog(TAG+" invoice status "+invoiceObj);
                        InvoiceDetailsModel invoiceDetailsModel = new Gson().fromJson(invoiceObj,
                                InvoiceDetailsModel.class);
                        invoiceDetailsModel.setCallAPi(false);
                        invoiceDetailsModel.setBookingId(bookingId);
                        if(!IS_APP_BACKGROUND)
                            RxInvoiceDetailsObserver.getInstance().publishInvoiceDetails(invoiceDetailsModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notificationIntent = new Intent(this, MainActivity.class);
                break;

            case 111:
            case 112:
                AdvertiseData advertiseDataModel = new Gson().fromJson(dataObject, AdvertiseData.class);
                notificationIntent = new Intent(this, MainActivity.class);
                taskStackBuilder.addParentStack(MainActivity.class);
                taskStackBuilder.addNextIntent(notificationIntent);
                Intent intent = new Intent(this, com.karru.ads.AdvertiseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ADVERTISE_DETAILS,advertiseDataModel);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        PendingIntent pendingIntent;

        if(notificationIntent == null)
            notificationIntent = new Intent(this, MainActivity.class);

        if(notificationIntent!=null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationCompat.Builder builder;

                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                if (mChannel == null) {
                    mChannel = new NotificationChannel
                            ("0", title, importance);
                    mChannel.setDescription(msg);
                    mChannel.enableVibration(true);
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(this, mChannel.getId());

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try
                {
                    pendingIntent =
                            taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                catch (IllegalStateException e)
                {
                    pendingIntent = PendingIntent.getActivity(this, 1251, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                }

                builder.setContentTitle(title)
                        .setSmallIcon(getNotificationIcon()) // required
                        .setContentText(msg)  // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setChannelId(mChannel.getId())
                        .setLargeIcon(BitmapFactory.decodeResource
                                (getResources(), R.drawable.ic_launcher))
                        .setBadgeIconType(R.drawable.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSound(RingtoneManager.getDefaultUri
                                (RingtoneManager.TYPE_NOTIFICATION));
                Notification notification = builder.build();
                notifManager.notify(0, notification);
            }
            else
            {
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try
                {
                    pendingIntent =
                            taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                catch (IllegalStateException e)
                {
                    pendingIntent = PendingIntent.getActivity(this, 1251, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                }

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"0")
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setSmallIcon(getNotificationIcon())
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(msg));

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1251, notificationBuilder.build());
            }
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_logo : R.drawable.notification_logo;
    }

}
