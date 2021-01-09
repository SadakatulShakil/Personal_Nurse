package com.astronist.personalnurse.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeopathicAndAllopathicActivity extends AppCompatActivity {

    private TextView toolName;
    private ImageView previewImage, thumbNail1, thumbNail2;
    private LinearLayout thumbNail1Lay, thumbNail2Lay;
    private ExtendedFloatingActionButton proceedBtn;
    private Uri imageUri;
    private String clicked;
    private String userId;
    private StorageTask uploadTask;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference imageRef;
    private StorageReference prescripStorageReference;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeopathic_and_allopathic);
        inItView();

        imageRef = FirebaseDatabase.getInstance().getReference();
        prescripStorageReference = FirebaseStorage.getInstance().getReference("Prescription");
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
                Intent intent = new Intent(HomeopathicAndAllopathicActivity.this, WaitingOfferedPriceActivity.class);
                finish();
                startActivity(intent);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(HomeopathicAndAllopathicActivity.this, "Uploading is Pending !", Toast.LENGTH_SHORT).show();
                } else {
                    StorePrescription(clicked);
                }

            }
        });
    }

    private void StorePrescription(String clicked) {

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
    }
}