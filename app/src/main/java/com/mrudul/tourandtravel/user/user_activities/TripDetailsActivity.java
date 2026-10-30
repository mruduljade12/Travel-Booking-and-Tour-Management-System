package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;

/**
 * Trip Details Activity - SmartTour360
 *
 * Receives Intent extras:
 *   EXTRA_NAME       String  destination name
 *   EXTRA_IMAGE      int     drawable resource id
 *   EXTRA_RATING     float   e.g. 4.3
 *   EXTRA_DURATION   String  e.g. "1 Day"
 *   EXTRA_LOCATION   String  e.g. "Mumbai, Maharashtra"
 *   EXTRA_TIME       String  e.g. "8:00 AM"
 *   EXTRA_PRICE      String  e.g. "\u20B9 8,000"
 */
public class TripDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NAME     = "td_name";
    public static final String EXTRA_IMAGE    = "td_image";
    public static final String EXTRA_RATING   = "td_rating";
    public static final String EXTRA_DURATION = "td_duration";
    public static final String EXTRA_LOCATION = "td_location";
    public static final String EXTRA_TIME     = "td_time";
    public static final String EXTRA_PRICE    = "td_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_trip_details);

        Intent intent = getIntent();
        String name     = intent.getStringExtra(EXTRA_NAME);
        int    imageRes = intent.getIntExtra(EXTRA_IMAGE, R.drawable.ic_default_image);
        float  rating   = intent.getFloatExtra(EXTRA_RATING, 0f);
        String duration = intent.getStringExtra(EXTRA_DURATION);
        String location = intent.getStringExtra(EXTRA_LOCATION);
        String time     = intent.getStringExtra(EXTRA_TIME);
        String price    = intent.getStringExtra(EXTRA_PRICE);

        ImageView tdImage       = findViewById(R.id.tdDestImage);
        TextView  tdName        = findViewById(R.id.tdDestName);
        TextView  tdLocation    = findViewById(R.id.tdLocation);
        TextView  tdDuration    = findViewById(R.id.tdDuration);
        TextView  tdTime        = findViewById(R.id.tdTime);
        TextView  tdPrice       = findViewById(R.id.tdPrice);
        TextView  tdRating      = findViewById(R.id.tdRating);
        TextView  tdRatingBadge = findViewById(R.id.tdRatingBadge);
        Button    tdBookNow     = findViewById(R.id.tdBookNowBtn);
        ImageView tdBackBtn     = findViewById(R.id.tdBackBtn);

        tdImage.setImageResource(imageRes);
        tdName.setText(name != null ? name : "Destination");
        tdLocation.setText(location != null ? location : "");
        tdDuration.setText(duration != null ? duration : "");
        tdTime.setText(time != null ? time : "");
        tdPrice.setText(price != null ? price : "");

        String ratingStr = String.format(java.util.Locale.getDefault(), "%.1f", rating);
        tdRating.setText(ratingStr);
        tdRatingBadge.setText(ratingStr);

        tdBackBtn.setOnClickListener(v -> finish());

        final String finalName = name;
        tdBookNow.setOnClickListener(v ->
            Toast.makeText(this, "Booking " + finalName + "...", Toast.LENGTH_SHORT).show());
    }

    private void setupStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        WindowInsetsControllerCompat ctrl =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (ctrl != null) {
            ctrl.setAppearanceLightStatusBars(false);
        }
    }
}