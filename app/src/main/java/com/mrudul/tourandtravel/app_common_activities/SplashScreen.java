package com.mrudul.tourandtravel.app_common_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
=======
import android.view.View;
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverRegistrationActivity;

/**
 * SmartTour360 Driver App — Splash Screen
 *
 * Entry point: user lands here on launch and taps a CTA to proceed.
 * No auto-navigation timer; all navigation is user-driven.
 */
public class SplashScreen extends AppCompatActivity {
<<<<<<< HEAD

    private static final int SPLASH_DELAY_MS = 2000;
    private final Handler handler = new Handler(Looper.getMainLooper());
=======
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_splash_screen);

<<<<<<< HEAD
        ImageView    logoIcon    = findViewById(R.id.splashLogoIcon);
        TextView     brandName   = findViewById(R.id.splashBrandName);
        TextView     tagline     = findViewById(R.id.splashTagline);
        TextView     description = findViewById(R.id.splashDescription);
        LinearLayout indicators  = findViewById(R.id.splashIndicators);

        applyTwoToneBrand(brandName);

        logoIcon.setAlpha(0f);
        brandName.setAlpha(0f);
        tagline.setAlpha(0f);
        description.setAlpha(0f);
        indicators.setAlpha(0f);

        fadeIn(logoIcon,    500,   0);
        fadeIn(brandName,   500,  50);
        fadeIn(tagline,     400, 400);
        fadeIn(description, 400, 600);
        fadeIn(indicators,  400, 700);

        handler.postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, ChooseRole.class));
            finish();
        }, SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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

    private void applyTwoToneBrand(TextView tv) {
        SpannableString s = new SpannableString("SmartTour360");
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0B3B80")),
                0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0EA5E9")),
                9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(s);
    }

    private void fadeIn(View view, int duration, int startDelay) {
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(new DecelerateInterpolator())
            .start();
=======
        // Push content clear of status bar + gesture navigation bar
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // "Let's Get Started" → Driver Login
        View btnGetStarted = findViewById(R.id.btn_get_started);
        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> openDriverLogin());
        }

        // "Driver Login" text link → Driver Login
        View tvLoginLink = findViewById(R.id.tv_driver_login_link);
        if (tvLoginLink != null) {
            tvLoginLink.setOnClickListener(v -> openDriverLogin());
        }

        // "Register" text link → Driver Registration
        View tvRegisterLink = findViewById(R.id.tv_register_link);
        if (tvRegisterLink != null) {
            tvRegisterLink.setOnClickListener(v -> openDriverRegistration());
        }
    }

    private void openDriverLogin() {
        startActivity(new Intent(this, DriverLoginActivity.class));
    }

    private void openDriverRegistration() {
        startActivity(new Intent(this, DriverRegistrationActivity.class));
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    }
}