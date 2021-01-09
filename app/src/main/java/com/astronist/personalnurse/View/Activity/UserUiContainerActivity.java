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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.astronist.personalnurse.MainActivity;
import com.astronist.personalnurse.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class UserUiContainerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CarouselView carouselView;
    private Toolbar dToolbar;
    private ImageView imageView;
    private String clicked;
    private FirebaseAuth firebaseAuth;
    public int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
            R.drawable.image_4};
    private CardView homeopathicCard, allopathicCard, kidsNdMomCard, medicalCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui_container);


        inItView();
        initNavigationViewDrawer();
        firebaseAuth = FirebaseAuth.getInstance();
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

        homeopathicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = "homeo";
                Intent intent = new Intent(UserUiContainerActivity.this, HomeopathicAndAllopathicActivity.class);
                intent.putExtra("click", clicked);
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
    }

    private void initNavigationViewDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch ((item.getItemId())) {

                    case R.id.medical_news:
                       /* getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayoutID, new SettingsFragment())
                                .addToBackStack(null)
                                .commit();*/
                        Toast.makeText(UserUiContainerActivity.this, "Settings Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile:
                        /*Intent intent = new Intent(context, FeedBackActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(UserUiContainerActivity.this, "FeedBack Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.order:
                        /*Intent intent1 = new Intent(context, AdminActivity.class);
                        startActivity(intent1);*/
                        Toast.makeText(UserUiContainerActivity.this, "Admin Under Construction be Patient!", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(UserUiContainerActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.logOut:
                        firebaseAuth.signOut();
                        finish();
                        Intent intent = new Intent(UserUiContainerActivity.this, MainActivity.class);
                        startActivity(intent);
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
        homeopathicCard = findViewById(R.id.homeopathicLayout);
        allopathicCard = findViewById(R.id.allopathicLayout);
        kidsNdMomCard = findViewById(R.id.kidsMomLayout);
        medicalCard = findViewById(R.id.medicalAccessLayout);
    }
}