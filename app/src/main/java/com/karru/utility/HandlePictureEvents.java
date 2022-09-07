package com.karru.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.heride.rider.R;
import com.karru.util.image_upload.AmazonImageUpload;
import com.karru.util.image_upload.AmazonUpload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.os.Build.VERSION_CODES.N;
import static com.karru.utility.Constants.CAMERA_PIC;
import static com.karru.utility.Constants.GALLERY_PIC;
import static com.karru.utility.Constants.OREO_PERMISSION;

/**
 * <h>HandlePictureEvents</h>
 * this class open the popup for the option to take the image
 * after it takes the the, it crops the image
 * and then upload it to amazon
 * Created by ${Ali} on 8/17/2017.
 */

public class HandlePictureEvents
{
    private static final String TAG = "HandlePictureEvents";
    private Activity mcontext = null;
    private String takenNewImage;
    public File newFile;
    private Fragment fragment = null;
    private AmazonImageUpload amazons3 ;
    private  String amazonFileName="";

    public HandlePictureEvents(Activity mcontext , Fragment fragment)
    {
        this.fragment = fragment;
        this.mcontext = mcontext;
        initializeAmazon();
    }

    /**
     * <h2>initializeAmazon</h2>
     * <p>
     *     method to configure and initialize AmazonS3
     * </p>
     */
    private void initializeAmazon()
    {
        amazons3 = AmazonImageUpload.getInstance();
        AmazonUpload.configureSettings(mcontext.getString(R.string.AMAZON_ACCESS_KEY_ID), mcontext.getString(R.string.AMAZON_SECRET_KEY), Regions.US_EAST_1);
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(mcontext.getString(R.string.AMAZON_ACCESS_KEY_ID),
                mcontext.getString(R.string.AMAZON_SECRET_KEY)));
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    /**
     * <h>openDialog</h>
     * <p>
     * this dialog have the option to choose whether to take picture
     * or open gallery or cancel the dialog
     * </p>
     */

    public void openDialog()
    {
        takenNewImage = "DayRunner"+String.valueOf(System.nanoTime())+".png";
        CreateOrClearDirectory directory = CreateOrClearDirectory.getInstance();
        newFile = directory.getAlbumStorageDir(mcontext, Constants.PARENT_FOLDER+"/Profile_Pictures",false);
        final Resources resources = mcontext.getResources();
        final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery), resources.getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(resources.getString(R.string.TakePhoto)))
            {
                takePicFromCamera();
            }
            else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                if(fragment!=null)
                    fragment.startActivityForResult(photoPickerIntent, GALLERY_PIC);
                else
                    mcontext.startActivityForResult(photoPickerIntent, GALLERY_PIC);
                mcontext.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
            else if (options[item].equals(resources.getString(R.string.action_cancel))){
                dialog.dismiss();
            }
        });
        builder.show();
    }


    /**
     * <h1>takePicFromCamera</h1>
     * <p>
     * This method is got called, when user chooses to take photos from camera.
     * </p>
     */
    private void takePicFromCamera()
    {
        checkForVersion(CAMERA_PIC,null);
    }

    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param newFile image file to be cropped
     */
    public File startCropImage(File newFile)
    {
        Utility.printLog("profile fragment CROP IMAGE CALLED: "+ this.newFile.getPath());
        Utility.printLog("profile fragment CROP IMAGE CALLEDd: "+ newFile);
        Intent intent = new Intent(mcontext,CropImage.class );
        intent.putExtra(CropImage.IMAGE_PATH, this.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        if(fragment!=null)
            fragment.startActivityForResult(intent, Constants.CROP_IMAGE);
        else
            mcontext.startActivityForResult(intent, Constants.CROP_IMAGE);
        return newFile;
    }

    /**
     * <h2>gallery</h2>
     * <p>
     * This method is got called, when user chooses to take photos from camera.
     * </p>
     * @param data uri data given by gallery
     */
    public void gallery(Uri data)
    {
        checkForVersion(GALLERY_PIC, data);
    }

    /**
     * <h2>checkForVersion</h2>
     * used to create the file in external storage
     */
    public void checkForVersion(int type,Uri data)
    {
        takenNewImage = "takenNewImage"+String.valueOf(System.nanoTime())+".png";
        if( Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
        {
            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED )
            {
                ActivityCompat.requestPermissions(mcontext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        OREO_PERMISSION);
            }
            else
            {
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                newFile = new File(path, Constants.PARENT_FOLDER +".jpg");
                try
                {
                    // Make sure the Pictures directory exists.
                    path.mkdirs();
                    newFile.createNewFile();
                    createFile(type,data);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
                newFile = new File(Environment.getExternalStorageDirectory()+"/"+ Constants.PARENT_FOLDER+"/Profile_Pictures/",takenNewImage);
            else
                newFile = new File(mcontext.getFilesDir()+"/"+ Constants.PARENT_FOLDER+"/Profile_Pictures/",takenNewImage);
            createFile(type,data);
        }
    }

    /**
     * <h2>createFile</h2>
     * create file
     * @param type type
     */
    private void createFile(int type,Uri data)
    {
        Uri newProfileImageUri;
        switch (type)
        {
            case CAMERA_PIC:
                Utility.printLog(TAG+" package name "+mcontext.getPackageName());
                if(Build.VERSION.SDK_INT>=N)
                    newProfileImageUri = FileProvider.getUriForFile(mcontext, mcontext.getPackageName(),newFile);
                else
                    newProfileImageUri = Uri.fromFile(newFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
                intent.putExtra("return-data", true);
                if(fragment!=null)
                    fragment.startActivityForResult(intent, CAMERA_PIC);
                else
                    mcontext.startActivityForResult(intent, CAMERA_PIC);
                break;

            case GALLERY_PIC:
                try
                {
                    InputStream inputStream = mcontext.getContentResolver().openInputStream(data);
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(newFile);
                }
                catch (Exception e)
                {
                    Utility.printLog(TAG+" exception "+e);
                }
                break;
        }
    }
}
