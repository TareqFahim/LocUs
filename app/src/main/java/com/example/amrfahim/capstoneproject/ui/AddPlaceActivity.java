package com.example.amrfahim.capstoneproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.utils.UploadImageToStorageAsyncTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPlaceActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();                    // Realtime Database Root
    DatabaseReference mPlacesRef ;
    DatabaseReference mAddedPlaceRef, mLocationRef, mPhoneRef, mTimeTableRef, mTimeRef, mPicRef;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @BindView(R.id.add_place_edittext)
    EditText placeEditText;
    @BindView(R.id.add_location_edittext)
    EditText locationEditText;
    @BindView(R.id.add_phone_edittext)
    EditText phoneEditText;
    @BindView(R.id.add_place_btn)
    Button addPlaceBtn;
    @BindView(R.id.img_url_tv)
    TextView imgUrlTextView;
    @BindView(R.id.select_img_btn)
    Button selectImgBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    String name, location, phone, imgUrl, imgDownloadUrl;
    Context context = this;
    Bitmap imgBitmap;
    Uri imgUri;
    byte[] imgByteArray;

    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private UploadImageToStorageAsyncTask uploadAsynTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        ButterKnife.bind(this);
        mPlacesRef = mRootRef.child(getString(R.string.firebase_database_places_ref));
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = placeEditText.getText().toString();
                location = locationEditText.getText().toString();
                phone = phoneEditText.getText().toString();
                if ((name != null && !name.equals("")) && (location != null && !location.equals("")) && (phone != null && !phone.equals(""))) {
                    // Add Place Name
                    mAddedPlaceRef = mPlacesRef.child(name);
                    // Add Phone and set phone number
                    mPhoneRef = mAddedPlaceRef.child(getString(R.string.firebase_database_phone_ref));
                    mPhoneRef.setValue(phone);
                    // Add Location and set location Name
                    mLocationRef = mAddedPlaceRef.child(getString(R.string.firebase_database_location_ref));
                    mLocationRef.setValue(location);
                    // Add Time Table
                    mTimeTableRef = mAddedPlaceRef.child(getString(R.string.firebase_database_timetable_ref));
                    for (int i = 1; i < 25; i++) {
                        mTimeRef = mTimeTableRef.child(Integer.toString(i));
                        mTimeRef.setValue(true);
                    }

                    if (imgUrl != null && !imgUrl.equals("")) {
                        String storagePath = getString(R.string.firebase_storage_root) + UUID.randomUUID() + getString(R.string.image_extension);
                        StorageReference placesImgsRef = firebaseStorage.getReference(storagePath);
                        uploadAsynTask = new UploadImageToStorageAsyncTask(imgByteArray, new UploadImageToStorageAsyncTask.Callback() {
                            @Override
                            public void asyncCallback(String imgDownloadUrl) {
                                progressBar.setVisibility(View.INVISIBLE);
                                addPlaceBtn.setEnabled(true);
                                // Add Img's Url to Database
                                mPicRef = mAddedPlaceRef.child(getString(R.string.firebase_database_pic_ref));
                                mPicRef.setValue(imgDownloadUrl);

                                Toast.makeText(context, getString(R.string.toast_image_upload_successful_message), Toast.LENGTH_SHORT).show();
                                // Return to main grid
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        uploadAsynTask.execute(placesImgsRef);

                        progressBar.setVisibility(View.VISIBLE);
                        addPlaceBtn.setEnabled(false);
                    }

                    Toast.makeText(context, getString(R.string.toast_place_added_successful_message), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, getString(R.string.toast_fill_required_info), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imgUri = data.getData();
            imgUrl = imgUri.toString();
            imgUrlTextView.setText(imgUri.toString());
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try {
                        imgBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        imgByteArray = baos.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, getString(R.string.toast_grant_permission), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
