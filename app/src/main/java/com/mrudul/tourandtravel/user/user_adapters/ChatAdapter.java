package com.mrudul.tourandtravel.user.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_models.ChatMessage;

import java.util.ArrayList;

/**
 * ChatAdapter — drives the AI assistant conversation RecyclerView.
 * Two view types: TYPE_USER (right blue bubble), TYPE_BOT (left white bubble).
 * Lives in the existing user_adapters package.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context             ctx;
    private final ArrayList<ChatMessage> messages;

    public ChatAdapter(Context ctx, ArrayList<ChatMessage> messages) {
        this.ctx      = ctx;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        if (viewType == ChatMessage.TYPE_USER) {
            View v = inflater.inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_chat_bot, parent, false);
            return new BotViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (holder instanceof UserViewHolder) {
            UserViewHolder h = (UserViewHolder) holder;
            h.tvMsg.setText(msg.getText());
            h.tvTime.setText(msg.getTimestamp());
        } else {
            BotViewHolder h = (BotViewHolder) holder;
            h.tvMsg.setText(msg.getText());
            h.tvTime.setText(msg.getTimestamp());
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    // ── ViewHolders ──────────────────────────────────────────────────────────
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsg, tvTime;
        UserViewHolder(@NonNull View v) {
            super(v);
            tvMsg  = v.findViewById(R.id.tvUserMsg);
            tvTime = v.findViewById(R.id.tvUserTime);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsg, tvTime;
        BotViewHolder(@NonNull View v) {
            super(v);
            tvMsg  = v.findViewById(R.id.tvBotMsg);
            tvTime = v.findViewById(R.id.tvBotTime);
        }
    }
}