package com.karru.util.image_upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.karru.utility.InternalStorageContentProvider;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.utility.Constants;
import com.karru.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.os.Build.VERSION_CODES.N;

/**
 * <h>ImageOperation</h>
 * Class to handle all image operations
 * @since 17/8/17.
 */

public class ImageOperation
{
    private static final String TAG = "ImageOperation";

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private String state;
    private String amazonFileName="";
    private int imageClick=1;
    private AmazonImageUpload amazons3 ;
    private File newFile;
    private File mFileTemp;
    private Activity mActivity;

    @Inject
    public ImageOperation()
    {
    }

    /**
     * <h2>initializeAmazon</h2>
     * <P>
     *    method to configure and initialize amazonS3
     * </P>
     */
    public void initializeAmazon(Activity mActivity)
    {
        this.mActivity=mActivity;
        amazons3 = AmazonImageUpload.getInstance();
        AmazonImageUpload.configureSettings(mActivity.getString(R.string.AMAZON_ACCESS_KEY_ID), mActivity.getString(R.string.AMAZON_SECRET_KEY), Regions.US_EAST_1);
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(mActivity.getString(R.string.AMAZON_ACCESS_KEY_ID), mActivity.getString(R.string.AMAZON_SECRET_KEY)));
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    /**
     * <h>doImageOperation</h>
     * <P>
     *     This mehtod is used to show the popup where we can select our images.
     * </P>
     * @param resultInterface ResultInterface: interface to return the operation call back
     */
    public void doImageOperation(ResultInterface resultInterface)
    {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {mActivity.getResources().getString(R.string.TakePhoto), mActivity.getResources().getString(R.string.ChoosefromGallery),
                    mActivity.getResources().getString(R.string.action_cancel), mActivity.getResources().getString(R.string.Remove)};
            showDialog(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {mActivity.getResources().getString(R.string.TakePhoto), mActivity.getResources().getString(R.string.ChoosefromGallery),
                    mActivity.getResources().getString(R.string.action_cancel)};
            showDialog(100, options, resultInterface);
        }
    }

    /**
     * <h2>clearOrCreateDir</h2>
     * <p>
     *     method to create a directory if its not exist already else clear it
     * </p>
     * @param position: contains image index
     * @return: the created file
     */
    private File clearOrCreateDir(int position)
    {
        String filename;
        state = Environment.getExternalStorageState();
        if(position==-1)
        {
            filename=preferenceHelperDataSource.getUsername()+System.currentTimeMillis()+".png";
        }
        else
        {
            filename=preferenceHelperDataSource.getUsername()+"_"+position+".png";
        }

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), filename);
        }
        else
        {
            mFileTemp = new File(mActivity.getFilesDir(),filename);
        }
        return mFileTemp;
    }

    /**
     * <h2>clearOrCreateDir</h2>
     * <p>
     *     method to create a directory if its not exist already else clear it
     * </p>
     * @return: the created file
     */
    private void clearOrCreateDir() {
        try {
            state = Environment.getExternalStorageState();
            File cropImagesDir;
            File[] cropImagesDirectory;
            File profilePicsDir;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                cropImagesDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            } else {
                cropImagesDir = new File(mActivity.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(mActivity.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            }
            if (!cropImagesDir.isDirectory()) {
                cropImagesDir.mkdirs();
            } else {
                cropImagesDirectory = cropImagesDir.listFiles();
                if (cropImagesDirectory.length > 0) {
                    for (File aCropImagesDirectory : cropImagesDirectory) {
                        aCropImagesDirectory.delete();
                    }
                }
            }
            if (!profilePicsDir.isDirectory()) {
                profilePicsDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select the dialog from where we can select our image, i.e. either camera or gallery.
     * @param position
     * @param options gives option.
     * @param resultInterface ResultInterface
     */
    private void showDialog(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle( mActivity.getResources().getString(R.string.AddPhoto));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(mActivity.getResources().getString(R.string.TakePhoto))) {
                if (position == 100)
                    resultInterface.errorMandatoryNotifier();
                else
                    takePicture();
            } else if (options[item].equals(mActivity.getResources().getString(R.string.ChoosefromGallery))) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                mActivity.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC);
                mActivity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            } else if (options[item].equals(mActivity.getResources().getString(R.string.action_cancel))) {
                dialog.dismiss();
            } else if (options[item].equals(mActivity.getResources().getString(R.string.Remove))) {
                resultInterface.errorInvalidNotifier();
                imageClick = 1;
            }
        });
        builder.show();
    }

    /**
     * <h2>takePicture</h2>
     * This method will call only when it is getting called by AddShipmentActivity Class.
     */
    private void takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(),mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            mActivity.startActivityForResult(intent, Constants.CAMERA_PIC);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }

    /**
     * This method is got called, when user chooses to take photos from camera.
     * @param singleCallbackWithParam single call back
     */
    public void takePicFromCamera(SingleFileCallback singleCallbackWithParam) {
        try {
            String takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state))
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
            else
                newFile = new File(mActivity.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);

            Uri newProfileImageUri;
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(),newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            mActivity.startActivityForResult(intent, Constants.CAMERA_PIC);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>uploadToAmazon</h2>
     * This method is used to upload the image on AMAZON bucket and get the callback that it is uploaded or not.
     * @param image contains the file image.
     * @param imageOperationInterface callback.
     */
    public void uploadToAmazon(File image, String folderName, final ImageOperationInterface imageOperationInterface)
    {
        Uri mUri = Uri.fromFile(image);
        MediaInterface callbacksAmazon = new MediaInterface()
        {
            @Override
            public void onSuccessUpload(JSONObject message)
            {
                imageOperationInterface.onSuccess(amazonFileName);
                Utility.printLog(TAG+"onSuccessUpload ");
            }
            @Override
            public void onUploadError(JSONObject message)
            {
                imageOperationInterface.onFailure();
                Utility.printLog(TAG+"onUploadError "+message);
            }
            @Override
            public void onSuccessDownload(String fileName, byte[] stream, JSONObject object)
            {
            }
            @Override
            public void onDownloadFailure(JSONObject object)
            {
            }
            @Override
            public void onSuccessPreview(String fileName, byte[] stream, JSONObject object) {
            }
        };
        amazonFileName = System.currentTimeMillis()+".jpg";
        Utility.printLog("pppppp image upload in amazon " + amazonFileName);
        JSONObject message = stringToJsonAndPublish(mActivity.getString(R.string.app_name) +"/"+ amazonFileName, Uri.fromFile(image));
        amazons3.uploadMedia(mActivity, mUri, mActivity.getString(R.string.AMAZON_PICTURE_BUCKET), folderName + amazonFileName, callbacksAmazon, message);
    }

    /**
     * This method is used to convert our string into json file and then publish on amazon.
     * @param fileName contains the name of file.
     * @param uri contains the uri.
     * @return the json object.
     */
    public JSONObject stringToJsonAndPublish(String fileName, Uri uri) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "image");
            message.put("filename", fileName);
            message.put("uri", uri.toString());
            message.put("uploaded", "inprocess");
            message.put("confirm", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param file Contains the File mActivity.
     */
    public File startCropImage(File file , Activity mActivity) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(mActivity, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        mActivity.startActivityForResult(intent, Constants.CROP_IMAGE);
        return file;
    }

    /**
     * <h2>imageGallery</h2>
     * <p>
     *     method to read the image strean and save to gallery in the given file
     * </p>
     * @param data
     * @param mFileTemp
     */
    public void imageGallery(Intent data, File mFileTemp)
    {
        try {
            InputStream inputStream = mActivity.getContentResolver().openInputStream(data.getData());
            FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
            Log.d("", "inputStream" + inputStream);
            Log.d("", "fileOutputStream" + fileOutputStream);
            copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();

            startCropImage(mFileTemp ,mActivity);

        } catch (Exception e) {

            Log.d("", "Error while creating temp file", e);
        }
    }

    /**
     * <h2>copyStream</h2>
     * <p>
     *     method to copy image byte streams from inputReadBuffer to OutputReadBuffer
     * </p>
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
}
