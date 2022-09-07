package com.karru.landing.emergency_contact;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.landing.emergency_contact.model.UserContactInfo;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private ArrayList<UserContactInfo> contactDeatails = new ArrayList<>();
    private AppTypeface appTypeface;
    private EmergencyContactActivity activity;

    @Inject
    public EmergencyContactAdapter(AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    public void setData(ArrayList<UserContactInfo> contactDeatails) {
        this.contactDeatails = contactDeatails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        activity= (EmergencyContactActivity) parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setAppTypeface(holder);
        String name = contactDeatails.get(position).getContactName();
        holder.name.setText(name);
        holder.number.setText(contactDeatails.get(position).getContactNumber());

        if(contactDeatails.get(position).getStatus()!=null && contactDeatails.get(position).getStatus().equals("1"))
            holder.swtch_emergency_contact_status.setChecked(true);
        else
            holder.swtch_emergency_contact_status.setChecked(false);

        String firstLetter = name.charAt(0) + "";
        holder.tv_emergency_contact_pic_letter.setVisibility(View.VISIBLE);
        holder.tv_emergency_contact_pic_letter.setText(firstLetter);
        holder.iv_emergency_contact_menu.setOnClickListener(view -> clickEvent(position, holder.iv_emergency_contact_menu));
        holder.swtch_emergency_contact_status.setOnClickListener(view -> {
            if(holder.swtch_emergency_contact_status.isChecked())
                activity.updateStatus(contactDeatails.get(position).get_id(),1);
            else
                activity.updateStatus(contactDeatails.get(position).get_id(),0);
        });
    }

    /**
     * <h>Set Font Styles</h>
     * <p>this method is using to set typeface of Textview</p>
     * @param viewHolder container of view
     */
    public void setAppTypeface(ViewHolder viewHolder)
    {
        viewHolder.number.setTypeface(appTypeface.getPro_News());
        viewHolder.name.setTypeface(appTypeface.getPro_News());
        viewHolder.tv_emergency_contact_pic_letter.setTypeface(appTypeface.getPro_News());
        viewHolder.tv_emergency_contact_share_det.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h>Click Listner</h>
     * <p>this method will be executed when the clickEvent Occurs</p>
     * @param position item position
     * @param view view reference
     */
    private void clickEvent(int position, View view) {
        activity.createMenu(view, position);
    }

    /**
     * <h>Remove Contact from list</h>
     * <p> this method is using to remove contacts in List</p>
     * @param position item position to remove
     */
    void removeContact(int position)
    {
        contactDeatails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contactDeatails.size());
        if(contactDeatails.size()==0)
            activity.showProfileBackground();
    }

    @Override
    public int getItemCount() {
        return contactDeatails.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_emergency_contact_name) TextView name;
        @BindView(R.id.tv_emergency_contact_number) TextView number;
        @BindView(R.id.tv_emergency_contact_pic_letter) TextView tv_emergency_contact_pic_letter;
        @BindView(R.id.tv_emergency_contact_share_det) TextView tv_emergency_contact_share_det;
        @BindView(R.id.iv_emergency_contact_item_pic) ImageView iv_emergency_contact_item_pic;
        @BindView(R.id.iv_emergency_contact_menu) ImageView iv_emergency_contact_menu;
        @BindView(R.id.swtch_emergency_contact_status) Switch swtch_emergency_contact_status;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
