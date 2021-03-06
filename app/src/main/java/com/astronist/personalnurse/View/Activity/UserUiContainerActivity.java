package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.MainActivity;
import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class UserUiContainerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CarouselView carouselView;
    private Toolbar dToolbar;
    private ImageView offer;
    private String clicked="allo", userId;
    private FirebaseAuth firebaseAuth;
    public int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
            R.drawable.image_4};
    private CardView dailyDrugCard, allopathicCard, kidsNdMomCard, medicalCard, nutritionCard;
    private RelativeLayout notifyLay;
    private TextView cartItemCount;
    private DatabaseReference cartReference;
    private ArrayList<CartList> cartListArrayList= new ArrayList<>();
    public static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui_container);


        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        getCartItemCount();
        initNavigationViewDrawer();
        carouselView.setImageListener(imageListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            carouselView.setClipToOutline(true);
        }
        carouselView.setPageCount(sampleImages.length);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, dToolbar,
                R.string.drawer_open, R.string.drawer_closed);
        drawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerToggle.setDrawerArrowDrawable(drawerToggle.getDrawerArrowDrawable());
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ////////Card click events///////////

        dailyDrugCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUiContainerActivity.this, DailyDrugsActivity.class);
                startActivity(intent);
            }
        });

        allopathicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = "allo";
                Intent intent = new Intent(UserUiContainerActivity.this, HomeopathicAndAllopathicActivity.class);
                intent.putExtra("click", clicked);
                startActivity(intent);
            }
        });

        kidsNdMomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUiContainerActivity.this, KidsAndMomsActivity.class);
                startActivity(intent);
            }
        });

        medicalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUiContainerActivity.this, MedicalAccessoriesActivity.class);
                startActivity(intent);
            }
        });
        nutritionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUiContainerActivity.this, NutritionActivity.class);
                startActivity(intent);
            }
        });
       /* offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserUiContainerActivity.this, GetPricingActivity.class);
                startActivity(intent1);
            }
        });*/
        notifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserUiContainerActivity.this, CartListActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void getCartItemCount() {

        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList").child(userId);

        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);
                    Log.d(TAG, "onDataChange: "+ cartListArrayList.size());
                    if(cartListArrayList.size()>=1){
                        cartItemCount.setVisibility(View.VISIBLE);
                        String cartCount = String.valueOf(cartListArrayList.size());
                        cartItemCount.setText(cartCount);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initNavigationViewDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch ((item.getItemId())) {

                    case R.id.prescribe_medicine:
                       Intent intent = new Intent(UserUiContainerActivity.this, GetPricingActivity.class);
                        startActivity(intent);
                        Toast.makeText(UserUiContainerActivity.this, "Settings Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile:
                        Intent profile = new Intent(UserUiContainerActivity.this, ProfileActivity.class);
                        startActivity(profile);
                       // Toast.makeText(UserUiContainerActivity.this, "FeedBack Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.order:
                        Intent order = new Intent(UserUiContainerActivity.this, UserOrderHistoryActivity.class);
                        startActivity(order);
                        //Toast.makeText(UserUiContainerActivity.this, "Admin Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.feedback:
                       /* Intent intent3 = new Intent(context, AboutUsActivity.class);
                        startActivity(intent3);*/
                        Toast.makeText(UserUiContainerActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.rate:
                       /* Intent intent3 = new Intent(context, AboutUsActivity.class);
                        startActivity(intent3);*/
                        Toast.makeText(UserUiContainerActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.about:
                       /* Intent intent3 = new Intent(context, AboutUsActivity.class);
                        startActivity(intent3);*/
                        Toast.makeText(UserUiContainerActivity.this, "About Under Construction be Patient!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.logOut:
                        firebaseAuth.signOut();
                        finish();
                        Intent intent1 = new Intent(UserUiContainerActivity.this, MainActivity.class);
                        startActivity(intent1);
                        Toast.makeText(UserUiContainerActivity.this, "Successfully Log Out", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void inItView() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationDrawer);
        dToolbar = findViewById(R.id.toolbar);
        carouselView = findViewById(R.id.carouselView);
        dailyDrugCard = findViewById(R.id.dailyDrugLayout);
        allopathicCard = findViewById(R.id.allopathicLayout);
        kidsNdMomCard = findViewById(R.id.kidsMomLayout);
        medicalCard = findViewById(R.id.medicalAccessLayout);
        nutritionCard = findViewById(R.id.nutritionLayout);
        notifyLay = findViewById(R.id.auctionNotificationAction);
        cartItemCount = findViewById(R.id.notificationCountTv);
    }
}