package com.mrudul.tourandtravel.user.user_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_adapters.ChatAdapter;
import com.mrudul.tourandtravel.user.user_models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * AssistantFragment — SmartTour360 AI Travel Assistant
 * ═════════════════════════════════════════════════════
 * Fully functional AI assistant UI with:
 *  • Welcome card + 6 topic shortcut cards
 *  • RecyclerView chat with separate user/bot bubble layouts
 *  • Suggestion chips (horizontally scrollable)
 *  • Message composer (EditText + Send button)
 *  • Mock AI response engine (keyword-based local responses)
 *  • Typing indicator (animated with Handler delay)
 *
 * NOTE: The AI responses are generated locally from a keyword-matching
 *       mock repository. When a real AI backend (e.g. Groq) is integrated,
 *       replace sendToMockAI() with the actual API call while keeping all
 *       UI interaction logic unchanged.
 *
 * Existing package/folder structure preserved.
 * No Groq API key in source code.
 */
public class AssistantFragment extends Fragment {

    // ── Views ─────────────────────────────────────────────────────────────────
    private RecyclerView      rvChat;
    private EditText          etPrompt;
    private FrameLayout       btnSend;
    private LinearLayout      typingIndicator;
    private NestedScrollView  scrollView;
    private LinearLayout      chipsContainer;

    // ── Data ──────────────────────────────────────────────────────────────────
    private ChatAdapter           chatAdapter;
    private ArrayList<ChatMessage> messages;

    // ── Legacy params (keep for backward compat) ─────────────────────────────
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public AssistantFragment() {}

    public static AssistantFragment newInstance(String p1, String p2) {
        AssistantFragment f = new AssistantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, p1);
        args.putString(ARG_PARAM2, p2);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ────────────────────────────────────────────────────────
        rvChat          = view.findViewById(R.id.rvChatMessages);
        etPrompt        = view.findViewById(R.id.etUserPrompt);
        btnSend         = view.findViewById(R.id.btnSendMessage);
        typingIndicator = view.findViewById(R.id.aiTypingIndicator);
        scrollView      = view.findViewById(R.id.aiScrollView);
        chipsContainer  = view.findViewById(R.id.aiChipsContainer);

        // ── Chat RecyclerView ─────────────────────────────────────────────────
        messages    = new ArrayList<>();
        chatAdapter = new ChatAdapter(requireContext(), messages);
        rvChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvChat.setAdapter(chatAdapter);

        // Add initial bot greeting
        addBotMessage("Hello! I am your AI Travel Assistant. Ask me anything about destinations, hotels, food, packing, weather, or itinerary planning!");

        // ── Send button ───────────────────────────────────────────────────────
        btnSend.setOnClickListener(v -> sendMessage());

        // IME action send
        etPrompt.setOnEditorActionListener((tv, actionId, event) -> {
            sendMessage();
            return true;
        });

        // ── Back button ───────────────────────────────────────────────────────
        view.findViewById(R.id.aiBackBtn).setOnClickListener(v ->
            requireActivity().onBackPressed());

        // ── Chat History ─────────────────────────────────────────────────────
        view.findViewById(R.id.aiHistoryBtn).setOnClickListener(v ->
            Toast.makeText(getContext(), "Chat history coming soon", Toast.LENGTH_SHORT).show());

        // ── See All topics ────────────────────────────────────────────────────
        view.findViewById(R.id.aiSeeAllBtn).setOnClickListener(v ->
            Toast.makeText(getContext(), "Showing all travel topics", Toast.LENGTH_SHORT).show());

        // ── Topic card listeners ──────────────────────────────────────────────
        view.findViewById(R.id.topicPacking).setOnClickListener(v ->
            submitQuickPrompt("What are the best packing tips for a beach holiday in Goa?"));
        view.findViewById(R.id.topicRestaurants).setOnClickListener(v ->
            submitQuickPrompt("Recommend top restaurants in Goa"));
        view.findViewById(R.id.topicWeather).setOnClickListener(v ->
            submitQuickPrompt("What is the best time to visit Goa and what is the weather like?"));
        view.findViewById(R.id.topicItinerary).setOnClickListener(v ->
            submitQuickPrompt("Suggest a 4 day itinerary for Goa"));
        view.findViewById(R.id.topicEmergency).setOnClickListener(v ->
            submitQuickPrompt("What are the emergency contacts and safety tips for Goa?"));
        view.findViewById(R.id.topicTips).setOnClickListener(v ->
            submitQuickPrompt("Give me essential travel tips for first-time visitors to Goa"));

        // ── Suggestion chips ──────────────────────────────────────────────────
        buildSuggestionChips();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Suggestion chips
    // ─────────────────────────────────────────────────────────────────────────
    private final String[] CHIP_LABELS = {
        "Show me hotels in Goa",
        "Best beaches in Goa",
        "What to pack?",
        "Suggest restaurants",
        "4-day Goa itinerary",
        "Weather forecast"
    };

    private void buildSuggestionChips() {
        if (chipsContainer == null) return;
        chipsContainer.removeAllViews();
        for (String label : CHIP_LABELS) {
            TextView chip = new TextView(requireContext());
            chip.setText(label);
            chip.setTextSize(12f);
            chip.setTextColor(android.graphics.Color.parseColor("#1688F8"));
            chip.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            chip.setBackground(requireContext().getDrawable(R.drawable.ai_chip_bg));
            int h = (int) (requireContext().getResources().getDisplayMetrics().density * 10);
            int v = (int) (requireContext().getResources().getDisplayMetrics().density * 6);
            chip.setPadding(h * 2, v, h * 2, v);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd((int)(requireContext().getResources().getDisplayMetrics().density * 8));
            chip.setLayoutParams(lp);
            chip.setClickable(true);
            chip.setFocusable(true);
            final String finalLabel = label;
            chip.setOnClickListener(v2 -> submitQuickPrompt(finalLabel));
            chipsContainer.addView(chip);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Message flow
    // ─────────────────────────────────────────────────────────────────────────
    private void sendMessage() {
        String text = etPrompt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;
        etPrompt.setText("");
        addUserMessage(text);
        showTyping(true);
        // Simulate AI thinking delay (800ms)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            showTyping(false);
            addBotMessage(getMockResponse(text));
        }, 900);
    }

    private void submitQuickPrompt(String prompt) {
        etPrompt.setText(prompt);
        sendMessage();
    }

    private void addUserMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_USER, now()));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addBotMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_BOT, now()));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void showTyping(boolean show) {
        if (typingIndicator != null)
            typingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) scrollToBottom();
    }

    private void scrollToBottom() {
        if (scrollView != null)
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String now() {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Mock AI response engine (keyword-based local responses)
    // Replace this method body with a real Groq/backend API call when ready.
    // The UI and adapter logic remains unchanged.
    // ─────────────────────────────────────────────────────────────────────────
    private String getMockResponse(String query) {
        String q = query.toLowerCase(Locale.getDefault());

        if (q.contains("itinerary") || q.contains("4 day") || q.contains("schedule") || q.contains("plan")) {
            return "Here is a 4-day Goa itinerary for you:\n\n" +
                   "Day 1 - Arrival & North Goa\n" +
                   "• Check in to your hotel in Calangute or Baga\n" +
                   "• Visit Baga Beach in the evening\n" +
                   "• Enjoy beachside shacks & nightlife\n\n" +
                   "Day 2 - North Goa Exploration\n" +
                   "• Morning water sports (optional)\n" +
                   "• Visit Anjuna Beach & Vagator\n" +
                   "• Explore Anjuna Flea Market\n\n" +
                   "Day 3 - South Goa & Heritage\n" +
                   "• Visit Old Goa churches & Basilica of Bom Jesus\n" +
                   "• Explore Panjim city & Fontainhas\n" +
                   "• Sunset cruise on the Mandovi River\n\n" +
                   "Day 4 - Leisure & Departure\n" +
                   "• Visit Colva or Benaulim Beach\n" +
                   "• Shop for spices & cashews\n" +
                   "• Depart after checkout";
        }
        if (q.contains("hotel") || q.contains("stay") || q.contains("accommodation")) {
            return "Top hotels in Goa:\n\n" +
                   "Luxury\n" +
                   "• Taj Holiday Village, Candolim - 5 star, beachfront\n" +
                   "• W Goa, Vagator - 5 star, contemporary design\n\n" +
                   "Mid-range\n" +
                   "• Lemon Tree Amarante Beach Resort, Candolim\n" +
                   "• Sterling Goa - Bardez, North Goa\n\n" +
                   "Budget\n" +
                   "• The Park Inn, Baga Beach area\n" +
                   "• OYO Rooms near Calangute\n\n" +
                   "Tip: Book at least 3 months in advance for peak season (Nov-Feb).";
        }
        if (q.contains("restaurant") || q.contains("food") || q.contains("eat") || q.contains("dining")) {
            return "Best restaurants in Goa:\n\n" +
                   "Seafood\n" +
                   "• Fisherman's Wharf, Cavelossim - authentic Goan seafood\n" +
                   "• Souza Lobo, Calangute - iconic beachside dining\n\n" +
                   "Beach shacks\n" +
                   "• Brittos, Baga Beach - family favourite since 1962\n" +
                   "• Mambos, Calangute - lively atmosphere\n\n" +
                   "Fine dining\n" +
                   "• Black Sheep Bistro, Panjim\n" +
                   "• A Reverie, Calangute\n\n" +
                   "Must-try dishes: Goan fish curry, prawn balchao, vindaloo, bebinca dessert.";
        }
        if (q.contains("pack") || q.contains("luggage") || q.contains("bring") || q.contains("carry")) {
            return "Packing checklist for Goa:\n\n" +
                   "Clothing\n" +
                   "• Light cotton clothes & breathable fabrics\n" +
                   "• Swimwear (2-3 sets)\n" +
                   "• Casual evening wear\n" +
                   "• Flip-flops + comfortable walking shoes\n\n" +
                   "Essentials\n" +
                   "• High SPF sunscreen (50+)\n" +
                   "• Sunglasses & sun hat\n" +
                   "• Insect repellent\n" +
                   "• Waterproof phone case\n\n" +
                   "Documents\n" +
                   "• Government photo ID\n" +
                   "• Hotel booking confirmations\n" +
                   "• Travel insurance details";
        }
        if (q.contains("weather") || q.contains("temperature") || q.contains("season") || q.contains("time to visit")) {
            return "Goa weather guide:\n\n" +
                   "Peak season (Nov-Feb)\n" +
                   "• Temperature: 20-32 degC\n" +
                   "• Sunny and dry - best time to visit\n\n" +
                   "Shoulder season (Oct, Mar)\n" +
                   "• Temperature: 25-35 degC\n" +
                   "• Warm, fewer crowds, lower prices\n\n" +
                   "Monsoon (Jun-Sep)\n" +
                   "• Heavy rainfall, lush greenery\n" +
                   "• Many beach shacks closed\n\n" +
                   "Recommendation: Visit November to February for the best beach and party experience.";
        }
        if (q.contains("beach") || q.contains("beaches")) {
            return "Best beaches in Goa:\n\n" +
                   "North Goa\n" +
                   "• Baga Beach - water sports & nightlife\n" +
                   "• Calangute Beach - most popular, family-friendly\n" +
                   "• Anjuna Beach - flea market & scenic cliffs\n" +
                   "• Vagator Beach - dramatic red cliffs\n\n" +
                   "South Goa\n" +
                   "• Palolem Beach - serene, crescent-shaped\n" +
                   "• Colva Beach - long, clean, quieter\n" +
                   "• Benaulim Beach - peaceful retreat\n\n" +
                   "Hidden gem: Butterfly Beach near Palolem - accessible only by boat.";
        }
        if (q.contains("emergency") || q.contains("safety") || q.contains("contact") || q.contains("help")) {
            return "Emergency contacts for Goa:\n\n" +
                   "Emergency Services\n" +
                   "• Police: 100\n" +
                   "• Ambulance: 108\n" +
                   "• Fire: 101\n" +
                   "• Tourist Helpline: 1364\n\n" +
                   "Hospitals\n" +
                   "• Goa Medical College, Panaji: 0832-2458700\n" +
                   "• Apollo Victor Hospital, Margao: 0832-2705000\n\n" +
                   "Safety tips\n" +
                   "• Do not swim at night or during rough seas\n" +
                   "• Keep valuables in hotel safe\n" +
                   "• Carry a photocopy of your ID\n" +
                   "• Avoid isolated beaches after dark";
        }
        if (q.contains("transport") || q.contains("travel") || q.contains("how to reach") || q.contains("getting")) {
            return "How to get around Goa:\n\n" +
                   "Getting there\n" +
                   "• Dabolim Airport (GOI) - 29 km from Panaji\n" +
                   "• Madgaon Railway Station - well connected\n" +
                   "• Panaji Bus Stand - interstate buses\n\n" +
                   "Local transport\n" +
                   "• Rental scooters: Rs 300-500/day (most popular)\n" +
                   "• Taxis (Goa Miles app): metered, reliable\n" +
                   "• Auto-rickshaws: short distances\n" +
                   "• Bikes: Rs 400-700/day\n\n" +
                   "Tip: A scooter is the best way to explore Goa freely. Always carry your driving licence.";
        }
        if (q.contains("tip") || q.contains("advice") || q.contains("first time") || q.contains("first-time")) {
            return "Essential travel tips for Goa:\n\n" +
                   "• Book accommodation in advance for Nov-Feb\n" +
                   "• Carry cash - many beach shacks are cash-only\n" +
                   "• Bargain at local markets (Anjuna, Mapusa)\n" +
                   "• Try the local Goan cuisine, not just North Indian food\n" +
                   "• Respect local culture at religious sites\n" +
                   "• Download offline maps before visiting remote beaches\n" +
                   "• Keep the beaches clean\n" +
                   "• Stay hydrated - it is hot and humid\n" +
                   "• Learn a few Konkani words - locals appreciate it!";
        }

        // Default response
        return "I am your AI Travel Assistant! I can help you with:\n\n" +
               "• Goa itinerary planning\n" +
               "• Hotel and resort recommendations\n" +
               "• Restaurant and food suggestions\n" +
               "• Packing tips\n" +
               "• Weather and best time to visit\n" +
               "• Beach recommendations\n" +
               "• Emergency contacts and safety\n" +
               "• Local transport tips\n\n" +
               "Try asking: \"Suggest a 4 day Goa itinerary\" or tap a topic card above!";
    }
}