package com.karru.util.image_upload;

import org.json.JSONObject;

public interface MediaInterface
{
	void onSuccessUpload(JSONObject message);
	void onUploadError(JSONObject message);
	void onSuccessDownload(String fileName, byte[] stream, JSONObject object);
	void onDownloadFailure(JSONObject object);
	void onSuccessPreview(String fileName, byte[] stream, JSONObject object);
}
