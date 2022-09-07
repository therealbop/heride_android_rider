package com.karru.landing.history.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.heride.rider.R;
import com.karru.landing.history.model.HistoryDataModel;
import com.karru.utility.Constants;
import com.karru.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.karru.utility.Constants.BOOKING_HISTORY_TYPE;
import static com.karru.utility.Constants.LOGIN_TYPE;

public class BookingHistoryPagerAdapter extends FragmentPagerAdapter
{
    private static final String TAG = "BookingHistoryPagerAdapter";
    private Context mContext;
    private ArrayList<HistoryDataModel> unassignedList =new ArrayList<>();
    private ArrayList<HistoryDataModel> assignedList =new ArrayList<>();
    private ArrayList<HistoryDataModel> pastList =new ArrayList<>();
    private PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    public BookingHistoryPagerAdapter(FragmentManager fm, Context context, PreferenceHelperDataSource preferenceHelperDataSource)
    {
        super(fm);
        this.mContext = context;
        this.preferenceHelperDataSource = preferenceHelperDataSource;
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * <h>ArrayList Setter</h>
     * <p>this method is using to set ArrayList data</p>
     * @param unassignedList stuff fragment data
     * @param assignedList me fragment data
     */
    void notifyHistoryList(ArrayList<HistoryDataModel> unassignedList,
                           ArrayList<HistoryDataModel> assignedList,
                           ArrayList<HistoryDataModel> pastList)
    {
        this.unassignedList =unassignedList;
        this.assignedList =assignedList;
        this.pastList =pastList;
        notifyDataSetChanged();
    }

    /**
     * <h2>notifyAssignedList</h2>
     * used to notify assigned list
     * @param assignedList assigned list
     */
    void notifyAssignedList(ArrayList<HistoryDataModel> assignedList)
    {
        this.assignedList =assignedList;
        notifyDataSetChanged();
    }

    /**
     * <h2>notifyAssignedList</h2>
     * used to notify assigned list
     * @param assignedList assigned list
     */
    void notifyAllList(ArrayList<HistoryDataModel> unassignedList, ArrayList<HistoryDataModel> assignedList)
    {
        Utility.printLog(TAG+" list of history pastList in API "+
                unassignedList.size()+" "+ assignedList.size());
        this.unassignedList =assignedList;
        this.assignedList =assignedList;

        Utility.printLog(TAG+" list of history pastList in API global "+
                this.unassignedList.size()+" "+  this.assignedList.size());
        notifyDataSetChanged();
    }

    /**
     * <h2>notifyAssignedList</h2>
     * used to notify assigned list
     * @param unassignedList assigned list
     */
    void notifyUnAssignedList(ArrayList<HistoryDataModel> unassignedList)
    {
        this.unassignedList =unassignedList;
        notifyDataSetChanged();
    }

    /**
     * <h2>notifyAssignedList</h2>
     * used to notify assigned list
     * @param pastList assigned list
     */
    void notifyPastList(ArrayList<HistoryDataModel> pastList)
    {
        this.pastList =pastList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Utility.printLog(TAG+" list of history unassignedList in adapter "+unassignedList.size());
                Fragment fragment=new BookingHistoryChildFragment();
                Bundle bundleUnassigned=new Bundle();
                bundleUnassigned.putInt(BOOKING_HISTORY_TYPE,1);
                bundleUnassigned.putSerializable(Constants.BOOKING_HISTORY, unassignedList);
                bundleUnassigned.putInt(LOGIN_TYPE,preferenceHelperDataSource.getLoginType());
                fragment.setArguments(bundleUnassigned);
                return fragment;

            case 1:
                Utility.printLog(TAG+" list of history assignedList in adapter "+assignedList.size());
                Fragment fragment1=new BookingHistoryChildFragment();
                Bundle bundleAssigned=new Bundle();
                bundleAssigned.putInt(BOOKING_HISTORY_TYPE,2);
                bundleAssigned.putSerializable(Constants.BOOKING_HISTORY, assignedList);
                bundleAssigned.putInt(LOGIN_TYPE,preferenceHelperDataSource.getLoginType());
                fragment1.setArguments(bundleAssigned);
                return fragment1;

            case 2:
                Utility.printLog(TAG+" list of history pastList in adapter "+pastList.size());
                Fragment fragment2=new BookingHistoryChildFragment();
                Bundle bundlePast=new Bundle();
                bundlePast.putInt(BOOKING_HISTORY_TYPE,3);
                bundlePast.putSerializable(Constants.BOOKING_HISTORY, pastList);
                bundlePast.putInt(LOGIN_TYPE,preferenceHelperDataSource.getLoginType());
                fragment2.setArguments(bundlePast);
                return fragment2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getString(R.string.unassigned);
            case 1:
                return mContext.getString(R.string.assigned);
            case 2:
                return mContext.getString(R.string.past);
            default:
                return null;
        }
    }
}
