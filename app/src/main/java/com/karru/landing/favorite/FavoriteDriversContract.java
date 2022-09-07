package com.karru.landing.favorite;

/**
 * <h1>FavoriteDriversContract</h1>
 * act as contract between view and presenter
 */
public interface FavoriteDriversContract
{
    interface View
    {
        /**
         * <h2>showToast</h2>
         * used to ow toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h>Show Progress Bar</h>
         * <p>this method is using to show the progressBar</p>
         */
        void showProgressDialog();

        /**
         * <h>Dismiss Progress Bar</h>
         * <p>this method is using to dismiss the progressBar</p>
         */
        void dismissProgressDialog();

        /**
         * <h>hideEmptyScreen</h>
         * <p>this method is using to hide the Background when recycler is not empty</p>
         */
        void hideEmptyScreen();

        /**
         * <h>hideOnlineLayout</h>
         * <p>this method is using to hide the online drivers layout</p>
         */
        void hideOnlineLayout();

        /**
         * <h>hideOfflineLayout</h>
         * <p>this method is using to hide the offline drivers layout</p>
         */
        void hideOfflineLayout();

        /**
         * <h>showEmptyScreen</h>
         * <p>this method is using to hide the Relative layout when recycler is empty</p>
         */
        void showEmptyScreen();

        /**
         * <h2>showConfirmationAlert</h2>
         * to show the alert when  user clicked delete
         */
        void showConfirmationAlert(String driverId);
    }
    interface Presenter
    {
       /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>getFavoriteDrivers</h2>
         * used to get all favorite drivers
         */
        void getFavoriteDrivers();

        /**
         * <h2>handleDeleteFavDriver</h2>
         * used to handle the click for delete for fav driver
         */
        void handleDeleteFavDriver(String driverId);

        /**
         * <h2>deleteDriverFromFav</h2>
         * used to delete the driver from fav
         */
        void deleteDriverFromFav(String driverId);

        void checkRTLConversion();
    }
}
