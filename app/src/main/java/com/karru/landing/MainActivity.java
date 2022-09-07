package com.karru.landing;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.karru.AppRater;
import com.karru.ApplicationClass;
import com.karru.help_center.ZendeskHelpActivity;
import com.karru.landing.favorite.view.FavoriteDriversActivity;
import com.karru.landing.home.model.AdvertiseData;
import com.karru.landing.my_vehicles.MyVehiclesActivity;
import com.karru.managers.network.ConnectivityReceiver;
import com.karru.network.NetworkReachableActivity;
import com.heride.rider.R;
import com.karru.landing.corporate.view.CorporateProfileActivity;
import com.karru.booking_flow.invoice.view.InvoiceActivity;
import com.karru.landing.about.AboutActivity;
import com.karru.landing.emergency_contact.EmergencyContactActivity;
import com.karru.landing.history.view.BookingHistoryActivity;
import com.karru.landing.home.view.HomeFragment;
import com.karru.landing.invite.InviteActivity;
import com.karru.landing.live_chat.LiveChatActivity;
import com.karru.landing.payment.PaymentActivity;
import com.karru.landing.profile.ProfileActivity;
import com.karru.landing.rate.RateCardActivity;
import com.karru.landing.support.SupportActivity;
import com.karru.landing.wallet.WalletActivity;
import com.karru.util.ActivityUtils;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.ADVERTISE_DETAILS;
import static com.karru.utility.Constants.CLEAR_STACK;
import static com.karru.utility.Constants.IS_INVOICE_OPEN;
import static com.karru.utility.Constants.LIVE_CHAT_NAME;
import static com.karru.utility.Constants.SCREEN_TITLE;

public class MainActivity extends DaggerAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, MainActivityContract.MainActView,
        ConnectivityReceiver.ConnectivityReceiverListener
{
    private static final String TAG = "ANimation";
    @Inject Context mContext;
    @Inject MainActivityContract.MainActPresenter mainActivityPresenter;
    @Inject AppTypeface appTypeface;
    @Inject HomeFragment homeActivity;
    @Inject ActivityUtils activityUtils;

    @BindView(R.id.dl_main_container) DrawerLayout mDrawerLayout;
    @BindView(R.id.nd_activity_main_navigator) NavigationView mDrawerList;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;
    @BindString(R.string.double_press_exit) String double_press_exit;
    @BindString(R.string.payments) String payments;
    @BindString(R.string.wallet) String wallet;

    private long backPressed;
    public Fragment fragment = null;
    View headerView;
    private MenuItem item_menu_nav_order,item_menu_emergency,item_menu_nav_wallet,item_menu_nav_favorite,
            item_menu_nav_payment,item_menu_nav_corporate,item_menu_nav_invite,item_menu_nav_vehicle,item_menu_nav_liveChat,item_menu_nav_helpCenter;
    private TextView tv_main_drawer_viewProfile, tv_main_drawer_name;
    private ImageView iv_main_drawer_profilepic;
    private ProgressBar pb_main_drawer_progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
    }



    /**
     * <h2>initialize</h2>
     * This method is used to initialize all the data required for Main Activity
     */
    public void initialize()
    {
        //Change 'YourConnectionChangedBroadcastReceiver'
        //to the class defined to handle the broadcast in your app
        registerReceiver(new ConnectivityReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        String dataObject = getIntent().getStringExtra(ADVERTISE_DETAILS);
        if(dataObject == null)
            dataObject = getIntent().getStringExtra("data");
        Utility.printLog(TAG+" advertise string "+dataObject);
        AdvertiseData advertiseData = new Gson().fromJson(dataObject, AdvertiseData.class);
        if(advertiseData!=null)
        {
            Intent intent = new Intent(this, com.karru.ads.AdvertiseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ADVERTISE_DETAILS,advertiseData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        mainActivityPresenter.getFavAddressApi();
        mainActivityPresenter.checkFlagConstant();
        headerView = mDrawerList.getHeaderView(0);
        tv_main_drawer_viewProfile = headerView.findViewById( R.id.tv_main_drawer_viewProfile);
        iv_main_drawer_profilepic = headerView.findViewById(  R.id.iv_main_drawer_profilepic);
        tv_main_drawer_name = headerView.findViewById(  R.id.tv_main_drawer_name);
        pb_main_drawer_progress = headerView.findViewById(  R.id.pb_main_drawer_progress);

        headerView.setOnClickListener(v ->
        {
            Intent intent =new Intent(this,ProfileActivity.class);
            startActivity(intent);
            mDrawerLayout.closeDrawer(mDrawerList);
        });

        mDrawerList.setNavigationItemSelectedListener(this);

        //to get the wallet menu item
        Menu menu = mDrawerList.getMenu();
        // find MenuItem you want to change
        item_menu_emergency = menu.findItem(R.id.item_menu_emergency);
        item_menu_nav_wallet = menu.findItem(R.id.item_menu_nav_wallet);
        item_menu_nav_favorite = menu.findItem(R.id.item_menu_nav_favorite);
        item_menu_nav_payment = menu.findItem(R.id.item_menu_nav_payment);
        item_menu_nav_corporate = menu.findItem(R.id.item_menu_nav_corporate);
        item_menu_nav_invite = menu.findItem(R.id.item_menu_nav_invite);
        item_menu_nav_vehicle = menu.findItem(R.id.item_menu_nav_vehicle);
        item_menu_nav_order = menu.findItem(R.id.item_menu_nav_order);
        item_menu_nav_liveChat = menu.findItem(R.id.item_menu_nav_liveChat);
        item_menu_nav_helpCenter = menu.findItem(R.id.item_menu_nav_helpCenter);

        setFonts();
        Utility.changeStatusBarColor(MainActivity.this, getWindow());
        AppRater.app_launched(MainActivity.this,appTypeface);
    }


    /**
     * <h2>setHeaderView</h2>
     * <p>
     * This method is used to set the data on Header View.
     * </p>
     */
    public void setHeaderView()
    {
        mainActivityPresenter.getHeader();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mainActivityPresenter.disposeObservable();
    }

    @Override
    public void setHeader(String url, double width, double height, String userName)
    {
        tv_main_drawer_name.setText(userName);

        if(!url.equals(""))
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(signup_profile_default_image);
            requestOptions = requestOptions.optionalCircleCrop();
            Glide.with(mContext)
                    .load(url)
                    .listener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb_main_drawer_progress.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb_main_drawer_progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(iv_main_drawer_profilepic);
        }
        else
            pb_main_drawer_progress.setVisibility(View.GONE);
    }

    /**
     * <h2>setFonts</h2>
     * <p>
     * This method is used for setting the FontFace.
     * </p>
     */
    private void setFonts()
    {
        Typeface clanProNarrowNews = appTypeface.getPro_News();
        tv_main_drawer_viewProfile.setTypeface(clanProNarrowNews);
        tv_main_drawer_name.setTypeface(clanProNarrowNews);
    }

    /**
     * <h2>displayView</h2>
     * <p>
     * This method is used to open a proper fragment, based on our given requirement.
     * </p>
     *
     * @param position position of fragments.
     */
    @Override
    public void displayView(int position)
    {
        mDrawerLayout.closeDrawer(mDrawerList);
        switch (position)
        {
            case 1:
                activityUtils.addFragmentToActivity(homeActivity,R.id.fl_activity_main_container);
                break;

            case 2:
                Intent historyIntent =new Intent(this,BookingHistoryActivity.class);
                startActivity(historyIntent);
                break;

            case 3:
                Intent rateCardIntent =new Intent(this,RateCardActivity.class);
                startActivity(rateCardIntent);
                break;

            case 4:
                Intent supportIntent =new Intent(this,SupportActivity.class);
                startActivity(supportIntent);
                break;

            case 5:
                Intent aboutIntent =new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                break;

            case 6:
                Intent inviteIntent =new Intent(this,InviteActivity.class);
                startActivity(inviteIntent);
                break;

            case 7:
                Intent paymentIntent =new Intent(this,PaymentActivity.class);
                paymentIntent.putExtra(SCREEN_TITLE,payments);
                startActivity(paymentIntent);
                break;

            case 8:
                Intent walletIntent =new Intent(this,WalletActivity.class);
                startActivity(walletIntent);
                break;

            case 9:
                mainActivityPresenter.getLiveChatDetails();
                break;

            case 10:
                Intent intent = new Intent(this, EmergencyContactActivity.class);
                startActivity(intent);
                break;

            case 11:
                Intent corporateProfile = new Intent(this, CorporateProfileActivity.class);
                startActivity(corporateProfile);
                break;

            case 12:
                Intent favorites = new Intent(this, FavoriteDriversActivity.class);
                startActivity(favorites);
                break;

            case 13:
                Intent zendesk = new Intent(this, ZendeskHelpActivity.class);
                startActivity(zendesk);
                break;

            case 14:
                Intent myVehicleIntent = new Intent(this, MyVehiclesActivity.class);
                startActivity(myVehicleIntent);
                break;
        }
    }

    @Override
    public void openLiveChatScreen(String userName)
    {
        Intent liveChat =new Intent(this,LiveChatActivity.class);
        liveChat.putExtra(LIVE_CHAT_NAME,userName);
        startActivity(liveChat);
    }

    /**
     * <h2>moveDrawer</h2>
     * <p>
     * checking drawer current state (Opens/Closed)
     * </p>
     */
    public void moveDrawer(DrawerLayout mDrawerLayout) {
        mainActivityPresenter.isDrawerOpen(mDrawerLayout);
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mainActivityPresenter.checkRTLConversion();
        // register connection status listener
        ApplicationClass.getInstance().setConnectivityListener(this);
        setHeaderView();
        ((ApplicationClass) getApplicationContext()).changeLangConfig();
        mainActivityPresenter.subscribeForInvoiceDetails();
        mainActivityPresenter.workResume();
    }

    @Override
    public void openNetworkScreen() {
        Intent intent = new Intent(this, NetworkReachableActivity.class);
        startActivity(intent);
    }

    @Override
    public void checkForMyBooking(boolean show) {
        if(show)
            item_menu_nav_order.setVisible(true);
        else
            item_menu_nav_order.setVisible(false);

    }

    @Override
    public void checkForLiveChat(boolean show) {
        if(show)
            item_menu_nav_liveChat.setVisible(true);
        else
            item_menu_nav_liveChat.setVisible(false);
    }

    @Override
    public void checkForHelpCenter(boolean show) {
        if(show)
            item_menu_nav_helpCenter.setVisible(true);
        else
            item_menu_nav_helpCenter.setVisible(false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        createMenu(menu);
        return true;
    }

    @Override
    public void checkForEmergencyContact(boolean show)
    {
        if(show)
            item_menu_emergency.setVisible(true);
        else
            item_menu_emergency.setVisible(false);
    }

    @Override
    public void checkForWallet(boolean show,String walletBal)
    {
        Utility.printLog(TAG+" is wallet enabled "+show);
        item_menu_nav_wallet.setTitle(wallet+" ("+walletBal+")");
        if(show)
            item_menu_nav_wallet.setVisible(true);
        else
            item_menu_nav_wallet.setVisible(false);
    }

    @Override
    public void checkForFavorite(boolean show) {
        if(show)
            item_menu_nav_favorite.setVisible(true);
        else
            item_menu_nav_favorite.setVisible(false);
    }

    @Override
    public void checkForCard(boolean show) {
        if(show)
            item_menu_nav_payment.setVisible(true);
        else
            item_menu_nav_payment.setVisible(false);
    }

    @Override
    public void checkForCorporate(boolean show)
    {
        if(show)
            item_menu_nav_corporate.setVisible(true);
        else
            item_menu_nav_corporate.setVisible(false);
    }

    @Override
    public void checkForReferral(boolean show)
    {
        if(show)
            item_menu_nav_invite.setVisible(true);
        else
            item_menu_nav_invite.setVisible(false);
    }

    @Override
    public void checkForTowing(boolean show)
    {
        if(show)
           // item_menu_nav_vehicle.setVisible(true);
            item_menu_nav_vehicle.setVisible(false);
        else
            item_menu_nav_vehicle.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        mainActivityPresenter.menuItemCheck(item, mDrawerLayout, mDrawerList);
        return super.onOptionsItemSelected(item);
    }

    /**
     * <h2>createMenu</h2>
     * <p>
     * THis method is creating the menu
     * </p>
     *
     * @param menu containing the Menu type.
     */
    public void createMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, 0, 0, "");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * This is an overrided method got called, when we select any options on Navigation item.
     *
     * @param item Menu Item's.
     * @return boolean flag.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.item_menu_nav_home:
                displayView(1);
                break;

            case R.id.item_menu_nav_order:
                displayView(2);
                break;

            case R.id.item_menu_nav_ratecard:
                displayView(3);
                break;

            case R.id.item_menu_nav_support:
                displayView(4);
                break;

            case R.id.item_menu_nav_about:
                displayView(5);
                break;

            case R.id.item_menu_nav_invite:
                displayView(6);
                break;

            case R.id.item_menu_nav_payment:
                displayView(7);
                break;

            case R.id.item_menu_nav_wallet:
                displayView(8);
                break;

            case R.id.item_menu_nav_liveChat:
                displayView(9);
                break;

            case R.id.item_menu_emergency:
                displayView(10);
                break;

            case R.id.item_menu_nav_corporate:
                displayView(11);
                break;

            case R.id.item_menu_nav_favorite:
                displayView(12);
                break;

            case R.id.item_menu_nav_helpCenter:
                displayView(13);
                break;

            case R.id.item_menu_nav_vehicle:
                displayView(14);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                ActivityCompat.finishAffinity(this);
            } else {
                Toast.makeText(getBaseContext(), double_press_exit, Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void openInvoiceScreen(Bundle bundle, int loginType)
    {
        if(!isFinishing() && loginType != 0)
        {
            return;
        }else {
            if(!isFinishing())
            {
                IS_INVOICE_OPEN = true;
                Intent intent = new Intent(this, InvoiceActivity.class);
                intent.putExtras(bundle);
                bundle.putBoolean(CLEAR_STACK,false);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        mainActivityPresenter.checkForNetwork(isConnected);
    }
}
