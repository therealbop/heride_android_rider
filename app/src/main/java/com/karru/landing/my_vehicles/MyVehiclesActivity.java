package com.karru.landing.my_vehicles;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.landing.my_vehicles.add_new_vehicle.AddNewVehicleActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.ADD_VEHICLE;
import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.VEHICLE_DETAIL;

/**
 * <h1>Invite Screen</h1>
 * This class is used to provide the Invite screen, where we can share_logo other persons, by sending shareBodyMessage or mail.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class MyVehiclesActivity extends DaggerAppCompatActivity implements MyVehiclesContract.View
{
    @Inject MyVehiclesContract.Presenter presenter;
    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;
    @Inject MyVehiclesAdapter myVehiclesAdapter;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_add_new_vehicle) TextView tv_add_new_vehicle;
    @BindView(R.id.tv_vehicles_empty) TextView tv_vehicles_empty;
    @BindView(R.id.rv_my_vehicles) RecyclerView rv_my_vehicles;
    @BindView(R.id.pb_my_vehicles) ProgressBar pb_my_vehicles;
    @BindString(R.string.my_vehicles) String my_vehicles;
    @BindString(R.string.alert) String alert;
    @BindString(R.string.delete_vehicle_alert) String delete_vehicle_alert;
    @BindString(R.string.yes) String ok;
    @BindString(R.string.no) String no;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservable();
    }

    /**
     * <h2>initViews</h2>
     * <p>initialize view elements</p>
     */
    private void initializeViews()
    {
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_my_vehicles.setLayoutManager(layoutManager);
        rv_my_vehicles.setAdapter(myVehiclesAdapter);

        myVehiclesAdapter.setParent(getIntent().getIntExtra(COMING_FROM,0));
        presenter.fetchMyVehicles();
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);

        tv_all_tool_bar_title.setText(my_vehicles);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_add_new_vehicle.setTypeface(appTypeface.getPro_narMedium());
        tv_vehicles_empty.setTypeface(appTypeface.getPro_narMedium());
    }

    @Override
    public void setDefaultVehicle(String bundle) {
        Intent intentReturn = getIntent();
        intentReturn.putExtra(VEHICLE_DETAIL, bundle);
        setResult(RESULT_OK, intentReturn);
        finish();
    }

    @Override
    public void showToast(String string) {
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showVehiclesList() {
        rv_my_vehicles.setVisibility(View.VISIBLE);
        pb_my_vehicles.setVisibility(View.GONE);
        myVehiclesAdapter.notifyDataSetChanged();
        tv_vehicles_empty.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        if(!progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button,R.id.tv_add_new_vehicle})
    public void onclickEvent(android.view.View view)
    {
        switch (view.getId())
        {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;

            case R.id.tv_add_new_vehicle:
                Intent intent = new Intent(this,AddNewVehicleActivity.class);
                startActivityForResult(intent,ADD_VEHICLE);
                break;
        }
    }

    @Override
    public void showDeleteVehicleAlert(int position) {
        Dialog deleteDialog = alerts.userPromptWithTwoButtons(this);
        TextView tv_alert_title = deleteDialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body = deleteDialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_yes = deleteDialog.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_no = deleteDialog.findViewById(R.id.tv_alert_no);
        tv_alert_title.setText(alert);
        tv_alert_body.setText(delete_vehicle_alert);
        tv_alert_yes.setText(ok);
        tv_alert_no.setText(no);

        tv_alert_yes.setOnClickListener(view ->
        {
            deleteDialog.dismiss();
            presenter.deleteVehicle(position);
        });
        deleteDialog.show();
    }

    @Override
    public void showNoVehicles() {
        pb_my_vehicles.setVisibility(View.GONE);
        rv_my_vehicles.setVisibility(View.GONE);
        myVehiclesAdapter.notifyDataSetChanged();
        tv_vehicles_empty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_VEHICLE)
        {
            if(resultCode == RESULT_OK)
                presenter.addVehicle(data.getStringExtra(VEHICLE_DETAIL));
        }
    }
}
