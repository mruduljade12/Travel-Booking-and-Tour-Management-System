package com.mrudul.tourandtravel.tour_guide.tour_guide_activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;

public class TourGuideLoginActivity extends AppCompatActivity {

    AppCompatButton btnGuideLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tour_guide_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGuideLogin = findViewById(R.id.btnGuideLogin);

        btnGuideLogin.setOnClickListener(v->{
            Intent intent = new Intent(TourGuideLoginActivity.this, TourGuidePage.class);
            startActivity(intent);
            finish();
        });
    }
}