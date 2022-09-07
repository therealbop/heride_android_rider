package com.karru.landing.history;

import com.karru.landing.BasePresenter;
import com.karru.landing.history.model.HistoryDataModel;

import java.util.ArrayList;

/**
 * <h1>BookingHistoryContract</h1>
 * usd to provide contract between view and presenter
 * @author 3Embed
 * @since on 2/22/2018.
 */
public interface BookingHistoryContract
{
    interface View
    {
        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to dismiss the progress dialog
         */
        void dismissProgressDialog();

        /**
         * <h2>notifyPagerAdapter</h2>
         * used to notify pager adapter
         * @param unassignedList unassigned bookings list
         * @param assignedList assignedList bookings list
         * @param pastList pastList bookings list
         */
        void notifyPagerAdapter(ArrayList<HistoryDataModel> unassignedList,
                                ArrayList<HistoryDataModel> assignedList,
                                ArrayList<HistoryDataModel> pastList);

        /**
         * <h2>notifyAllList</h2>
         * used to notify all lists
         * @param unassignedList unassigned bookings list
         * @param assignedList assignedList bookings list
         * @param pastList pastList bookings list
         */
        void notifyAllList(ArrayList<HistoryDataModel> unassignedList,
                                ArrayList<HistoryDataModel> assignedList,
                                ArrayList<HistoryDataModel> pastList);

        /**
         * <h2>notifyAssignedList</h2>
         * used to notify pager adapter
         * @param assignedList assignedList bookings list
         */
        void notifyAssignedList(ArrayList<HistoryDataModel> assignedList);
        /**
         * <h2>notifyPagerAdapter</h2>
         * used to notify pager adapter
         * @param unassignedList unassigned bookings list
         */
        void notifyUnassignedList(ArrayList<HistoryDataModel> unassignedList);
        /**
         * <h2>notifyPastList</h2>
         * used to notify pager adapter
         * @param pastList pastList bookings list
         */
        void notifyPastList(ArrayList<HistoryDataModel> pastList);

        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message to be shown
         */
        void showToast(String message);
    }

    interface Presenter
    {
        /**
         * <h2>getBookingHistory</h2>
         * used to get the bookings history
         */
        void getBookingHistory(boolean first);

        /**
         * <h2>disposeObservables</h2>
         * used to dispose observables
         */
        void disposeObservables();
        /**
         * <h2>subscribeObservables</h2>
         * used to subscribe observables
         */
        void subscribeObservables();

        void checkRTLConversion();
    }
}
