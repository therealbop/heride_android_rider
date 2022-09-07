package com.karru.landing.home.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.utility.Utility;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karru.utility.Constants.LATER_BOOKING_TYPE;

/**
 * <h1>TimePickerDialog</h1>
 * used to show the later booking schedule
 * @author 3Embed
 * @since on 28/2/18.
 */

public class TimePickerDialog extends Dialog
{
    private static final String TAG = "TimePickerDialog";
    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter presenter;
    private DateFormatter dateFormatter;
    private int minHour,minMinute,minDay,minMonth,minYear;
    private boolean change;

    @BindView(R.id.btn_time_picker_set) Button btn_time_picker_set;
    @BindView(R.id.btn_time_picker_cancel) Button btn_time_picker_cancel;
    @BindView(R.id.btn_time_picker_timePicker) TimePicker btn_time_picker_timePicker;
    @BindView(R.id.btn_time_picker_datePicker) DatePicker btn_time_picker_datePicker;

    public TimePickerDialog(@NonNull Context context, AppTypeface appTypeface,DateFormatter dateFormatter,
                            HomeFragmentContract.Presenter presenter)
    {
        super(context);
        this.appTypeface = appTypeface;
        this.presenter = presenter;
        this.dateFormatter = dateFormatter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_time_picker);

        initialize();
        setTypeface();
    }

    @Override
    public void show()
    {
        super.show();
        presenter.calculateMinDate();
    }

    public void isToChange(boolean change)
    {
        this.change = change;
    }

    @OnClick({R.id.btn_time_picker_cancel,R.id.btn_time_picker_set})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_time_picker_cancel:
                dismiss();
                break;

            case R.id.btn_time_picker_set:
                dismiss();
                int year = btn_time_picker_datePicker.getYear();
                int month = btn_time_picker_datePicker.getMonth()+1;
                int day = btn_time_picker_datePicker.getDayOfMonth();
                int minute = btn_time_picker_timePicker.getCurrentMinute();
                String minuteString = minute+"";

                String monthFormatted = dateFormatter.getMonth(month);
                String dayString = day+"";
                String monthString = month+"";
                if(monthString.length() ==1 )
                    monthString = "0"+monthString;

                if(dayString.length() ==1 )
                    dayString = "0"+dayString;

                int hour24Format = btn_time_picker_timePicker.getCurrentHour();
                String hour = hour24Format+"";

                if(hour.length() ==1 )
                    hour = "0"+hour;

                if(minuteString.length() ==1 )
                    minuteString = "0"+minuteString;

                String hour12Format = dateFormatter.hourAdjustment(hour24Format,minute);
                String dateFormatToShow =  day+" "+monthFormatted+", "+hour12Format;
                String dateFormatToBeSent =  year+"-"+monthString+"-"+dayString+" "+hour+":"+minuteString+":"+"00" ;//YYYY-mm-dd HH:ii:ss
                Utility.printLog(TAG+" later time "+ dateFormatToShow+" "+dateFormatToBeSent);

                presenter.handleClickEventForBooking(LATER_BOOKING_TYPE,dateFormatToShow,
                        dateFormatToBeSent,change);
        }
    }

    /**
     * <h2>initialize</h2>
     * THis method is used to initialize views
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        btn_time_picker_timePicker.setOnTimeChangedListener((TimePicker view, int hourOfDay, int minute) ->
        {
            int month = btn_time_picker_datePicker.getMonth()+1;
            Utility.printLog(TAG+" time change listsner "+minHour +" "+minMinute+" "+
                    hourOfDay+" "+minute+" " +btn_time_picker_datePicker.getDayOfMonth()+" "+minDay
                    +" "+month+" "+minMonth+" "+btn_time_picker_datePicker.getYear()+ " "+minYear);
            if(btn_time_picker_datePicker.getDayOfMonth() <= minDay &&
                    month <= minMonth && btn_time_picker_datePicker.getYear() <= minYear)
            {
                if(hourOfDay < minHour)
                    btn_time_picker_timePicker.setCurrentHour(minHour);

                else if(hourOfDay == minHour)
                {
                    if(minute<minMinute)
                        btn_time_picker_timePicker.setCurrentMinute(minMinute);
                }
            }
        });

        btn_time_picker_datePicker.getCalendarView().setOnDateChangeListener((view, year, month, dayOfMonth) ->
        {
            Utility.printLog(TAG+ "finally found the listener, the date is: year " + year + ", month "  + month + ", dayOfMonth " + dayOfMonth
                    +" "+minDay+" "+minMonth+" "+minYear);
            if(dayOfMonth==minDay && month+1==minMonth && year==minYear )
            {
                btn_time_picker_timePicker.setCurrentHour(minHour);
                btn_time_picker_timePicker.setCurrentMinute(minMinute);
            }
        });
    }

    /**
     * <h2>setMinDateToPicker</h2>
     * used to set the min date in picker
     * @param hour hour
     * @param minute minute
     */
    void setMinDateToPicker(int day,int month,int year, int hour, int minute)
    {
        minHour = hour;
        minMinute = minute;
        minDay = day;
        minMonth = month+1;
        minYear = year;
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, day);
        minDate.set(Calendar.MONTH, month);
        minDate.set(Calendar.YEAR, year);
        Utility.printLog(TAG+" min date millis "+minDate.getTimeInMillis());
        btn_time_picker_datePicker.setMinDate(minDate.getTimeInMillis()-1000);

        btn_time_picker_timePicker.setCurrentHour(hour);
        btn_time_picker_timePicker.setCurrentMinute(minute);
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the typeface
     */
    private void setTypeface()
    {
        btn_time_picker_cancel.setTypeface(appTypeface.getPro_News());
        btn_time_picker_set.setTypeface(appTypeface.getPro_News());
    }
}
