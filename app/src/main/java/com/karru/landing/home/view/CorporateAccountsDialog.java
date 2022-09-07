package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>OutstandingBalanceBottomSheet</h1>
 * used to show the last dues bottom sheet dialog
 * @author 3Embed
 * @since on 2/27/2018.
 */
@SuppressLint("ValidFragment")
public class CorporateAccountsDialog extends BottomSheetDialogFragment
{
    private static final String TAG = "OutstandingBalanceBottomSheet";

    @BindView(R.id.tv_driver_preferences_cancel) TextView tv_driver_preferences_cancel;
    @BindView(R.id.tv_driver_preferences_title) TextView tv_driver_preferences_title;
    @BindView(R.id.tv_driver_preferences_done) TextView tv_driver_preferences_done;
    @BindView(R.id.rv_driver_preferences_list) RecyclerView rv_driver_preferences_list;
    @BindString(R.string.pick_profile) String pick_profile;

    private CorporateAccountsAdapter corporateAccountsAdapter;
    private AppTypeface appTypeface;

    public CorporateAccountsDialog(AppTypeface appTypeface,CorporateAccountsAdapter corporateAccountsAdapter)
    {
        this.appTypeface = appTypeface;
        this.corporateAccountsAdapter = corporateAccountsAdapter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_driver_preferences, null);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        dialog.setContentView(contentView);
        initialize(contentView);
        setTypeface();
    }

    @Override
    public void show(FragmentManager manager, String tag)
    {
        super.show(manager, tag);
        corporateAccountsAdapter.notifyDataSetChanged();
    }

    /**
     * <h2>setTypeface</h2>
     * Used to set the typeface
     */
    private void setTypeface()
    {
        tv_driver_preferences_cancel.setTypeface(appTypeface.getPro_narMedium());
        tv_driver_preferences_title.setTypeface(appTypeface.getPro_narMedium());
        tv_driver_preferences_done.setTypeface(appTypeface.getPro_narMedium());
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_driver_preferences_list.setLayoutManager(layoutManager);
        rv_driver_preferences_list.setAdapter(corporateAccountsAdapter);

        tv_driver_preferences_cancel.setVisibility(View.GONE);
        tv_driver_preferences_done.setVisibility(View.GONE);
        tv_driver_preferences_title.setText(pick_profile);
    }

    /**
     * <h2>populateProfiles</h2>
     * used to popluate the profiles data
     * @param corporateProfileData profiles data
     */
    public void populateProfiles(ArrayList<CorporateProfileData> corporateProfileData,
                                 HomeFragmentContract.View view, RentCarContract.View rentalView)
    {
        corporateAccountsAdapter.populateCorporateAccounts(corporateProfileData,view,rentalView);
    }
}