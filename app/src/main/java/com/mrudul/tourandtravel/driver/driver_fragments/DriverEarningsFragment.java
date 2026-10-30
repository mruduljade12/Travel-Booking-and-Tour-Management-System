package com.mrudul.tourandtravel.driver.driver_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mrudul.tourandtravel.R;

public class DriverEarningsFragment extends Fragment {

    private static final int PERIOD_TODAY = 0;
    private static final int PERIOD_WEEK  = 1;
    private static final int PERIOD_MONTH = 2;
    private static final int PERIOD_YEAR  = 3;

    private TextView toggleToday, toggleWeek, toggleMonth, toggleYear;
    private TextView tvTotalAmount, tvTotalPeriod;
    private DriverBarChartView barChart;
    private int currentPeriod = PERIOD_WEEK;

    // TODAY: Hourly 6AM-10PM -- slow morning, office peak, evening rush
    private static final float[] DATA_TODAY = {
        120f, 280f, 350f, 180f, 220f, 310f, 150f, 200f,
        320f, 380f, 450f, 520f, 480f, 400f, 300f, 180f, 90f
    };
    private static final String[] LABELS_TODAY = {
        "6A","7A","8A","9A","10A","11A","12P","1P",
        "2P","3P","4P","5P","6P","7P","8P","9P","10P"
    };
    private static final String TOTAL_TODAY     = "\u20b9 4,930";
    private static final String PERIOD_TODAY_LBL = "Today (Mon, 30 Sep)";

    // THIS WEEK: Mon-Sun -- weekday trips, Saturday tourism spike
    private static final float[] DATA_WEEK = {
        850f,   // Mon - 2 city + 1 airport drop
        1100f,  // Tue - Pune-Lonavala added
        1250f,  // Wed - 3 long trips, best weekday
        750f,   // Thu - Shorter trips
        1050f,  // Fri - Pre-weekend office and airport
        1900f,  // Sat - Pune-Mahabaleshwar + 2 city trips
        500f    // Sun - Half day rest
    };
    private static final String[] LABELS_WEEK = {
        "Mon","Tue","Wed","Thu","Fri","Sat","Sun"
    };
    private static final String TOTAL_WEEK     = "\u20b9 7,400";
    private static final String PERIOD_WEEK_LBL = "This Week (23-30 Sep)";

    // MONTH: Daily Sep -- festival season peak in week 3 (Navratri)
    private static final float[] DATA_MONTH = {
        620f,780f,850f,500f,920f,1800f,420f,   // Week 1
        750f,1050f,980f,860f,1100f,2100f,600f, // Week 2 picks up
        900f,1300f,1450f,1200f,1350f,2400f,1100f, // Week 3 Navratri PEAK
        820f,1050f,950f,780f,1000f,1700f,550f, // Week 4 post-festival
        880f,930f                              // Days 29-30
    };
    private static final String[] LABELS_MONTH;
    static {
        LABELS_MONTH = new String[30];
        for (int i = 0; i < 30; i++) LABELS_MONTH[i] = String.valueOf(i + 1);
    }
    private static final String TOTAL_MONTH     = "\u20b9 18,450";
    private static final String PERIOD_MONTH_LBL = "This Month (1-30 Sep)";

    // YEAR: Monthly -- low Jan-Mar, summer Apr-Jun, monsoon dip Jul-Aug,
    //       peak Sep-Nov (Ganesh+Navratri+Diwali), moderate Dec
    private static final float[] DATA_YEAR = {
        12500f,  // Jan - Post new year, quiet
        11800f,  // Feb - Moderate, some weekend trips
        13200f,  // Mar - Holi festival boost
        15600f,  // Apr - Summer vacation family trips start
        17800f,  // May - Peak summer, Mahabaleshwar runs
        16400f,  // Jun - Monsoon begins, slows slightly
        14200f,  // Jul - Heavy rains, fewer long trips
        14800f,  // Aug - Independence Day weekend boost
        18450f,  // Sep - Ganesh Chaturthi + tourism
        21500f,  // Oct - Navratri + Dussehra PEAK
        22800f,  // Nov - Diwali season, BEST month
        19200f   // Dec - Year-end travel
    };
    private static final String[] LABELS_YEAR = {
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    };
    private static final String TOTAL_YEAR     = "\u20b9 2,18,250";
    private static final String PERIOD_YEAR_LBL = "This Year (Jan-Dec 2024)";

    public DriverEarningsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_earnings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleToday  = view.findViewById(R.id.toggle_today);
        toggleWeek   = view.findViewById(R.id.toggle_week);
        toggleMonth  = view.findViewById(R.id.toggle_month);
        toggleYear   = view.findViewById(R.id.toggle_year);
        barChart     = view.findViewById(R.id.bar_chart);
        tvTotalAmount = view.findViewById(R.id.tv_earn_total_amount);
        tvTotalPeriod = view.findViewById(R.id.tv_earn_total_period);
        toggleToday.setOnClickListener(v -> selectPeriod(PERIOD_TODAY));
        toggleWeek.setOnClickListener(v  -> selectPeriod(PERIOD_WEEK));
        toggleMonth.setOnClickListener(v -> selectPeriod(PERIOD_MONTH));
        toggleYear.setOnClickListener(v  -> selectPeriod(PERIOD_YEAR));
        selectPeriod(PERIOD_WEEK);
    }

    private void selectPeriod(int period) {
        currentPeriod = period;
        applyToggleStyle(toggleToday, false);
        applyToggleStyle(toggleWeek,  false);
        applyToggleStyle(toggleMonth, false);
        applyToggleStyle(toggleYear,  false);
        switch (period) {
            case PERIOD_TODAY:
                applyToggleStyle(toggleToday, true);
                barChart.setData(DATA_TODAY, LABELS_TODAY);
                updateCard(TOTAL_TODAY, PERIOD_TODAY_LBL); break;
            case PERIOD_MONTH:
                applyToggleStyle(toggleMonth, true);
                barChart.setData(DATA_MONTH, LABELS_MONTH);
                updateCard(TOTAL_MONTH, PERIOD_MONTH_LBL); break;
            case PERIOD_YEAR:
                applyToggleStyle(toggleYear, true);
                barChart.setData(DATA_YEAR, LABELS_YEAR);
                updateCard(TOTAL_YEAR, PERIOD_YEAR_LBL); break;
            default:
                applyToggleStyle(toggleWeek, true);
                barChart.setData(DATA_WEEK, LABELS_WEEK);
                updateCard(TOTAL_WEEK, PERIOD_WEEK_LBL); break;
        }
    }

    private void updateCard(String amount, String period) {
        if (tvTotalAmount != null) tvTotalAmount.setText(amount);
        if (tvTotalPeriod != null) tvTotalPeriod.setText(period);
    }

    private void applyToggleStyle(TextView tv, boolean selected) {
        if (tv == null) return;
        if (selected) {
            tv.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_earn_toggle_selected));
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.earn_toggle_selected_text));
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            tv.setBackground(null);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.earn_toggle_unselected_text));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }
}