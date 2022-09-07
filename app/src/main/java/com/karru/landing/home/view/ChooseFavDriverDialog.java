package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * <h1><ChooseFavDriverDialog/h1>
 *     used to show the dialog for fav drivers
 * @author 3Embed
 * @since on 27-04-2018.
 */
@SuppressLint("ValidFragment")
public class ChooseFavDriverDialog extends BottomSheetDialogFragment
{
    @BindView(R.id.rv_home_fav_drivers_list) RecyclerView rv_home_fav_drivers_list;
    @BindView(R.id.tv_home_fav_header) TextView tv_home_fav_header;
    @BindView(R.id.btn_home_fav_drivers_confirm) Button btn_home_fav_drivers_confirm;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable book_profile_green_default_image;

    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter presenter;
    private int bookingType ;
    private FavDriversListAdapter favDriversListAdapter;

    @SuppressLint("ValidFragment")
    @Inject
    public ChooseFavDriverDialog(AppTypeface appTypeface, HomeFragmentContract.Presenter presenter,
                                 FavDriversListAdapter favDriversListAdapter)
    {
        this.presenter=presenter;
        this.favDriversListAdapter=favDriversListAdapter;
        this.appTypeface=appTypeface;
    }

    @OnClick(R.id.btn_home_fav_drivers_confirm)
    public void clickEvent(View view)
    {
        if(favDriversListAdapter.lastCheckedPosition != -1)
        {
            getDialog().dismiss();
            if(favDriversListAdapter.lastCheckedPosition == 0)
                presenter.chooseAllFavDrivers(bookingType);
            else if(favDriversListAdapter.lastCheckedPosition == favDriversListAdapter.getItemCount()-1)
                presenter.chooseAllDrivers(bookingType);
            else
                presenter.chooseAFavDriver(favDriversListAdapter.favDriversList.get(favDriversListAdapter.
                        lastCheckedPosition).getMasterId(),bookingType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_choose_fav_drivers, container, false);
        ButterKnife.bind(this,view);
        initRecycler();
        setAppTypeface();
        return view;
    }

    /**
     * <h2>setBookingType</h2>
     * used to set the booking type
     * @param bookingType booking type
     */
    public void setBookingType(int bookingType)
    {
        this.bookingType = bookingType;
    }

    /**
     * <h2>initRecycler</h2>
     * used to initialize recycler view
     */
    public void initRecycler()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()//.getApplicationContext()
                , LinearLayoutManager.HORIZONTAL, false);
        rv_home_fav_drivers_list.setLayoutManager(linearLayoutManager);
        favDriversListAdapter.notifyDataSetChanged();
        rv_home_fav_drivers_list.setAdapter(favDriversListAdapter);
    }

    /**
     * <h2>notifyViewWithLists</h2>
     * used to notify drivers list
     */
    public void notifyViewWithLists()
    {
        favDriversListAdapter.notifyDataSetChanged();
    }

    /**
     * <h2>setAppTypeface</h2>
     * used to set the type face
     */
    public void setAppTypeface()
    {
        tv_home_fav_header.setTypeface(appTypeface.getPro_narMedium());
        btn_home_fav_drivers_confirm.setTypeface(appTypeface.getPro_narMedium());
    }
}
