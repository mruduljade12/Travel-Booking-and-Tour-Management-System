package com.mrudul.tourandtravel.app_common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.tour_guide.tour_guide_activities.TourGuideLoginActivity;
import com.mrudul.tourandtravel.user.user_activities.Login;

public class ChooseRole extends AppCompatActivity {

    private CardView customer,driver,tourGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_role);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customer = findViewById(R.id.customerRole);
        driver = findViewById(R.id.driverRole);
        tourGuide = findViewById(R.id.tourGuideRole);

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code for navigate to customer page

                Intent intent = new Intent(ChooseRole.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code for navigate to driver page

                Intent intent = new Intent(ChooseRole.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        tourGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code for navigate to tour guide page

                Intent intent = new Intent(ChooseRole.this, TourGuideLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}