package com.mrudul.tourandtravel.driver.driver_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.NavigateToPickupActivity;
import com.mrudul.tourandtravel.driver.driver_interfaces.DriverRequestClickListener;
import com.mrudul.tourandtravel.driver.driver_models.DriverRequestModel;

import java.util.ArrayList;

/**
 * Shared RecyclerView adapter used by:
 *   - DriverRequestsActivity  (Driver Requests screen)
 *   - DriverScheduleFragment  (Schedule screen)
 *
 * Both screens use the same DriverRequestRepository so status changes
 * made in one screen are instantly visible in the other.
 *
 * Card behaviour by status:
 *   PENDING   → shows Accept / Reject buttons
 *   ACCEPTED  → hides buttons; tap card → NavigateToPickupActivity
 *   REJECTED  → hides buttons; shows Rejected badge (not shown in All tab)
 *   COMPLETED → hides buttons; shows Completed badge
 */
public class DriverRequestAdapter extends RecyclerView.Adapter<DriverRequestAdapter.ViewHolder> {

    private final Context                       context;
    private final ArrayList<DriverRequestModel> list;
    private final DriverRequestClickListener    listener;

    public DriverRequestAdapter(Context context,
                                ArrayList<DriverRequestModel> list,
                                DriverRequestClickListener listener) {
        this.context  = context;
        this.list     = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_driver_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        DriverRequestModel req = list.get(pos);

        // ── Text fields ──────────────────────────────────────
        h.tvTime.setText(req.getTime());
        h.tvPickup.setText(req.getPickupLocation());
        h.tvDestination.setText(req.getDestination());
        h.tvSeats.setText(req.getSeats() + context.getString(R.string.req_seats_suffix));
        h.tvAmount.setText(req.getAmount());
        h.tvDistance.setText(req.getDistance());

        // ── Badge ────────────────────────────────────────────
        applyBadge(h, req.getStatus());

        // ── Action buttons: visible only for PENDING ─────────
        h.llActionButtons.setVisibility(
                req.getStatus() == DriverRequestModel.STATUS_PENDING
                        ? View.VISIBLE : View.GONE);

        // ── Card tap: ACCEPTED → Navigate to Pickup ──────────
        h.itemView.setOnClickListener(v -> {
            if (req.getStatus() == DriverRequestModel.STATUS_ACCEPTED) {
                Intent intent = new Intent(context, NavigateToPickupActivity.class);
                intent.putExtra(NavigateToPickupActivity.EXTRA_PICKUP,      req.getPickupLocation());
                intent.putExtra(NavigateToPickupActivity.EXTRA_DESTINATION, req.getDestination());
                context.startActivity(intent);
            }
        });

        // ── Accept ───────────────────────────────────────────
        h.btnAccept.setOnClickListener(v -> {
            int adPos = h.getAdapterPosition();
            if (adPos != RecyclerView.NO_ID && listener != null) {
                listener.onAcceptRequest(req, adPos);
            }
        });

        // ── Reject ───────────────────────────────────────────
        h.btnReject.setOnClickListener(v -> {
            int adPos = h.getAdapterPosition();
            if (adPos != RecyclerView.NO_ID && listener != null) {
                listener.onRejectRequest(req, adPos);
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    // ─────────────────────────────────────────────────────────
    //  Badge styling
    // ─────────────────────────────────────────────────────────

    private void applyBadge(ViewHolder h, int status) {
        switch (status) {
            case DriverRequestModel.STATUS_ACCEPTED:
                h.tvBadge.setText(R.string.req_badge_accepted);
                h.tvBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.sched_badge_accepted_text));
                h.tvBadge.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_accepted));
                break;
            case DriverRequestModel.STATUS_REJECTED:
                h.tvBadge.setText(R.string.req_badge_rejected);
                h.tvBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.sched_badge_rejected_text));
                h.tvBadge.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_rejected));
                break;
            case DriverRequestModel.STATUS_COMPLETED:
                h.tvBadge.setText(R.string.sched_tab_completed);
                h.tvBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.sched_badge_completed_text));
                h.tvBadge.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_completed));
                break;
            default: // PENDING
                h.tvBadge.setText(R.string.sched_badge_pending);
                h.tvBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.sched_badge_pending_text));
                h.tvBadge.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_pending));
                break;
        }
    }

    // ─────────────────────────────────────────────────────────
    //  ViewHolder
    // ─────────────────────────────────────────────────────────

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView        tvTime, tvBadge, tvPickup, tvDestination,
                        tvSeats, tvAmount, tvDistance;
        LinearLayout    llActionButtons;
        AppCompatButton btnAccept, btnReject;

        ViewHolder(@NonNull View view) {
            super(view);
            tvTime          = view.findViewById(R.id.tv_req_time);
            tvBadge         = view.findViewById(R.id.tv_req_badge);
            tvPickup        = view.findViewById(R.id.tv_req_pickup);
            tvDestination   = view.findViewById(R.id.tv_req_destination);
            tvSeats         = view.findViewById(R.id.tv_req_seats);
            tvAmount        = view.findViewById(R.id.tv_req_amount);
            tvDistance      = view.findViewById(R.id.tv_req_distance);
            llActionButtons = view.findViewById(R.id.ll_req_action_buttons);
            btnAccept       = view.findViewById(R.id.btn_req_accept);
            btnReject       = view.findViewById(R.id.btn_req_reject);
        }
    }
}