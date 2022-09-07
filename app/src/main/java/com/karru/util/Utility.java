package com.karru.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.utility.Scaler;
import com.heride.rider.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

/**
 * <h1>Utility</h1>
 * This class is created to add the utilities
 * @author 3Embed
 * @since  on 02-01-2018.
 */

public class Utility
{
    private static final String TAG = "Utility";

    /**
     * <h2>getLocalIpAddress</h2>
     * This method is used to get the IP address of the mobile
     * @return returns IP address of the mobile
     */
    public String getLocalIpAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                        return inetAddress.getHostAddress();
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    /**
     * <h2>currencyAdjustment</h2>
     * used to adjust the amount with currency symbol
     * @param currencyAbbr curency abbreviation
     * @param currencySymbol currency symbol
     * @param amount amount
     * @return returns amount with symbol
     */
    public String currencyAdjustment(int currencyAbbr,String currencySymbol,String amount)
    {
        switch (currencyAbbr)
        {
            case 1:
                amount = currencySymbol+" "+amount;
                return amount;

            case 2:
                amount = amount+" "+currencySymbol;
                return amount;
        }
        return amount;
    }

    /**
     * <h2>getDeviceId</h2>
     * <p>
     * method to get device id
     *</p>
     * @param context: calling activity reference
     * @return String: device id
     */
    public String getDeviceId(Context context)
    {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        com.karru.utility.Utility.printLog(TAG+" device ID "+androidId);
        return androidId;
    }

    /**
     * <h2>moveCarMarker</h2>
     * This method is used to move the car icon smoothly on the map
     * @param driverLocation current location of driver
     * @param marker driver marker icon
     */
    public void moveCarMarker(LatLng driverLocation,Marker marker)
    {
        LatLng prevLatLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
        double bearing = bearingBetweenLocations(prevLatLng, driverLocation);
        final long duration = 1000;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * driverLocation.longitude + (1 - t)
                        * prevLatLng.longitude;
                double lat = t * driverLocation.latitude + (1 - t)
                        * prevLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setAnchor(0.5f, 0.5f);
                marker.setRotation((float) bearing);
                marker.setFlat(true);
                if (t < 1.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        /*if (marker != null) {
            Log.d(TAG, "marker not null");
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = driverLocation;

            // final float startRotation = marker.getRotation();
            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(10000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);

                        marker.setRotation(getBearing(startPosition, driverLocation));
                    } catch (Exception ex) {
                        //I don't care atm..                    }
                    }
                }
            });
            valueAnimator.start();
        }*/
    }

    private float getBearing(LatLng begin, LatLng end)
    {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    private interface LatLngInterpolator
    {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    /**
     * <h2>bearingBetweenLocations</h2>
     * @param prevLatLng previous lat long
     * @param currLatLng current lat long
     * @return returns bearing
     */
    public double bearingBetweenLocations(LatLng prevLatLng, LatLng currLatLng) {

        double PI = 3.14159;
        double lat1 = prevLatLng.latitude * PI / 180;
        double long1 = prevLatLng.longitude * PI / 180;
        double lat2 = currLatLng.latitude * PI / 180;
        double long2 = currLatLng.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    /**
     * <h2>cropImageCircle</h2>
     * used to crop the image in circle
     * @param mContext context
     * @param path path of the image
     * @return returns bitmap
     */
    public Bitmap cropImageCircle(Context mContext,String path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = Bitmap.createScaledBitmap(bitmap, mContext.getResources().getDrawable(R.drawable.signup_profile_default_image).getMinimumWidth(),
                mContext.getResources().getDrawable(R.drawable.signup_profile_default_image).getMinimumHeight(), true);
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
        return circleBitmap;
    }

    /**
     * <h2>changeLanguageConfig</h2>
     * used to set the language configuration
     * @param code language code
     */
    public static int changeLanguageConfig(String code,Context context )
    {
       /* Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            configuration.setLayoutDirection(new Locale(code));
            com.karru.utility.Utility.printLog(TAG+" language direction "+configuration.getLayoutDirection());
        }
        Locale locale = new Locale(code);
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());
       */

        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        context.getApplicationContext().getResources().updateConfiguration(config,
                context.getApplicationContext().getResources().getDisplayMetrics());



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return config.getLayoutDirection();
        }

      /*  Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            config.setLayoutDirection(new Locale(code));
            com.karru.utility.Utility.printLog(TAG+" language direction "+config.getLayoutDirection());
        }
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }*/
       /* Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        Locale newLocale = new Locale(code);
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(newLocale);

            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);

            context = context.createConfigurationContext(configuration);

        } else if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return configuration.getLayoutDirection();
        }*/

        return 0;
    }

    public static String getFormattedDate(Date date,PreferenceHelperDataSource preferenceHelperDataSource){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        //cal.setTimeZone(preferenceHelperDataSource.getCurrentTimeStamp());//getTimeZone(AppConstants.CURRENT_ZONE_LAT, AppConstants.CURRENT_ZONE_LONGI));
        //2nd of march 2015
        int day=cal.get(Calendar.DATE);

        if(!((day>10) && (day<19)))
            switch (day % 10) {
                case 1:
                    return simpleDateFormater("EEE d'st' MMM yyyy',' hh:mm a",date);
                case 2:

                    return simpleDateFormater("EEE d'nd' MMM yyyy',' hh:mm a",date);
                case 3:
                    return simpleDateFormater("EEE d'rd' MMM yyyy',' hh:mm a",date);
                default:
                    return simpleDateFormater("EEE d'th' MMM yyyy',' hh:mm a",date);
            }
        return simpleDateFormater("EEE d'th' MMM yyyy',' hh:mm a",date);
    }

    private static String simpleDateFormater(String format, Date date)
    {
        SimpleDateFormat sfd = new SimpleDateFormat(format, Locale.getDefault());
        //sfd.setTimeZone(getTimeZone(AppConstants.CURRENT_ZONE_LAT, AppConstants.CURRENT_ZONE_LONGI));
        return sfd.format(date);
    }

    /*get the color*/

    public static int getColor(Context mContext, int id) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.getColor(id);
        } else {
            return ContextCompat.getColor(mContext, id);
        }
    }

    public static void hideKeyboard(Activity mcontext) {
        try{
            InputMethodManager inputManager = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mcontext.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }catch (NullPointerException e)
        {

        }

    }



    public Bitmap punchAHoleInABitmap(Context context, Bitmap foreground) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float radius = (float)(getScreenSize(context).x *.5);
        float x = (float) ((getScreenSize(context).x) /*+ (radius * .5)*/);
        float y = (float)  ((getScreenSize(context).y)/* + (radius * .5)*/);
        canvas.drawCircle(x, y, radius, paint);
        return bitmap;
    }

    private Point getScreenSize(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public Bitmap combineTwoBitmaps(Context context, Bitmap background, Bitmap foreground)
    {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(),
                background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        double[] size = Scaler.getScalingFactor(context);
        canvas.drawBitmap(foreground, (50) * (int)size[0], (50) *(int)size[0], paint);
        return combinedBitmap;
    }

    public static void RtlConversion(Activity activity, String lang)
    {

        if(lang!=null && lang.equals("ar"))
        {
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

        }
        else if(lang!=null && lang.equals("fa"))
        {
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

        }
        else if(lang!=null && lang.equals("ps"))
        {
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

        }
        else
        {
            if(lang==null)
                lang="en";
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }

    public static void setLocale(String lang, Context context) {
        try {
            Locale myLocale = new Locale(lang);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
        }
    }

}
