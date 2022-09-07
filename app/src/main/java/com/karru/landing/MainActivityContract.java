package com.karru.landing;


import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;

public interface MainActivityContract
{
    interface MainActView
    {

        /**
         * <h>Dispalya the View</h>
         * <p>this method is using to to dispaly the View based on the value of position</p>
         * @param position is using as the decision value
         */
        void displayView(int position);

        /**
         * <h>Close Drawer</h>
         * <p>this method is using to close the Drawer layout</p>
         */
        void closeDrawer();

        /**
         * <h>Open the Drawer</h>
         * <p>this method is using to open the drawer layout</p>
         */
        void openDrawer();

        /**
         * <h>Setting Header</h>
         * <p>this method is using to setting the header value and Image</p>
         * @param url is the Image URL link
         * @param width is using to set the Image width
         * @param height is using to set the Image height
         * @param userName is the name of the user
         */
        void setHeader(String url, double width, double height, String userName);

        /**
         * <h2>showToast</h2>
         * This method is used to show the toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h2>openInvoiceScreen</h2>
         * This method is used to open the invoice screen
         * @param bundle bundle which contains invoice data
         * @param loginType
         */
        void openInvoiceScreen(Bundle bundle, int loginType);
        /**
         * <h2>checkForEmergencyContact</h2>
         * used to check whether emergency contact tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForEmergencyContact(boolean show);

        /**
         * <h2>checkForWallet</h2>
         * used to check whether wallet contact tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForWallet(boolean show,String walletBal);

        /**
         * <h2>checkForFavorite</h2>
         * used to check whether favorite tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForFavorite(boolean show);

        /**
         * <h2>checkForCard</h2>
         * used to check whether card tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForCard(boolean show);

        /**
         * <h2>checkForCorporate</h2>
         * used to check whether corporate tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForCorporate(boolean show);

        /**
         * <h2>checkForReferral</h2>
         * used to check whether referral code tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForReferral(boolean show);

        /**
         * <h2>checkForTowing</h2>
         * used to check whether referral code tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForTowing(boolean show);

        /**
         * <h2>openLiveChatScreen</h2>
         * used to open live chat screen
         * @param userName user name
         */
        void openLiveChatScreen(String userName);

        /**
         * <h2>openNetworkScreen</h2>
         * used to open network connection page
         */
        void openNetworkScreen();


        void checkForMyBooking(boolean show);

        void checkForLiveChat(boolean show);

        void checkForHelpCenter(boolean show);
    }
    interface MainActPresenter
    {
        /**
         *<h>Get Address</h>
         * <p> this method is to make API call to get Address value</p>
         */
        void getFavAddressApi();

        /**
         * <p>this method is using to checking the Flag vlaues</p>
         */
        void checkFlagConstant();

        /**
         *<h>Resuming the View</h>
         * <p>this method is using to Resuming the view based on flag value</p>
         */
        void workResume();


        /**
         *<h>Check IsDrawerOpen</h>
         * <p>this method is using to check whether Drawer is opened or not</p>
         * @param mDrawerLayout to check
         */
        void isDrawerOpen(DrawerLayout mDrawerLayout);

        /**
         *<h>Managing closing and opening of Menu</h>
         * @param item which is selected
         * @param mDrawerLayout is using to check whether it's opened or not
         * @param mDrawerList is using to fetch values
         */
        void menuItemCheck(MenuItem item, DrawerLayout mDrawerLayout, NavigationView mDrawerList);

        /**
         *<h>Get the header</h>
         * <p>this method is using to get the Header values</p>
         */
        void getHeader();

        /**
         * <h2>subscribeForInvoiceDetails</h2>
         * This method is used to subscribe to the booking details published
         */
        void subscribeForInvoiceDetails();

        /**d ..
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        /**
         * <h2>getLiveChatDetails</h2>
         * used to get live chat details
         */
        void getLiveChatDetails();

        /**
         * <h2>checkForNetwork</h2>
         * to check whether network available
         * @param isConnected is connected to internet
         */
        void checkForNetwork(boolean isConnected);

        void checkRTLConversion();
    }
}
