package com.karru;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.widget.TextView;

import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.BuildConfig;
import com.heride.rider.R;

public class AppRater
{
    private static final String TAG = "AppRater";
    private static String appName;
    private final static int DAYS_UNTIL_PROMPT = 2;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches
    private static AppTypeface appTypeface;

    public static void app_launched(Context mContext, AppTypeface typeface)
    {
        appTypeface = typeface;
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();
        appName = mContext.getString(R.string.app_name);
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0)
        {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        Utility.printLog(TAG+" RATE launches count  "+launch_count);
        Utility.printLog(TAG+" RATE launches current time  "+System.currentTimeMillis());
        Utility.printLog(TAG+" RATE launches date of launch  "+date_firstLaunch);
        Utility.printLog(TAG+" RATE launches date of launch 1  "+date_firstLaunch+
                (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000));
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT)
        {
            editor.putLong("launch_count", 0);
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000))
                showRateDialog(mContext, editor);
        }
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    private static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rating);

        editor.putLong("date_firstlaunch", 0);

        TextView dialog_rating_title = dialog.findViewById(R.id.dialog_rating_title);
        dialog_rating_title.setText(mContext.getString(R.string.enjoy)+" " + appName + mContext.getString(R.string.rate_it));
        dialog_rating_title.setTypeface(appTypeface.getPro_narMedium());

        TextView dialog_rating_button_positive = dialog.findViewById(R.id.dialog_rating_button_positive);
        dialog_rating_button_positive.setTypeface(appTypeface.getPro_News());
        dialog_rating_button_positive.setOnClickListener(v ->
        {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
            dialog.dismiss();
        });

        TextView dialog_rating_button_negative = dialog.findViewById(R.id.dialog_rating_button_negative);
        dialog_rating_button_negative.setTypeface(appTypeface.getPro_News());
        dialog_rating_button_negative.setOnClickListener(v ->
        {
            //for never show
            dialog.dismiss();
        });
        dialog.show();
    }
}