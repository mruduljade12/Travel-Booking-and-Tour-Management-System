package com.mrudul.tourandtravel.user.user_fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * GoapMapView — SmartTour360 Live Tracking (Canvas-drawn Goa map)
 * ════════════════════════════════════════════════════════════════
 * Fully offline. NO Google Maps SDK. NO API key. NO internet.
 *
 * Draws (fraction-based coordinates — scales to any screen size):
 *   1.  Sea background (#79CBEA)
 *   2.  Goa land mass polygon (#B8E1B1) + inland highlight (#C8EAC0)
 *   3.  Rivers (Mandovi, Zuari) — blue strokes
 *   4.  Roads: NH-66 yellow, NH-748 yellow, minor white
 *   5.  Blue route glow + solid: YourLocation → Driver → Baga Beach
 *   6.  Road number badges: 66, 748
 *   7.  "Goa" bold region label
 *   8.  Landmark pills: Calangute, Candolim, Sinquerim Fort, Aguada Fort, Old Goa
 *   9.  Baga Beach — red destination pin + label
 *  10.  Driver marker — blue circle, car icon, "Driver" label
 *  11.  Your Location — white ring + blue dot + teal "Your Location" label
 */
public class GoapMapView extends View {

    // ── Paint declarations ─────────────────────────────────────────────────
    private final Paint pSea          = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLand         = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pInland       = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRiver        = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRoadMajor    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRoadMinor    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRouteGlow    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRoute        = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pPin          = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pPinWhite     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pDriverCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pDriverInner  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLocOuter     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLocInner     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLabelBg      = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLabelText    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pBadgeFill    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pBadgeText    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pRegion       = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pDrvLabelBg   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pDrvLabelTxt  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLocLabelBg   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLocLabelTxt  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLandmarkIcon = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLandmarkIcIn = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pShadow       = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pCar          = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float W, H;

    public GoapMapView(Context c)                            { super(c);         init(); }
    public GoapMapView(Context c, AttributeSet a)            { super(c, a);      init(); }
    public GoapMapView(Context c, AttributeSet a, int d)     { super(c, a, d);   init(); }

    private void init() {
        // Sea — reference: #79CBEA
        pSea.setColor(Color.parseColor("#79CBEA"));
        pSea.setStyle(Paint.Style.FILL);

        // Land — reference: #B8E1B1
        pLand.setColor(Color.parseColor("#B8E1B1"));
        pLand.setStyle(Paint.Style.FILL);

        // Inland lighter green
        pInland.setColor(Color.parseColor("#C8EAC0"));
        pInland.setStyle(Paint.Style.FILL);

        // River (blue-grey)
        pRiver.setColor(Color.parseColor("#6DBCD9"));
        pRiver.setStyle(Paint.Style.STROKE);
        pRiver.setStrokeCap(Paint.Cap.ROUND);

        // Major road yellow (#F5C842)
        pRoadMajor.setColor(Color.parseColor("#F5C842"));
        pRoadMajor.setStyle(Paint.Style.STROKE);
        pRoadMajor.setStrokeCap(Paint.Cap.ROUND);
        pRoadMajor.setStrokeJoin(Paint.Join.ROUND);

        // Minor road white
        pRoadMinor.setColor(Color.WHITE);
        pRoadMinor.setStyle(Paint.Style.STROKE);
        pRoadMinor.setStrokeCap(Paint.Cap.ROUND);
        pRoadMinor.setAlpha(210);

        // Route glow — reference route blue #1687F8 semi-transparent
        pRouteGlow.setColor(Color.parseColor("#1687F8"));
        pRouteGlow.setAlpha(55);
        pRouteGlow.setStyle(Paint.Style.STROKE);
        pRouteGlow.setStrokeCap(Paint.Cap.ROUND);
        pRouteGlow.setStrokeJoin(Paint.Join.ROUND);

        // Route solid
        pRoute.setColor(Color.parseColor("#1687F8"));
        pRoute.setStyle(Paint.Style.STROKE);
        pRoute.setStrokeCap(Paint.Cap.ROUND);
        pRoute.setStrokeJoin(Paint.Join.ROUND);

        // Red destination pin
        pPin.setColor(Color.parseColor("#E53935"));
        pPin.setStyle(Paint.Style.FILL);

        pPinWhite.setColor(Color.WHITE);
        pPinWhite.setStyle(Paint.Style.FILL);

        // Driver marker — primary blue #087FF5
        pDriverCircle.setColor(Color.parseColor("#087FF5"));
        pDriverCircle.setStyle(Paint.Style.FILL);

        pDriverInner.setColor(Color.WHITE);
        pDriverInner.setStyle(Paint.Style.FILL);

        // Location dot
        pLocOuter.setColor(Color.WHITE);
        pLocOuter.setStyle(Paint.Style.FILL);

        pLocInner.setColor(Color.parseColor("#087FF5"));
        pLocInner.setStyle(Paint.Style.FILL);

        // Landmark pill bg — light blue-white
        pLabelBg.setColor(Color.parseColor("#EDF5FF"));
        pLabelBg.setStyle(Paint.Style.FILL);

        pLabelText.setColor(Color.parseColor("#0B3B78"));
        pLabelText.setTextAlign(Paint.Align.CENTER);
        pLabelText.setTypeface(Typeface.DEFAULT_BOLD);

        // Road badge
        pBadgeFill.setColor(Color.parseColor("#F5C842"));
        pBadgeFill.setStyle(Paint.Style.FILL);

        pBadgeText.setColor(Color.parseColor("#2D2000"));
        pBadgeText.setTextAlign(Paint.Align.CENTER);
        pBadgeText.setTypeface(Typeface.DEFAULT_BOLD);

        // "Goa" region label
        pRegion.setColor(Color.parseColor("#1A3A5C"));
        pRegion.setTextAlign(Paint.Align.CENTER);
        pRegion.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        pRegion.setAlpha(170);

        // Driver label pill
        pDrvLabelBg.setColor(Color.WHITE);
        pDrvLabelBg.setStyle(Paint.Style.FILL);

        pDrvLabelTxt.setColor(Color.parseColor("#087FF5"));
        pDrvLabelTxt.setTextAlign(Paint.Align.CENTER);
        pDrvLabelTxt.setTypeface(Typeface.DEFAULT_BOLD);

        // Your Location pill — teal #0D9488
        pLocLabelBg.setColor(Color.parseColor("#0D9488"));
        pLocLabelBg.setStyle(Paint.Style.FILL);

        pLocLabelTxt.setColor(Color.WHITE);
        pLocLabelTxt.setTextAlign(Paint.Align.CENTER);
        pLocLabelTxt.setTypeface(Typeface.DEFAULT_BOLD);

        // Landmark icon
        pLandmarkIcon.setColor(Color.parseColor("#0D9488"));
        pLandmarkIcon.setStyle(Paint.Style.FILL);

        pLandmarkIcIn.setColor(Color.WHITE);
        pLandmarkIcIn.setStyle(Paint.Style.FILL);

        // Drop shadow
        pShadow.setColor(Color.parseColor("#20000000"));
        pShadow.setStyle(Paint.Style.FILL);

        // Car body paint
        pCar.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        W = w; H = h;
        float ts = W * 0.032f;
        pLabelText.setTextSize(ts);
        pBadgeText.setTextSize(ts * 0.78f);
        pRegion.setTextSize(W * 0.062f);
        pDrvLabelTxt.setTextSize(ts);
        pLocLabelTxt.setTextSize(ts);
        // Stroke widths relative to screen width
        pRiver.setStrokeWidth(W * 0.013f);
        pRoadMajor.setStrokeWidth(W * 0.017f);
        pRoadMinor.setStrokeWidth(W * 0.009f);
        pRouteGlow.setStrokeWidth(W * 0.052f);
        pRoute.setStrokeWidth(W * 0.020f);
    }

    private float x(float f) { return f * W; }
    private float y(float f) { return f * H; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (W == 0 || H == 0) return;

        drawSea(canvas);
        drawLand(canvas);
        drawRivers(canvas);
        drawRoads(canvas);
        drawRoute(canvas);
        drawRoadBadges(canvas);
        drawRegionLabel(canvas);
        drawLandmarks(canvas);
        drawDestinationPin(canvas);
        drawDriverMarker(canvas);
        drawYourLocation(canvas);
    }

    // ── 1. Sea ───────────────────────────────────────────────────────────────
    private void drawSea(Canvas canvas) {
        canvas.drawRect(0, 0, W, H, pSea);
    }

    // ── 2. Land mass ─────────────────────────────────────────────────────────
    private void drawLand(Canvas canvas) {
        Path land = new Path();
        land.moveTo(x(0.27f), y(0.00f));
        land.lineTo(x(1.00f), y(0.00f));
        land.lineTo(x(1.00f), y(1.00f));
        land.lineTo(x(0.25f), y(1.00f));
        land.lineTo(x(0.23f), y(0.89f));
        land.lineTo(x(0.19f), y(0.83f));
        land.lineTo(x(0.21f), y(0.75f));
        land.lineTo(x(0.17f), y(0.69f));
        land.lineTo(x(0.19f), y(0.61f));
        land.lineTo(x(0.23f), y(0.56f));
        land.lineTo(x(0.21f), y(0.49f));
        land.lineTo(x(0.17f), y(0.43f));
        land.lineTo(x(0.21f), y(0.36f));
        land.lineTo(x(0.25f), y(0.29f));
        land.lineTo(x(0.23f), y(0.19f));
        land.lineTo(x(0.27f), y(0.09f));
        land.close();
        canvas.drawPath(land, pLand);

        // Inland lighter patch
        Path inland = new Path();
        inland.moveTo(x(0.58f), y(0.08f));
        inland.lineTo(x(1.00f), y(0.04f));
        inland.lineTo(x(1.00f), y(0.56f));
        inland.lineTo(x(0.63f), y(0.62f));
        inland.lineTo(x(0.53f), y(0.42f));
        inland.close();
        canvas.drawPath(inland, pInland);
    }

    // ── 3. Rivers ────────────────────────────────────────────────────────────
    private void drawRivers(Canvas canvas) {
        Path mandovi = new Path();
        mandovi.moveTo(x(0.27f), y(0.31f));
        mandovi.cubicTo(x(0.40f), y(0.29f), x(0.54f), y(0.33f), x(0.76f), y(0.30f));
        canvas.drawPath(mandovi, pRiver);

        Path zuari = new Path();
        zuari.moveTo(x(0.27f), y(0.61f));
        zuari.cubicTo(x(0.39f), y(0.57f), x(0.51f), y(0.63f), x(0.69f), y(0.59f));
        canvas.drawPath(zuari, pRiver);
    }

    // ── 4. Roads ─────────────────────────────────────────────────────────────
    private void drawRoads(Canvas canvas) {
        // NH-66 coastal highway (north–south)
        Path nh66 = new Path();
        nh66.moveTo(x(0.37f), y(0.00f));
        nh66.lineTo(x(0.36f), y(0.20f));
        nh66.lineTo(x(0.34f), y(0.42f));
        nh66.lineTo(x(0.33f), y(0.58f));
        nh66.lineTo(x(0.35f), y(0.80f));
        nh66.lineTo(x(0.34f), y(1.00f));
        canvas.drawPath(nh66, pRoadMajor);

        // NH-748 east–west
        Path nh748 = new Path();
        nh748.moveTo(x(0.35f), y(0.59f));
        nh748.cubicTo(x(0.49f), y(0.55f), x(0.64f), y(0.57f), x(0.89f), y(0.54f));
        canvas.drawPath(nh748, pRoadMajor);

        // Minor north road
        Path mn = new Path();
        mn.moveTo(x(0.31f), y(0.14f));
        mn.lineTo(x(0.49f), y(0.17f));
        mn.lineTo(x(0.59f), y(0.13f));
        canvas.drawPath(mn, pRoadMinor);

        // Minor east Old Goa road
        Path me = new Path();
        me.moveTo(x(0.54f), y(0.33f));
        me.lineTo(x(0.71f), y(0.37f));
        canvas.drawPath(me, pRoadMinor);
    }

    // ── 5. Route ─────────────────────────────────────────────────────────────
    private void drawRoute(Canvas canvas) {
        // Your Location (0.50, 0.74) → Driver (0.35, 0.42) → Baga Beach (0.24, 0.11)
        Path route = new Path();
        route.moveTo(x(0.50f), y(0.74f));
        route.cubicTo(x(0.46f), y(0.63f), x(0.41f), y(0.54f), x(0.35f), y(0.42f));
        route.cubicTo(x(0.31f), y(0.33f), x(0.27f), y(0.23f), x(0.24f), y(0.11f));
        canvas.drawPath(route, pRouteGlow);
        canvas.drawPath(route, pRoute);
    }

    // ── 6. Road badges ───────────────────────────────────────────────────────
    private void drawRoadBadges(Canvas canvas) {
        drawBadge(canvas, x(0.71f), y(0.07f), "66");
        drawBadge(canvas, x(0.77f), y(0.54f), "748");
    }

    private void drawBadge(Canvas canvas, float cx, float cy, String text) {
        float pw = W * 0.082f, ph = W * 0.052f;
        RectF r = new RectF(cx - pw/2, cy - ph/2, cx + pw/2, cy + ph/2);
        // Shadow
        canvas.drawRoundRect(new RectF(r.left+1.5f,r.top+1.5f,r.right+1.5f,r.bottom+1.5f),
            ph/2, ph/2, pShadow);
        canvas.drawRoundRect(r, ph/2, ph/2, pBadgeFill);
        canvas.drawText(text, cx, cy + pBadgeText.getTextSize() * 0.38f, pBadgeText);
    }

    // ── 7. "Goa" region label ────────────────────────────────────────────────
    private void drawRegionLabel(Canvas canvas) {
        canvas.drawText("Goa", x(0.63f), y(0.49f), pRegion);
    }

    // ── 8. Landmark pills ────────────────────────────────────────────────────
    private void drawLandmarks(Canvas canvas) {
        drawPill(canvas, x(0.30f), y(0.21f), "Calangute");
        drawPill(canvas, x(0.29f), y(0.34f), "Candolim");
        drawPill(canvas, x(0.27f), y(0.51f), "Sinquerim Fort");
        drawPill(canvas, x(0.29f), y(0.67f), "Aguada Fort");
        drawPill(canvas, x(0.61f), y(0.39f), "Old Goa");
    }

    private void drawPill(Canvas canvas, float cx, float cy, String label) {
        float iconR = W * 0.038f;
        float iconCx = cx;
        float textStartX = cx + iconR * 1.25f;

        // Teal icon circle
        canvas.drawCircle(iconCx, cy, iconR, pLandmarkIcon);
        canvas.drawCircle(iconCx, cy, iconR * 0.52f, pLandmarkIcIn);

        float textW = pLabelText.measureText(label);
        float pad = W * 0.015f;
        float textH = pLabelText.getTextSize();
        float pillH = textH + pad * 1.6f;
        float pillLeft = textStartX + pad * 0.5f;
        RectF pill = new RectF(pillLeft, cy - pillH/2, pillLeft + textW + pad*2, cy + pillH/2);

        canvas.drawRoundRect(new RectF(pill.left+1.5f,pill.top+1.5f,pill.right+1.5f,pill.bottom+1.5f),
            pillH/2, pillH/2, pShadow);
        canvas.drawRoundRect(pill, pillH/2, pillH/2, pLabelBg);
        canvas.drawText(label, pill.left + pad + textW/2, cy + textH * 0.36f, pLabelText);
    }

    // ── 9. Destination pin (Baga Beach) ──────────────────────────────────────
    private void drawDestinationPin(Canvas canvas) {
        float px = x(0.24f), py = y(0.11f);
        float pr = W * 0.058f;

        canvas.drawCircle(px, py, pr, pPin);
        canvas.drawCircle(px, py, pr * 0.36f, pPinWhite);
        // Tail
        Path tail = new Path();
        tail.moveTo(px - pr * 0.38f, py + pr * 0.72f);
        tail.lineTo(px + pr * 0.38f, py + pr * 0.72f);
        tail.lineTo(px, py + pr * 1.75f);
        tail.close();
        canvas.drawPath(tail, pPin);

        // Label
        float lx = px + pr * 1.35f;
        float labelW = pLabelText.measureText("Baga Beach");
        float lpad = W * 0.020f;
        float lh = pLabelText.getTextSize() + lpad * 1.6f;
        RectF lr = new RectF(lx, py - lh/2, lx + labelW + lpad*2, py + lh/2);
        canvas.drawRoundRect(new RectF(lr.left+1.5f,lr.top+1.5f,lr.right+1.5f,lr.bottom+1.5f),
            lh/2, lh/2, pShadow);
        canvas.drawRoundRect(lr, lh/2, lh/2, pDrvLabelBg);
        Paint pinTxt = new Paint(pLabelText);
        pinTxt.setColor(Color.parseColor("#C62828"));
        canvas.drawText("Baga Beach", lr.left + lpad + labelW/2,
            py + pinTxt.getTextSize() * 0.36f, pinTxt);
    }

    // ── 10. Driver marker ────────────────────────────────────────────────────
    private void drawDriverMarker(Canvas canvas) {
        float dx = x(0.35f), dy = y(0.42f);
        float dr = W * 0.062f;

        canvas.drawCircle(dx, dy, dr, pDriverCircle);
        canvas.drawCircle(dx, dy, dr * 0.58f, pDriverInner);

        // Car body (blue)
        pCar.setColor(Color.parseColor("#087FF5"));
        float cw = dr * 0.76f, ch = dr * 0.42f;
        RectF body = new RectF(dx-cw/2, dy-ch/2-ch*0.08f, dx+cw/2, dy+ch/2-ch*0.08f);
        canvas.drawRoundRect(body, ch*0.28f, ch*0.28f, pCar);
        RectF roof = new RectF(dx-cw*0.26f, dy-ch-ch*0.08f, dx+cw*0.26f, dy-ch/2-ch*0.08f);
        canvas.drawRoundRect(roof, ch*0.22f, ch*0.22f, pCar);
        // Wheels
        pCar.setColor(Color.parseColor("#0A3A7A"));
        float wr = ch * 0.30f;
        canvas.drawCircle(dx - cw*0.30f, dy + ch/2 - ch*0.04f, wr, pCar);
        canvas.drawCircle(dx + cw*0.30f, dy + ch/2 - ch*0.04f, wr, pCar);

        // "Driver" label pill
        float lx = dx + dr + W * 0.02f, ly = dy;
        float labelW = pDrvLabelTxt.measureText("Driver");
        float lpad = W * 0.022f;
        float lh = pDrvLabelTxt.getTextSize() + lpad * 1.4f;
        RectF lr = new RectF(lx, ly-lh/2, lx+labelW+lpad*2, ly+lh/2);
        canvas.drawRoundRect(new RectF(lr.left+1.5f,lr.top+1.5f,lr.right+1.5f,lr.bottom+1.5f),
            lh/2, lh/2, pShadow);
        canvas.drawRoundRect(lr, lh/2, lh/2, pDrvLabelBg);
        canvas.drawText("Driver", lr.left+lpad+labelW/2, ly+pDrvLabelTxt.getTextSize()*0.36f, pDrvLabelTxt);
    }

    // ── 11. Your Location marker ─────────────────────────────────────────────
    private void drawYourLocation(Canvas canvas) {
        float lx = x(0.50f), ly = y(0.74f);
        float outerR = W * 0.055f, innerR = W * 0.036f;

        canvas.drawCircle(lx, ly, outerR, pLocOuter);
        canvas.drawCircle(lx, ly, innerR, pLocInner);

        // "Your Location" pill
        float pilx = lx + outerR + W * 0.02f;
        float textW = pLocLabelTxt.measureText("Your Location");
        float lpad = W * 0.018f;
        float pillH = pLocLabelTxt.getTextSize() + lpad * 1.5f;
        RectF pill = new RectF(pilx, ly-pillH/2, pilx+textW+lpad*2, ly+pillH/2);
        canvas.drawRoundRect(new RectF(pill.left+1.5f,pill.top+1.5f,pill.right+1.5f,pill.bottom+1.5f),
            pillH/2, pillH/2, pShadow);
        canvas.drawRoundRect(pill, pillH/2, pillH/2, pLocLabelBg);
        canvas.drawText("Your Location", pill.left+lpad+textW/2,
            ly+pLocLabelTxt.getTextSize()*0.36f, pLocLabelTxt);
    }
}