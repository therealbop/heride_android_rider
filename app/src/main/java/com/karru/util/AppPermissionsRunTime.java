package com.karru.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static com.karru.utility.Constants.PERMISSION_BLOCKED;
import static com.karru.utility.Constants.PERMISSION_DENIED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PERMISSION_REQUEST;

/**
 * <h1></h1>
 * <p>
 *     Class to handle app runtime permissions
 * </p>
 *@since 24/5/16.
 */
public class AppPermissionsRunTime
{
    private int REQUEST_CODE_PERMISSIONS = PERMISSION_REQUEST;
    private List<String> permissionsNeeded=null;
    private List<String> permissionsList=null;
    private AlertDialog dialog_parent=null;
    private Alerts alerts;

    public enum Permission
    {
        LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, PHONE, RECORD_AUDIO, READ_CONTACT, READ_SMS, RECEIVE_SMS
    }

    /**
     * Private constructor to make this class as Singleton.
     */
    @Inject
    public AppPermissionsRunTime(Alerts alerts)
    {
        this.alerts = alerts;
    }

    /**
     * Creating the List if not created .
     * if created then clear the list for refresh use.
     * @param permission_list: list of required permissions
     * @param activity: calling activity reference
     * @param isFixed:
     * @return true if requested permissions are already granted
     */
    public boolean getPermission(final ArrayList<Permission> permission_list,Activity activity,
                                 boolean isFixed,int requestCode)
    {
        if(permissionsNeeded==null||permissionsList==null)
        {
            permissionsNeeded= new ArrayList<>();
            permissionsList= new ArrayList<>();
        }else
        {
            permissionsNeeded.clear();
            permissionsList.clear();
        }

        if(dialog_parent!=null&&dialog_parent.isShowing())
        {
            dialog_parent.dismiss();
            dialog_parent.cancel();
        }
        for(int count=0;permission_list!=null&&count<permission_list.size();count++)
        {
            switch (permission_list.get(count))
            {
                case LOCATION:
                    if (!   addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Fine Location");
                    }
                    if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Course Location");
                    }
                    break;
                case RECORD_AUDIO:
                    if (!   addPermission(permissionsList,Manifest.permission.RECORD_AUDIO,activity))
                    {
                        permissionsNeeded.add("Record audio");
                    }
                    break;
                case CAMERA:
                    if (!addPermission(permissionsList, Manifest.permission.CAMERA,activity))
                    {
                        permissionsNeeded.add("Camera");
                    }
                    break;
                case READ_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Write to external Storage");
                    }
                    break;
                case WRITE_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Read to external Storage");
                    }
                    break;
                case PHONE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE,activity))
                    {
                        permissionsNeeded.add("Read Phone State");
                    }
                    break;
                case READ_CONTACT:
                    if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS,activity))
                    {
                        permissionsNeeded.add("Read Contact State");
                    }
                case READ_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.READ_SMS,activity))
                    {
                        permissionsNeeded.add("Read SMS State");
                    }
                case RECEIVE_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS,activity))
                    {
                        permissionsNeeded.add("Receive SMS State");
                    }
                    break;
                default:
                    break;
            }

        }
        if (permissionsList.size() > 0&&permissionsNeeded.size() > 0)
        {
            StringBuilder message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
            for (int i = 1; i < permissionsNeeded.size(); i++)
            {
                message.append(", ").append(permissionsNeeded.get(i));
            }
            REQUEST_CODE_PERMISSIONS = requestCode;
            showAlert(message.toString(),activity,isFixed);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param permissionsList: array of requested permissions
     * @param permission: array of requested permissions
     * @param activity: calling activity reference
     * @return
     */
    private boolean addPermission(List<String> permissionsList,String permission,Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            return false;
        }else
        {
            return true;
        }
    }

    /**
     * <h2>checkIfPermissionGrant</h2>
     * used to check whether permission is granted
     * @param permission permission name
     * @param activity activity
     * @return returns true if granted
     */
    public boolean checkIfPermissionGrant(String permission, Activity activity)
    {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * <h2>showAlert</h2>
     * <p>
     *     method to show alert for requested permission has been granted or not
     * </p>
     * @param message: to be display in permission alert
     * @param mActivity: calling activity reference
     * @param isFixed: boolean true if permission has denied permanently
     */
    private void showAlert(final String message, final Activity mActivity, final boolean isFixed)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Note.");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                requestForPermission(permissionsList.toArray(new String[permissionsList.size()]),mActivity);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (isFixed) {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    mActivity.onBackPressed();
                } else {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }

            }
        });
        dialog_parent = alertDialog.show();
        dialog_parent.show();
    }


    /**
     * <h2>requestForPermission</h2>
     * <p>
     *     method to check whether requested permission has granted or not
     * </p>
     * @param permissions: array of permissions to be requested
     * @param mActivity:* calling activity reference
     */
    private void requestForPermission(String permissions[], Activity mActivity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mActivity.requestPermissions(permissions, REQUEST_CODE_PERMISSIONS);
        }
    }

    /**
     * <h2>requestForPermission</h2>
     * used to request for permission
     * @param permissions permission to be requested
     * @param mActivity activity
     * @param requestCode request code
     */
    public void requestForPermission(String permissions[], Activity mActivity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mActivity.requestPermissions(permissions, requestCode);
        }
    }

    /**
     * <h2>requestForPermission</h2>
     * used to request for permission
     * @param permissions permission to be requested
     * @param mActivity activity
     * @param requestCode request code
     */
    public void requestForPermission(String permissions[], Fragment mActivity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mActivity.requestPermissions(permissions, requestCode);
        }
    }

    /**
     * <h2>getPermissionStatus</h2>
     * used to get the status of the persmission
     * @param activity activity
     * @param androidPermissionName name of permission
     * @return returns the status
     */
    public int getPermissionStatus(Activity activity, String androidPermissionName,boolean mandatePermission)
    {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED)
        {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName))
            {
                if(mandatePermission)
                {
                    Dialog alert = alerts.userPromptWithOneButton(activity.getResources().getString(R.string.permission_alert),activity);
                    TextView tv_alert_ok = alert.findViewById(R.id.tv_alert_ok);
                    TextView tv_alert_title = alert.findViewById(R.id.tv_alert_title);
                    tv_alert_ok.setText(activity.getResources().getString(R.string.ok));
                    tv_alert_title.setText(activity.getResources().getString(R.string.alert_location_permision));
                    tv_alert_ok.setOnClickListener(view ->
                    {
                        alert.dismiss();
                        activity.onBackPressed();
                    });
                    alert.show();
                }
                return PERMISSION_BLOCKED;
            }
            return PERMISSION_DENIED;
        }
        return PERMISSION_GRANTED;
    }

    /**
     * <h2>getPermissionStatus</h2>
     * used to get the status of the persmission
     * @param activity activity
     * @param androidPermissionName name of permission
     * @return returns the status
     */
    public int getPermissionStatusCall(Activity activity, String androidPermissionName,boolean mandatePermission)
    {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED)
        {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName))
            {
                if(mandatePermission)
                {
                    Dialog alert = alerts.userPromptWithOneButton(activity.getResources().getString(R.string.permission_alert),activity);
                    TextView tv_alert_ok = alert.findViewById(R.id.tv_alert_ok);
                    TextView tv_alert_title = alert.findViewById(R.id.tv_alert_title);
                    tv_alert_ok.setText(activity.getResources().getString(R.string.ok));
                    tv_alert_title.setText(activity.getResources().getString(R.string.alert_call_permision));
                    tv_alert_ok.setOnClickListener(view ->
                    {
                        alert.dismiss();
                        activity.onBackPressed();
                    });
                    alert.show();
                }
                return PERMISSION_BLOCKED;
            }
            return PERMISSION_DENIED;
        }
        return PERMISSION_GRANTED;
    }

    /**
     * <h2>checkIfPermissionNeeded</h2>
     * used to check sdk version to ask run time persmission
     * @return returns if to ask persmission
     */
    public boolean checkIfPermissionNeeded()
    {
        return Build.VERSION.SDK_INT >= 23;
    }
}
