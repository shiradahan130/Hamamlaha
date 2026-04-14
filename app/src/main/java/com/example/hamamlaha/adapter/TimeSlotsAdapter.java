package com.example.hamamlaha.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.databinding.ItemTimeSlotBinding;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder> {

    public interface OnSlotClickListener {
        void onSlotClick(String time);
    }

    private final Context context;
    private List<String> timeSlots;
    private List<String> availableSlots;
    private List<String> partialSlots; // ← חדש: פנוי אבל לא ניתן להתחיל מכאן
    private final OnSlotClickListener listener;

    public TimeSlotsAdapter(Context context, List<String> timeSlots, List<String> bookedSlots, OnSlotClickListener listener) {
        this.context = context;
        this.timeSlots = new ArrayList<>(timeSlots);
        this.availableSlots = new ArrayList<>(timeSlots); // בהתחלה הכל פנוי
        this.partialSlots = new ArrayList<>(); // ← חדש
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimeSlotBinding binding = ItemTimeSlotBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new TimeSlotViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String time = timeSlots.get(position);
        boolean isAvailable = availableSlots.contains(time);
        boolean isPartial = partialSlots.contains(time); // ← חדש
        holder.bind(time, isAvailable, isPartial); // ← עדכון
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    // עדכון עם שלושת הרשימות
    public void updateAvailableSlots(List<String> allSlots, List<String> newAvailableSlots, List<String> newPartialSlots) {
        this.timeSlots = new ArrayList<>(allSlots);
        this.availableSlots = new ArrayList<>(newAvailableSlots);
        this.partialSlots = new ArrayList<>(newPartialSlots); // ← חדש
        notifyDataSetChanged();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {

        private final ItemTimeSlotBinding binding;

        public TimeSlotViewHolder(ItemTimeSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String time, boolean isAvailable, boolean isPartial) {
            binding.tvItemTime.setText(time);

            if (isAvailable) {
                // ✅ פנוי לגמרי - וי ירוק, ניתן ללחוץ
                binding.ivStatusIcon.setImageResource(R.drawable.ic_check_circle);
                binding.ivStatusIcon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.green_500)));
                binding.getRoot().setAlpha(1.0f);
                binding.cardTimeSlot.setClickable(true);
                binding.cardTimeSlot.setFocusable(true);
                binding.cardTimeSlot.setOnClickListener(v -> listener.onSlotClick(time));

            } else if (isPartial) {
                // ⚠️ שעה פנויה אבל לא ניתן להתחיל מכאן - סימן כתום, לא ניתן ללחוץ
                binding.ivStatusIcon.setImageResource(R.drawable.ic_warning);
                binding.ivStatusIcon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.orange_400)));
                binding.getRoot().setAlpha(0.75f);
                binding.cardTimeSlot.setClickable(false);
                binding.cardTimeSlot.setFocusable(false);
                binding.cardTimeSlot.setOnClickListener(null);

            } else {
                // ❌ תפוס - איקס אדום, לא ניתן ללחוץ
                binding.ivStatusIcon.setImageResource(R.drawable.ic_cancel);
                binding.ivStatusIcon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.red_400)));
                binding.getRoot().setAlpha(0.5f);
                binding.cardTimeSlot.setClickable(false);
                binding.cardTimeSlot.setFocusable(false);
                binding.cardTimeSlot.setOnClickListener(null);
            }
        }
    }
}