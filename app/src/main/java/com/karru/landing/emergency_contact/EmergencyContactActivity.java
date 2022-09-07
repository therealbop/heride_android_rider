package com.karru.landing.emergency_contact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.landing.emergency_contact.model.UserContactInfo;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.PERMISSION_BLOCKED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PICK_CONTACT;
import static com.karru.utility.Constants.READ_CONTACTS_PERMISSIONS_REQUEST;

public class EmergencyContactActivity extends DaggerAppCompatActivity implements
        EmergencyContactContract.EmergencyContactView
{
    private ArrayList<UserContactInfo> contactDeatailsList;

    @BindView(R.id.btn_emergency_contact_add) Button btn_emergency_contact_add;
    @BindView(R.id.tv_emergency_contact_add_limit) TextView tv_emergency_contact_add_limit;
    @BindView(R.id.tv_emergency_contact_alert) TextView tv_emergency_contact_alert;
    @BindView(R.id.tv_emergency_contact_saferMsg) TextView tv_emergency_contact_saferMsg;
    @BindView(R.id.rv_emergency_contact_container) RecyclerView rv_emergency_contact_container;
    @BindView(R.id.ll_emergency_contact_background) LinearLayout ll_emergency_contact_background;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.iv_back_button) ImageView iv_back_button;
    @BindString(R.string.load) String load;
    @BindString(R.string.cant_add) String cant_add;
    @BindString(R.string.emergency) String emergency;
    @BindString(R.string.contacts) String contacts;
    @BindString(R.string.cant_add_extra) String cant_add_extra;
    @BindString(R.string.emergency_contact_alert) String emergency_contact_alert;
    @BindString(R.string.emergency_alert) String emergency_alert;
    @BindString(R.string.emergency_alert_multiple) String emergency_alert_multiple;
    @BindString(R.string.sorry_alert) String sorry_alert;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject EmergencyContactAdapter adapter;
    @Inject AppPermissionsRunTime permissionsRunTime;
    @Inject EmergencyContactContract.EmergencyContactPresenter presenter;

    private ProgressDialog pDialog;
    private int limit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        ButterKnife.bind(this);
        initProgressBar();
        initData();
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        pDialog.dismiss();
    }

    /**
     * <h>Initialize Progress bar</h>
     * <p>this method is using to initialize Progress bar</p>
     */
    public void initProgressBar()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(load);
        pDialog.setCancelable(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.getEmergencyContact();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.clearDisposable();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    /**
     * <h>Initialize Data</h>
     * <p>this method is using to initialize Data</p>
     */
    public void initData()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        btn_emergency_contact_add.setTypeface(appTypeface.getPro_narMedium());
        tv_emergency_contact_alert.setTypeface(appTypeface.getPro_News());
        tv_emergency_contact_add_limit.setTypeface(appTypeface.getPro_News());
        tv_emergency_contact_saferMsg.setTypeface(appTypeface.getPro_News());

        tv_all_tool_bar_title.setText(emergency);
        contactDeatailsList=new ArrayList<>();
        adapter.setData(contactDeatailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_emergency_contact_container.setLayoutManager(mLayoutManager);
        rv_emergency_contact_container.setAdapter(adapter);
    }

    @OnClick({R.id.btn_emergency_contact_add,R.id.iv_back_button,R.id.rl_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_emergency_contact_add:
                getPermissionToReadUserContacts();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                this.onBackPressed();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLimit(int limit)
    {
        this.limit = limit;
        tv_emergency_contact_add_limit.setText(cant_add+" "+limit+" "+contacts);
    }

    /**
     * <h>Create delete Menu</h>
     * <p>this method is using to create delete menu on Contact</p>
     * @param view view which will hold
     * @param position position holded
     */
    public void createMenu(View view,int position)
    {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contact_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case R.id.delete:
                    presenter.deleteContact(contactDeatailsList.get(position).get_id(), position);
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    @Override
    public void onDeleteSuccess(int position)
    {
        adapter.removeContact(position);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode)
        {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    assert contactData != null;
                    @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    assert cursor != null;
                    if (cursor.moveToFirst())
                    {
                        long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String name;
                        String num;
                        if (Integer.valueOf(hasNumber) == 1)
                        {
                            @SuppressLint("Recycle") Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            assert numbers != null;
                            boolean isNumberAdded = false;

                            while (numbers.moveToNext())
                            {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                if(!isNumberAdded )
                                {
                                    presenter.validateMobileNumber(num,name);
                                    isNumberAdded = true;
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void showAlertForInvalidNumber()
    {
        Dialog dialog = alerts.userPromptWithOneButton(emergency_alert,this);
        TextView tv_alert_title = dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_ok = dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_title.setText(sorry_alert);
        tv_alert_ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showAlertForMultipleNumber()
    {
        Dialog dialog = alerts.userPromptWithOneButton(emergency_alert_multiple,this);
        TextView tv_alert_title = dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_ok = dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_title.setText(sorry_alert);
        tv_alert_ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void addContact(String name,String number)
    {
        presenter.addContact(name,number);
        UserContactInfo contactInfo=new UserContactInfo();
        contactInfo.setContactName(name);
        contactInfo.setContactNumber(number);
        contactDeatailsList.add(contactInfo);
        adapter.notifyDataSetChanged();
        presenter.isArrayEmpty(contactDeatailsList);
    }

    @Override
    public void setList(ArrayList<UserContactInfo> contactList)
    {
        Utility.printLog("SaveContact"+contactList.get(0).getContactName());
        this.contactDeatailsList=contactList;
        adapter.setData(contactDeatailsList);
        adapter.notifyDataSetChanged();
        presenter.isArrayEmpty(contactDeatailsList);
    }

    /**
     * <h2>getPermissionToReadUserContacts</h2>
     * check if contact permission given
     */
    public void getPermissionToReadUserContacts()
    {
        if (permissionsRunTime.checkIfPermissionNeeded())
        {
            if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.READ_CONTACTS,this))
                openContactDirectory();
            else
            {
                String[] strings = {Manifest.permission.READ_CONTACTS};
                permissionsRunTime.requestForPermission(strings,this,READ_CONTACTS_PERMISSIONS_REQUEST);
            }
        }
        else
            openContactDirectory();
    }

    /**
     * <h2></h2>
     * used to open the contact directory
     */
    private void openContactDirectory()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST)
        {
            int status =  permissionsRunTime.getPermissionStatus(this,
                    Manifest.permission.READ_CONTACTS, false);
            switch (status)
            {
                case PERMISSION_GRANTED:
                    openContactDirectory();
                    break;

                case PERMISSION_BLOCKED:
                    Toast.makeText(this,emergency_contact_alert,Toast.LENGTH_LONG).show();
                    break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
            }
        }
    }

    /**
     * <h>update Conatct status</h>
     * <p>this method is using to update status of Contact</p>
     * @param id custmer id
     * @param status customer status
     */
    public void updateStatus(String id,int status)
    {
        presenter.updateContact(id,status);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void enableAddContactButton()
    {
        tv_emergency_contact_add_limit.setText(cant_add+" "+limit+" "+contacts);
        btn_emergency_contact_add.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableAddContactButton()
    {
        String extraString = cant_add+" "+limit+" "+contacts+"\n"+cant_add_extra;
        tv_emergency_contact_add_limit.setText(extraString);
        btn_emergency_contact_add.setVisibility(View.GONE);
    }

    @Override
    public void showProfileBackground()
    {
        rv_emergency_contact_container.setVisibility(View.GONE);
        ll_emergency_contact_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProfileBackground()
    {
        rv_emergency_contact_container.setVisibility(View.VISIBLE);
        ll_emergency_contact_background.setVisibility(View.GONE);
    }
}
