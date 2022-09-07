package com.karru.landing.my_vehicles.add_new_vehicle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_LAT;
import static com.karru.utility.Constants.DROP_LONG;
import static com.karru.utility.Constants.VEHICLE_DETAIL;

public class AddNewVehicleActivity extends DaggerAppCompatActivity implements  AddNewVehicleContract.View {

    @BindView(R.id.spnr_year_select) Spinner spnr_year_select;
    @BindView(R.id.spnr_make_select) Spinner spnr_make_select;
    @BindView(R.id.spnr_mode_select) Spinner spnr_mode_select;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_add_vehicle_year) TextView tv_add_vehicle_year;
    @BindView(R.id.tv_add_vehicle_make) TextView tv_add_vehicle_make;
    @BindView(R.id.tv_add_vehicle_model) TextView tv_add_vehicle_model;
    @BindView(R.id.tv_add_vehicle_color) TextView tv_add_vehicle_color;
    @BindView(R.id.tv_add_vehicle_save) TextView tv_add_vehicle_save;
    @BindView(R.id.et_color_select) EditText et_color_select;
    @BindString(R.string.add_new_vehicle) String add_new_vehicle;
    @BindColor(R.color.vehicle_unselect_color) int vehicle_unselect_color;
    @BindDrawable(R.drawable.grey_login_selector) Drawable grey_login_selector;
    @BindDrawable(R.drawable.signin_login_selector) Drawable signin_login_selector;

    @Inject AddNewVehicleContract.Presenter presenter;
    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;
    private ProgressDialog progressDialog;
    private int yearIndex,makeIndex,modelIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);
        ButterKnife.bind(this);
        initViews();
        setTypeface();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h2>setTypeface</h2>
     * used to set the typeface
     */
    private void setTypeface() {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_add_vehicle_save.setTypeface(appTypeface.getPro_narMedium());
        tv_add_vehicle_year.setTypeface(appTypeface.getPro_News());
        tv_add_vehicle_make.setTypeface(appTypeface.getPro_News());
        tv_add_vehicle_model.setTypeface(appTypeface.getPro_News());
        tv_add_vehicle_color.setTypeface(appTypeface.getPro_News());
        et_color_select.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h2>initViews</h2>
     * used to initialize the views
     */
    void initViews(){
        tv_all_tool_bar_title.setText(add_new_vehicle);
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        presenter.fetchVehicleDetailList(1,0,0);
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button,R.id.tv_add_vehicle_save})
    void onClickEvent(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;

            case R.id.tv_add_vehicle_save:
                presenter.saveVehicleDetails();
                break;
        }
    }

    @OnTextChanged({R.id.et_color_select})
    public void afterTextChanged(Editable editable)
    {
        presenter.validateSavingVehicle(yearIndex,makeIndex,modelIndex,et_color_select.getText().toString());
    }

    @Override
    public void showProgressDialog() {
        progressDialog.setMessage(getString(R.string.pleaseWait));
        if(!progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateYearsSpinner(ArrayList<String> yearData) {
        // Creating adapter for spinner
        ArrayAdapter<String> yearDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearData);
        // Drop down layout style - list view with radio button
        yearDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_year_select.setAdapter(yearDataAdapter);

        //Set the listener for when each option is clicked.
        spnr_year_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(vehicle_unselect_color);
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTypeface(appTypeface.getPro_News());
                yearIndex = position;
                presenter.fetchVehicleDetailList(2,yearIndex,0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public void populateMakesSpinner(ArrayList<String> makesData) {
        // Creating adapter for spinner
        ArrayAdapter<String> makesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, makesData);
        // Drop down layout style - list view with radio button
        makesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_make_select.setAdapter(makesDataAdapter);

        //Set the listener for when each option is clicked.
        spnr_make_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(vehicle_unselect_color);
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTypeface(appTypeface.getPro_News());
                makeIndex = position;
                presenter.fetchVehicleDetailList(3,yearIndex,makeIndex);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public void populateModelsSpinner(ArrayList<String> modelsData) {
        // Creating adapter for spinner
        ArrayAdapter<String> yearDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelsData);
        // Drop down layout style - list view with radio button
        yearDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_mode_select.setAdapter(yearDataAdapter);

        //Set the listener for when each option is clicked.
        spnr_mode_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(vehicle_unselect_color);
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTypeface(appTypeface.getPro_News());
                modelIndex = position;
                presenter.validateSavingVehicle(yearIndex,makeIndex,modelIndex,et_color_select.getText().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public void finishActivity(String response) {
        Intent intentReturn = getIntent();
        intentReturn.putExtra(VEHICLE_DETAIL, response);
        setResult(RESULT_OK, intentReturn);
        finish();
    }

    @Override
    public void enableSaveOption() {
        tv_add_vehicle_save.setBackground(signin_login_selector);
        tv_add_vehicle_save.setEnabled(true);
    }

    @Override
    public void disableSaveOption() {
        tv_add_vehicle_save.setBackground(grey_login_selector);
        tv_add_vehicle_save.setEnabled(false);
    }
}
