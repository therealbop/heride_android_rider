package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.karru.landing.home.model.DriverPreferenceModel;
import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>OutstandingBalanceBottomSheet</h1>
 * used to show the last dues bottom sheet dialog
 * @author 3Embed
 * @since on 2/27/2018.
 */
@SuppressLint("ValidFragment")
public class DriverPreferenceBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "OutstandingBalanceBottomSheet";

    @BindView(R.id.tv_driver_preferences_cancel) TextView tv_driver_preferences_cancel;
    @BindView(R.id.tv_driver_preferences_title) TextView tv_driver_preferences_title;
    @BindView(R.id.tv_driver_preferences_done) TextView tv_driver_preferences_done;
    @BindView(R.id.rv_driver_preferences_list) RecyclerView rv_driver_preferences_list;

    private DriverPreferencesAdapter driverPreferencesAdapter;
    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter homeFragmentPresenter;
    private RentCarContract.Presenter rentalPresenter;

    public DriverPreferenceBottomSheet(AppTypeface appTypeface, DriverPreferencesAdapter driverPreferencesAdapter,
                                       HomeFragmentContract.Presenter homeFragmentPresenter,RentCarContract.Presenter rentalPresenter)
    {
        this.appTypeface = appTypeface;
        this.homeFragmentPresenter = homeFragmentPresenter;
        this.rentalPresenter = rentalPresenter;
        this.driverPreferencesAdapter = driverPreferencesAdapter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_driver_preferences, null);
        dialog.setContentView(contentView);
        initialize(contentView);
        setTypeface();
    }

    @Override
    public void show(FragmentManager manager, String tag)
    {
        super.show(manager, tag);
        driverPreferencesAdapter.notifyDataSetChanged();
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
        rv_driver_preferences_list.setAdapter(driverPreferencesAdapter);
    }

    @OnClick({R.id.tv_driver_preferences_cancel,R.id.tv_driver_preferences_done})
    public void clickEvent(View view)
    {
        this.getDialog().dismiss();
        switch (view.getId())
        {
            case R.id.tv_driver_preferences_done:
                if(homeFragmentPresenter != null)
                    homeFragmentPresenter.addDriverPreferences(true);
                else
                    rentalPresenter.addDriverPreferences(true);
                break;

            case R.id.tv_driver_preferences_cancel:
                if(homeFragmentPresenter != null)
                    homeFragmentPresenter.addDriverPreferences(false);
                else
                    rentalPresenter.addDriverPreferences(false);
                break;
        }
    }
}

