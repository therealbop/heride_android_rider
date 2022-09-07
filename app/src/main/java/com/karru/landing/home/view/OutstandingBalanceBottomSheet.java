package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

/**
 * <h1>OutstandingBalanceBottomSheet</h1>
 * used to show the last dues bottom sheet dialog
 * @author 3Embed
 * @since on 2/27/2018.
 */
@SuppressLint("ValidFragment")
public class OutstandingBalanceBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "OutstandingBalanceBottomSheet";

    @BindView(R.id.tv_last_dues_title) TextView tv_last_dues_title;
    @BindView(R.id.tv_last_dues_address) TextView tv_last_dues_address;
    @BindView(R.id.tv_last_dues_details) TextView tv_last_dues_details;
    @BindView(R.id.tv_last_dues_cancel) TextView tv_last_dues_cancel;
    @BindView(R.id.tv_last_dues_confirm) TextView tv_last_dues_confirm;
    @BindString(R.string.trip_from) String trip_from;

    private AppTypeface appTypeface;
    private String title,businessName ,bookingDate,pickupAddress;
    private HomeFragmentContract.View homeView;
    private RentCarContract.View view;
    private CoordinatorLayout.Behavior behavior;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {

                case BottomSheetBehavior.STATE_COLLAPSED:{

                    Utility.printLog(TAG+"collapsed");
                }
                case BottomSheetBehavior.STATE_SETTLING:{

                    Utility.printLog(TAG+"settling");
                }
                case BottomSheetBehavior.STATE_EXPANDED:{

                    Utility.printLog(TAG+"expanded");
                }
                case STATE_HIDDEN: {

                    Utility.printLog(TAG+"hidden");
                    dismiss();
                }
                case BottomSheetBehavior.STATE_DRAGGING: {
                    Utility.printLog(TAG+"dragging");
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            Utility.printLog(TAG+" sliding "+slideOffset);
        }
    };

    public OutstandingBalanceBottomSheet(AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_fragment_outstanding, null);
        dialog.setContentView(contentView);
        initialize(contentView);
        setTypeface();
    }

    /**
     * <h2>setTypeface</h2>
     * Used to set the typeface
     */
    private void setTypeface()
    {
        tv_last_dues_title.setTypeface(appTypeface.getPro_News());
        tv_last_dues_address.setTypeface(appTypeface.getPro_News());
        tv_last_dues_details.setTypeface(appTypeface.getPro_News());
        tv_last_dues_cancel.setTypeface(appTypeface.getPro_narMedium());
        tv_last_dues_confirm.setTypeface(appTypeface.getPro_narMedium());
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize the variables
     */
    @SuppressLint("SetTextI18n")
    private void initialize(View contentView)
    {
        Utility.printLog(TAG+" initialize bottomsheet ");
        ButterKnife.bind(this,contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        tv_last_dues_title.setText(title);
        tv_last_dues_details.setText(businessName+", "+bookingDate);
        tv_last_dues_address.setText(trip_from+" "+pickupAddress);
    }

    /**
     * <h2>populateValues</h2>
     * used to populate the data and show the dialog
     */
    public void populateValues(String title, String businessName, String bookingDate,
                               String pickupAddress, FragmentManager fragmentManager,
                               HomeFragmentContract.View homeView, RentCarContract.View view)
    {
        this.homeView = homeView;
        this.view = view;
        this.title = title;
        this.businessName = businessName;
        this.bookingDate = bookingDate;
        this.pickupAddress = pickupAddress;
        show(fragmentManager,"BottomSheet Fragment");
    }

    @OnClick({R.id.tv_last_dues_cancel,R.id.tv_last_dues_confirm})
    public void clickEvent(View view)
    {
        ((BottomSheetBehavior) behavior).setState(STATE_HIDDEN);
        switch (view.getId())
        {
            case R.id.tv_last_dues_confirm:
                if(homeView != null)
                    homeView.confirmToBook();
                else
                    this.view.confirmToBook();
                break;
        }
    }
}
