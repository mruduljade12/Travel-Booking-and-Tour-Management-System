package com.mrudul.tourandtravel.app_common_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 2000;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_splash_screen);

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
    }
}