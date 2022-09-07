package com.karru.util;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.utility.Utility;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <h1>DateFormatter</h1>
 * This class is used to format the date
 * @author 3Embed
 * @since on 03-02-2018.
 */
public class DateFormatter
{

    private static final String TAG = "DateFormatter";

    /**
     +     * <h2>dateFormatter</h2>
     +     * This method is used to convert the date format to MMM dd, hh:mm a
     +     * <p>
     +     *     Convert the date from yyyy-MM-dd HH:mm to MMM dd, hh:mm a
     +     * </p>
     +     * @param dateToBeConverted input the date to be converted
     +     * @return returns the Converted date
     +     */
    public static String dateFormatter(String dateToBeConverted)
    {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat fort=new SimpleDateFormat("MMM dd, hh:mm a",Locale.US);
        Date date;
        try {
            if (dateToBeConverted != null)
            {
                date = formatter.parse(dateToBeConverted);
                return fort.format(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h2>getCurrTime</h2>
     * This method is used to get the current time
     * @return returns current time
     */
    public String getCurrTime(PreferenceHelperDataSource preferenceHelperDataSource)
    {
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()),
                Double.parseDouble(preferenceHelperDataSource.getCurrLongitude()));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        long currentTime = (long) preferenceHelperDataSource.getCurrentTimeStamp();
        Utility.printLog(TAG+" current tiem zone "+timeZone.getRawOffset());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        df.setTimeZone(timeZone);
        Date date =new Date();
        date.setTime(currentTime*1000L);
        Utility.printLog(TAG+" current tiem new "+date.getTime());
        return df.format(date.getTime());
    }

    /**
     * <h2>getDateInSpecificFormat</h2>
     * This method is used to convert the date format to 4th May,1993
     * @param dateInString date in string format
     * @return returns the formatted date in string
     */
    public String getDateInSpecificFormat(String dateInString) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            cal.setTime(sdf.parse(dateInString));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayNumberSuffix = getDayNumberSuffix(cal.get(Calendar.DAY_OF_MONTH));
        DateFormat dateFormat = new SimpleDateFormat("dd'"+ dayNumberSuffix +"' MMM yyyy",Locale.US);
        return dateFormat.format(cal.getTime());
    }

    /**
     * <h2>getDateInSpecificFormatWithTime</h2>
     * This method is used to convert the date format to 4th May 1993, 5:06 am
     * @param dateInString date in string format
     * @return returns the formatted date in string
     */
    public String getDateInSpecificFormatWithTime(String dateInString,TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            cal.setTime(sdf.parse(dateInString));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayNumberSuffix = getDayNumberSuffix(cal.get(Calendar.DAY_OF_MONTH));
        DateFormat dateFormat = new SimpleDateFormat("dd'"+ dayNumberSuffix +"' MMM yyyy, hh:mm a",Locale.US);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(cal.getTime());
    }

    /**
     * <h2>getDateCurrentTimeZone</h2>
     * used to get the date from time stamp
     * @param dateInString time stamp
     * @return returns date
     */
    public  String getDateCurrentTimeZone(String dateInString)
    {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(Long.parseLong(dateInString) * 1000L);
        String dayNumberSuffix = getDayNumberSuffix(cal.get(Calendar.DAY_OF_MONTH));
        return  android.text.format.DateFormat.format("dd'"+ dayNumberSuffix +"' MMM yyyy, hh:mm a", cal).toString();
    }

    /**
     * <h2>getDateWithTimeZone</h2>
     * used to get the time in gmt
     * @param OurDate date in string with gmt
     * @param timeZone time zone
     * @return returns date with time zone
     */
    public static String getDateWithTimeZone(String OurDate, TimeZone timeZone)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a",Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            formatter.setTimeZone(timeZone);
            OurDate = formatter.format(value);
            com.karru.utility.Utility.printLog(" time zone time "+OurDate);
        }
        catch (Exception e)
        {
            com.karru.utility.Utility.printLog(" time zone format "+e);
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

    /**
     * <h2>getDayNumberSuffix</h2>
     * This method is used to add teh suffix to the day
     * @param day day
     * @return returns the suffix for the day
     */
    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * <h2>getMonth</h2>
     * used to get the month name from position
     * @param month month position
     * @return returns the month string
     */
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1].substring(0,3);
    }

    /**
     * <h2>hourAdjustment</h2>
     * used to adust the hour to 1 hour format
     * @param hour hour in 24 format
     * @return hour in 12 format
     */
    public String hourAdjustment(int hour,int minute)
    {
        if(hour>12 && hour<24)
            return hour-12+":"+minute+"pm";
        else if (hour ==12 )
            return hour+":"+minute+"pm";
        else if (hour ==24)
            return 12+":"+minute+"am";
        else
            return hour+":"+minute+"am";
    }

    /**
     +     * <h2>getPromoDateFormatter</h2>
     +     * This method is used to convert the date format to MMM dd, hh:mm a
     +     * <p>
     +     *     Convert the date from yyyy-MM-dd HH:mm to MMM dd, hh:mm a
     +     * </p>
     +     * @param dateToBeConverted input the date to be converted
     +     * @return returns the Converted date
     +     */
    public  String getPromoDateFormatter(String dateToBeConverted)
    {

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
        SimpleDateFormat fort=new SimpleDateFormat("dd MMMM yyyy, hh:mm a",Locale.US);
        Date date;
        try {
            if (dateToBeConverted != null)
            {
                date = formatter.parse(dateToBeConverted);
                return fort.format(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




}
