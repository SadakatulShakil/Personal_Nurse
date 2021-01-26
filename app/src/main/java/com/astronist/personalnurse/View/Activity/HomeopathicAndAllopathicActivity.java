package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Model.PrescriptionInfo;
import com.astronist.personalnurse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeopathicAndAllopathicActivity extends AppCompatActivity {

    private TextView toolName;
    private ImageView previewImage, thumbNail1, thumbNail2;
    private LinearLayout thumbNail1Lay, thumbNail2Lay;
    private ExtendedFloatingActionButton proceedBtn;
    private Uri imageUri;
    private String clicked, status = "unsolved";
    private StorageTask uploadTask;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private String userId, addingTime, addingDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeopathic_and_allopathic);
        inItView();

        mStorageRef = FirebaseStorage.getInstance().getReference("PrescriptionInfo");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PrescriptionInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        clicked = intent.getStringExtra("click");

        if(clicked.equals("allo")){
            toolName.setText("Allopathic");
        }else if(clicked.equals("homeo")){
            toolName.setText("Homeopathic");
        }

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorePrescription(clicked);
            }
        });
    }

    private void StorePrescription(String clicked) {

        progressBar.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        addingTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        addingDate = myDateFormat.format(calendar.getTime());

        if (imageUri != null) {
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 500);
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            //Toast.makeText(UploadProductActivity.this, "image Upload Successful!", Toast.LENGTH_SHORT).show();
                            uploadFullInfo(url.toString(), addingDate, addingTime, clicked);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(HomeopathicAndAllopathicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No file Selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadFullInfo(final String imageUri, final String addingDate, final String addingTime, final String clicked) {

        String uploadId = mDatabaseRef.push().getKey();
        PrescriptionInfo prescriptionInfo = new PrescriptionInfo(userId, uploadId, imageUri, addingDate, addingTime,clicked,status);
        mDatabaseRef.child(userId).child(uploadId).setValue(prescriptionInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(HomeopathicAndAllopathicActivity.this, "Info Upload Successful!", Toast.LENGTH_SHORT).show();
                    Intent infoProduct = new Intent(HomeopathicAndAllopathicActivity.this, WaitingOfferedPriceActivity.class);
                    infoProduct.putExtra("addingTime", addingTime);
                    infoProduct.putExtra("addingDate", addingDate);
                    startActivity(infoProduct);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });

    }


    private void OpenFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d(TAG, "onActivityResult: " +imageUri);
            Picasso.get().load(imageUri).into(previewImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                previewImage.setClipToOutline(true);
            }
            //saveImageBtn.setVisibility(View.VISIBLE);
        }
    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void inItView() {

        toolName = findViewById(R.id.toolbarName);
        previewImage = findViewById(R.id.previewImage);
        proceedBtn = findViewById(R.id.proceedBtn);
        progressBar = findViewById(R.id.progressBar);
    }
}