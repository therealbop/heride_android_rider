package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heride.rider.R;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karru.utility.Constants.PICK_CONTACT;

@SuppressLint("ValidFragment")
public class SomeOneRideBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "SomeOneRideBottomSheet";
    private SomeOneRideContactsAdapter imageAdapter;
    private ArrayList<ContactDetails> contactDetailsList = new ArrayList<>();
    @BindView(R.id.rv_some_one_container) RecyclerView rv_some_one_container;
    @BindView(R.id.tv_someOne_header) TextView tv_someOne_header;
    @BindView(R.id.tv_someOne_note) TextView tv_someOne_note;
    @BindView(R.id.tv_someOne_profile_name) TextView tv_someOne_profile_name;
    @BindView(R.id.btn_someOne_confirm) Button btn_someOne_confirm;
    @BindView(R.id.rl_someOne_add) RelativeLayout rl_someOne_add;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable book_profile_green_default_image;

    private int bookingType;
    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter presenter;
    private HomeFragmentContract.View homeView;

    @SuppressLint("ValidFragment")
    @Inject
    public SomeOneRideBottomSheet(AppTypeface appTypeface, HomeFragmentContract.Presenter presenter)
    {
        this.presenter=presenter;
        this.appTypeface=appTypeface;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        contactDetailsList.clear();
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.rl_someOne_add,R.id.btn_someOne_confirm})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_someOne_confirm:
                Utility.printLog(TAG+" posision for slectioin "+imageAdapter.lastCheckedPosition);
                if(imageAdapter.lastCheckedPosition!=0)
                {
                    presenter.setSomeOneDetails(contactDetailsList.get(imageAdapter.lastCheckedPosition).getName(),
                            contactDetailsList.get(imageAdapter.lastCheckedPosition).getPhoneNumber(),bookingType);
                }
                else
                    presenter.setSomeOneDetails("", "",bookingType);

                getDialog().dismiss();
                break;

            case R.id.rl_someOne_add:
                this.homeView.askContactPermission();
                break;
        }
    }

    /**
     * <h2>checkBookingTypeAndShow</h2>
     * used to check for booking type
     */
    public void checkBookingTypeAndShow(androidx.fragment.app.FragmentManager fragmentManager,
                                        int bookingType, HomeFragmentContract.View homeView)
    {
        this.bookingType= bookingType;
        this.homeView= homeView;
        show(fragmentManager,"kmkvmk");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_some_one_booking, container, false);
        ButterKnife.bind(this,view);
        initRecycler();
        setAppTypeface();
        return view;
    }

    /**
     * <h2>initRecycler</h2>
     * used to initialize recycler view
     */
    public void initRecycler()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()//.getApplicationContext()
                , LinearLayoutManager.HORIZONTAL, false);
        rv_some_one_container.setLayoutManager(linearLayoutManager);
        imageAdapter = new SomeOneRideContactsAdapter(this, contactDetailsList,appTypeface);
        imageAdapter.notifyDataSetChanged();
        rv_some_one_container.setAdapter(imageAdapter);
        presenter.getUserDetails();
    }

    /**
     * <h2>setAppTypeface</h2>
     * used to set the type face
     */
    public void setAppTypeface()
    {
        tv_someOne_header.setTypeface(appTypeface.getPro_narMedium());
        btn_someOne_confirm.setTypeface(appTypeface.getPro_narMedium());
        tv_someOne_profile_name.setTypeface(appTypeface.getPro_narMedium());
        tv_someOne_note.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h2>addContact</h2>
     * used to add the contact
     */
    public void addContact()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.startActivityForResult(intent, PICK_CONTACT);
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
                    @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    assert cursor != null;
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String name;
                        String num;
                        if (Integer.valueOf(hasNumber) == 1)
                        {
                            @SuppressLint("Recycle") Cursor numbers =getActivity(). getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            assert numbers != null;
                            boolean isNumberAdded =false;
                            ArrayList<String> listOfContacts = new ArrayList<>();
                            for (ContactDetails contactDetails:contactDetailsList)
                            {
                                listOfContacts.add(contactDetails.getPhoneNumber());
                            }

                            while (numbers.moveToNext())
                            {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                if(!isNumberAdded )
                                {
                                    Utility.printLog(" MyNameFindv "+name+"--"+num);
                                    presenter.validateMobileNumber(num,name,listOfContacts);
                                    isNumberAdded = true;
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    /**
     * <h2>setDefaultProfile</h2>
     * used to put the default profile image
     */
    public void setDefaultProfile(String phNumber,String userName,String imageUrl)
    {
        ContactDetails contactDeatails=new ContactDetails(userName);
        contactDeatails.setPhoneNumber(phNumber);
        contactDeatails.setName(userName);
        contactDeatails.setImgUrl(imageUrl);
        contactDetailsList.add(contactDeatails);
        imageAdapter.notifyDataSetChanged();
    }

    /**
     * <h2>setData</h2>
     * used to set the data to the list
     * @param name name of the contact
     * @param num number
     */
    public void setData(String name,String num)
    {
        ContactDetails contactDetails=new ContactDetails(name);
        contactDetails.setPhoneNumber(num);
        contactDetails.setName(name);
        contactDetailsList.add(contactDetails);
        imageAdapter.notifyDataSetChanged();
    }
}
