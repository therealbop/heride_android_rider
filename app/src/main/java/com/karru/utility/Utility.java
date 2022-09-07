
package com.karru.utility;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import com.karru.landing.MainActivity;
import com.heride.rider.R;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <h2>Utility</h2>
 * <p>
 *     class to provide the basic common functionalities and other common methods
 * </p>
 *@since  23/5/15.
 */
public class Utility
{

    private static final String TAG ="Utility" ;
    public static void printLog(String msg)
    {
        Log.i("DayRunner", msg);
    }

    /**
     * <h2>isNetworkAvailable</h2>
     * <P>
     *      This method is used for checking internet connection
     * </P>
     * @param context current context.
     * @return boolean value.
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity;
        boolean isNetworkAvail = false;
        try {
            connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info)
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkAvail;
    }

    /**
     * <h2>copyStream</h2>
     * <P>
     *     method to copy the input stream to output stream
     * </P>
     * @param input: contains the image input byte streams
     * @param output: image output streams
     * @throws IOException: exception
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    //====================================================================

    //************************************************************************/

    /**
     * <h2>date</h2>
     * <p>
     * GETTING CURRENT DATE in am/pm
     * </p>
     */
    public static String date() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        return formater.format(date);
    }

    /**
     * <h2>finishAndRestartMainActivity</h2>
     * This method is used to refresh the current activity and start main activity
     * @param mContext Context of the activity
     */
    public static void finishAndRestartMainActivity(AppCompatActivity mContext)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        mContext.finish();
        mContext.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /*
     * <h2>getAppVersion</h2>
     * <p>
     * method to get Application's version code from the {@code PackageManager}.
     *  </p>
     *  @return Application's version code
     */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    //*****************************************************************/

    /**
     * <h2>changeStatusBarColor</h2>
     * <p>
     * This method is used for changing the status bar color for above than Lollipop device
     * </p>
     *
     * @param context , calling activity context
     * @param window  contains the Window.
     */
    public static void changeStatusBarColor(Context context, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    /**
     * <h2>returnDisplayWidth</h2>
     * this method will return the display width
     * @param context takes the context
     * @return returns the width
     */
    public static int returnDisplayWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        display.getSize(size);
        //}
        return size.x;
    }
    //========================== By PS ====================================

    /**
     *<h2>getFormattedPrice</h2>
     * <P>
     *     method to get price into formatted form upto 2 decimal points
     * </P>
     * @param tempPrice: retrieved price need to be formatted
     * @return String: returns the formatted price
     */
    public static String getFormattedPrice(String tempPrice) {
        String price;
        Double dPrice = 0.00;
        if (tempPrice != null && !"".equals(tempPrice) && !"0".equals(tempPrice) && !"00".equals(tempPrice)
                && !"0.00".equals(tempPrice) && !"0.0".equals(tempPrice) && !".00".equals(tempPrice)) {
            dPrice = Double.parseDouble(tempPrice);
        }

        NumberFormat nf_out = NumberFormat.getInstance(Locale.US);
        nf_out.setMaximumFractionDigits(2);
        nf_out.setMinimumFractionDigits(2);
        nf_out.setGroupingUsed(false);
        price = nf_out.format(dPrice);
        return price;
    }
}