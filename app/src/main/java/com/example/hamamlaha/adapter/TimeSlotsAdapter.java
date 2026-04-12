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
    private final OnSlotClickListener listener;

    public TimeSlotsAdapter(Context context, List<String> timeSlots, List<String> bookedSlots, OnSlotClickListener listener) {
        this.context = context;
        this.timeSlots = new ArrayList<>(timeSlots);
        this.availableSlots = new ArrayList<>(timeSlots); // בהתחלה הכל פנוי
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
        holder.bind(time, isAvailable);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void updateBookedSlots(List<String> newBookedSlots) {
        notifyDataSetChanged();
    }

    public void updateAvailableSlots(List<String> allSlots, List<String> newAvailableSlots) {
        this.timeSlots = new ArrayList<>(allSlots);
        this.availableSlots = new ArrayList<>(newAvailableSlots);
        notifyDataSetChanged();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {

        private final ItemTimeSlotBinding binding;

        public TimeSlotViewHolder(ItemTimeSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String time, boolean isAvailable) {
            binding.tvItemTime.setText(time);

            binding.ivStatusIcon.setImageResource(
                    isAvailable ? R.drawable.ic_check_circle : R.drawable.ic_cancel
            );

            binding.ivStatusIcon.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context,
                            isAvailable ? R.color.green_500 : R.color.red_400)
            ));

            binding.getRoot().setAlpha(isAvailable ? 1.0f : 0.5f);

            binding.cardTimeSlot.setClickable(isAvailable);
            binding.cardTimeSlot.setFocusable(isAvailable);

            binding.cardTimeSlot.setOnClickListener(v -> {
                if (isAvailable) listener.onSlotClick(time);
            });
        }
    }
}