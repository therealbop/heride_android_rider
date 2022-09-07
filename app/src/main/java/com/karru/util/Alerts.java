package com.karru.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Window;
import android.widget.TextView;

import com.karru.landing.profile.ProfileContract;
import com.karru.splash.first.LanguagesList;
import com.karru.splash.first.SplashContract;
import com.heride.rider.R;
import com.karru.utility.*;
import com.karru.utility.Utility;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;


/**
 * <h1>Alerts</h1>
 * <p>
 *     class to show different types of alerts
 * </p>
 * @since 23/7/15.
 */
public class Alerts
{
    private AppTypeface appTypeface;
    private AlertDialog alertDialog = null;

    public Alerts(Context context,AppTypeface appTypeface) {
        this.appTypeface=appTypeface;
    }

    /**
     * <h2>getProcessDialog</h2>
     * <p>
     * Getting the Process dialog
     * </p>
     */
    public ProgressDialog getProcessDialog(Activity mContext)
    {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setCancelable(true);
        dialog.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        return dialog;
    }


    /**
     * <h2>problemLoadingAlert</h2>
     * <p>
     *     method to show an alert for loading error
     * </p>
     * @param context context of the activity
     * @param message message to be desiplayed
     */
    public void problemLoadingAlert(final Context context,String message)
    {
        @SuppressLint("ResourceType") final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        dialog.show();

        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_ok =  dialog.findViewById(R.id.tv_alert_ok);

        tv_alert_title.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_ok.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_body.setTypeface(appTypeface.getPro_News());

        tv_alert_body.setText(message);
        tv_alert_ok.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * <h2>updateAppVersionAlert</h2>
     * <p>
     *     method to show an alert dialog that a newer version is available
     *     in the play store with message whether its mandatory update or not
     * </p>
     * @param mandatory: its mandatory update or not
     */
    public AlertDialog updateAppVersionAlert(final Context mContext, boolean mandatory,
                                             SplashContract.View view)
    {
        String msg = mContext.getString(R.string.update_non_mandatory);
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle(mContext.getString(R.string.update_available));
            Utility.printLog("ANimation"+ "UpdateAppVersionAlert mandatory: "+mandatory);
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getString(R.string.Update), (dialog, which) -> {
                        Constants.isToUpdateAlertVisible = false;
                        dialog.dismiss();
                        if (com.karru.utility.Utility.isNetworkAvailable(mContext))
                        {
                            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                mContext.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                            }
                        }
                    })
                    .setNegativeButton(mContext.getString(R.string.not_now), (dialog, id) ->
                    {
                        if(view!=null)
                            view.startLocationService();
                        Constants.isToUpdateAlertVisible = false;
                        dialog.dismiss();
                    });

            Utility.printLog("ANimation"+"UpdateAppVersionAlert mandatory: "+mandatory);
            if(mandatory){
                alertDialogBuilder.setNegativeButton("",null);
                alertDialogBuilder.setMessage(mContext.getString(R.string.update_msg));
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return alertDialog;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h2>userPromptWithOneButton</h2>
     * This method is used to show the alert with response
     * @param message message to be shown
     * @param context context of the activity
     */
    public Dialog userPromptWithOneButton(String message, Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_ok =  dialog.findViewById(R.id.tv_alert_ok);

        tv_alert_title.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_ok.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_body.setTypeface(appTypeface.getPro_News());

        tv_alert_body.setText(message);
        return dialog;
    }

    /**
     * <h2>userPromptWithTwoButtons</h2>
     * This method is used to show the alert with 2 buttons
     * @param context context of the activity
     */
    public Dialog userPromptWithTwoButtons(Activity context)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_two_buttons_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        dialog.setCancelable(false);

        TextView tv_alert_no =  dialog.findViewById(R.id.tv_alert_no);
        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_yes =  dialog.findViewById(R.id.tv_alert_yes);

        tv_alert_title.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_yes.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_no.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_body.setTypeface(appTypeface.getPro_News());

        tv_alert_no.setOnClickListener(v ->
                dialog.dismiss());
        return dialog;
    }

    /**
     *<h1>customAlertDialogLogout</h1>
     * <p>Logout dialog</p>
     */
    public void showLanguagesAlert(final Activity mActivity, ArrayList<LanguagesList> languagesList,
                                   SplashContract.Presenter presenter, ProfileContract.Presenter profilePresenter,
                                   int indexSelected)
    {
        ArrayList<String> languageListTemp = new ArrayList<>();
        for(int language = 0; language< languagesList.size(); language++)
        {
            languageListTemp.add(languagesList.get(language).getName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(mActivity.getString(R.string.select_lang));
        builder.setSingleChoiceItems(languageListTemp.toArray(new CharSequence[languagesList.size()]),
                indexSelected, (dialog, item) ->
                {
                    String langCode = languagesList.get(languagesList.indexOf(languagesList.get(item))).getCode();
                    String langName = languagesList.get(languagesList.indexOf(languagesList.get(item))).getName();
                    int direction = com.karru.util.Utility.changeLanguageConfig(langCode,mActivity);
                    if(presenter != null)
                        presenter.changeLanguage(langCode,langName,direction);
                    else
                        profilePresenter.changeLanguage(langCode,langName,direction);
                    alertDialog.dismiss();
                });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
