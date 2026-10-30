package com.mrudul.tourandtravel.driver.driver_fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mrudul.tourandtravel.R;

/**
 * Lightweight custom bar-chart View for the Earnings screen.
 *
 * Supports 4 period modes via setData(float[], String[]):
 *   Today      — 24 bars (hourly: 0–23)
 *   This Week  — 7 bars  (Mon–Sun)
 *   Month      — up to 31 bars (day numbers)
 *   Year       — 12 bars (Jan–Dec)
 *
 * Call setData() to switch datasets; the view redraws automatically.
 */
public class DriverBarChartView extends View {

    // ── Default dataset: This Week ────────────────────────────
    private static final float[] DEFAULT_DATA = {
            850f, 1100f, 1250f, 750f, 1050f, 1500f, 950f
    };
    private static final String[] DEFAULT_LABELS = {
            "Mon","Tue","Wed","Thu","Fri","Sat","Sun"
    };

    private float[]  data   = DEFAULT_DATA;
    private String[] labels = DEFAULT_LABELS;
    private float    maxVal = 2000f;

    private Paint barPaint, gridPaint, labelPaint, axisLabelPaint;

    public DriverBarChartView(Context ctx) {
        super(ctx); init(ctx);
    }
    public DriverBarChartView(Context ctx, AttributeSet a) {
        super(ctx, a); init(ctx);
    }
    public DriverBarChartView(Context ctx, AttributeSet a, int d) {
        super(ctx, a, d); init(ctx);
    }

    private void init(Context ctx) {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setColor(ContextCompat.getColor(ctx, R.color.earn_chart_bar));
        barPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(ContextCompat.getColor(ctx, R.color.earn_chart_grid));
        gridPaint.setStrokeWidth(1.5f);
        gridPaint.setStyle(Paint.Style.STROKE);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(ContextCompat.getColor(ctx, R.color.earn_chart_label));
        labelPaint.setTextSize(26f);
        labelPaint.setTextAlign(Paint.Align.RIGHT);

        axisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisLabelPaint.setColor(ContextCompat.getColor(ctx, R.color.earn_chart_label));
        axisLabelPaint.setTextSize(26f);
        axisLabelPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Supply new chart data and trigger a redraw.
     *
     * @param values  Array of float values (one per bar)
     * @param barLabels  X-axis labels (same length as values)
     */
    public void setData(float[] values, String[] barLabels) {
        this.data   = values;
        this.labels = barLabels;
        // Compute dynamic max — round up to nearest 500
        float raw = 0;
        for (float v : values) if (v > raw) raw = v;
        this.maxVal = (float)(Math.ceil(raw / 500.0) * 500);
        if (this.maxVal < 500) this.maxVal = 500;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.length == 0) return;

        int w = getWidth();
        int h = getHeight();

        float leftPad  = 105f;
        float rightPad = 20f;
        float topPad   = 20f;
        float botPad   = 55f;

        float chartW = w - leftPad - rightPad;
        float chartH = h - topPad - botPad;

        // ── Grid lines + Y labels (5 horizontal lines: 0, 25%, 50%, 75%, 100%) ──
        int gridLines = 5;
        float step = maxVal / (gridLines - 1);
        for (int i = 0; i < gridLines; i++) {
            float frac = (float) i / (gridLines - 1);
            float y = topPad + (1f - frac) * chartH;
            canvas.drawLine(leftPad, y, leftPad + chartW, y, gridPaint);

            float val = step * i;
            String lbl;
            if (val >= 1000) {
                lbl = val % 1000 == 0
                        ? (int)(val/1000) + "K"
                        : (val/1000) + "K";
            } else {
                lbl = String.valueOf((int) val);
            }
            canvas.drawText(lbl, leftPad - 8f, y + labelPaint.getTextSize() / 3f, labelPaint);
        }

        // ── Bars ──
        int n = data.length;

        // Adaptive bar width: fewer bars → wider; many bars → narrower
        float slotW = chartW / n;
        float barW  = Math.min(slotW * 0.55f, 40f);
        if (n > 20) barW = Math.max(slotW * 0.7f, 6f);

        // Only draw every Nth X label to avoid crowding
        int labelStep = 1;
        if (n > 15) labelStep = 2;
        if (n > 25) labelStep = 4;

        for (int i = 0; i < n; i++) {
            float barH  = (data[i] / maxVal) * chartH;
            float cx    = leftPad + slotW * i + slotW / 2f;
            float left  = cx - barW / 2f;
            float right = cx + barW / 2f;
            float top   = topPad + chartH - barH;
            float bot   = topPad + chartH;
            float r     = barW / 4f;

            canvas.drawRoundRect(new RectF(left, top, right, bot), r, r, barPaint);

            // X label — skip some for crowded charts
            if (i % labelStep == 0) {
                canvas.drawText(labels[i],
                        cx,
                        topPad + chartH + botPad - 8f,
                        axisLabelPaint);
            }
        }
    }
}