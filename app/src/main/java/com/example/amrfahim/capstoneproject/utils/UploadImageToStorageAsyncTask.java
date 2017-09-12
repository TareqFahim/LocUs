package com.example.amrfahim.capstoneproject.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.ui.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Amr Fahim on 9/12/2017.
 */

public class UploadImageToStorageAsyncTask extends AsyncTask<StorageReference, Void, Void> {

    private UploadTask uploadTask;
    private byte[] imgByteArray;
    private String imgDownloadUrl;
    private Callback mCallback;

    public UploadImageToStorageAsyncTask(byte[] imgByteArray, Callback mCallback){
        this.imgByteArray = imgByteArray;
        this.mCallback = mCallback;
    }

    @Override
    protected Void doInBackground(StorageReference... params) {
        uploadTask = params[0].putBytes(imgByteArray);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imgDownloadUrl = downloadUrl.toString();
                mCallback.asyncCallback(imgDownloadUrl);
            }
        });
    }

    public interface Callback{
        void asyncCallback(String imgDownloadUrl);
    }
}
