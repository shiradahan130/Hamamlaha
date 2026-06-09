package com.example.hamamlaha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Appointment> items = new ArrayList<>();

    public MyAppointmentsAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.items.addAll(appointments);
    }

    public void updateAppointments(List<Appointment> newAppointments) {
        items.clear();
        items.addAll(newAppointments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_my_appointment, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Appointment appointment = items.get(position);

        holder.tvCategory.setText(appointment.getCategory() != null
                ? appointment.getCategory().getHebrewName() : "");
        holder.tvDateTime.setText(appointment.getDate() + " | " + appointment.getTime());
        holder.tvOptions.setText(appointment.getStatus());

        switch (appointment.getStatus()) {
            case "APPROVED":
                holder.tvStatus.setText("מאושר ✓");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status);
                holder.tvStatus.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                androidx.core.content.ContextCompat.getColor(context, R.color.green_500)));
                break;
            case "CANCELED":
                holder.tvStatus.setText("בוטל ✗");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status);
                holder.tvStatus.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                androidx.core.content.ContextCompat.getColor(context, R.color.red_400)));
                break;
            default:
                holder.tvStatus.setText("ממתין לאישור");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status);
                holder.tvStatus.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                android.graphics.Color.parseColor("#FF9800")));
                break;
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvStatus, tvDateTime, tvOptions;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvStatus   = itemView.findViewById(R.id.tv_status);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvOptions  = itemView.findViewById(R.id.tv_options);
        }
    }
}