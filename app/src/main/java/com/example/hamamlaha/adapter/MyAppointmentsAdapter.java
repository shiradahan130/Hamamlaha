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

import java.util.List;

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.ViewHolder> {

    private final Context context;
    private List<Appointment> appointments;

    public MyAppointmentsAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.tvCategory.setText(appointment.getCategory() != null ? appointment.getCategory().getHebrewName() : "");
        holder.tvDateTime.setText(appointment.getDate() + " | " + appointment.getTime());
        holder.tvOptions.setText(appointment.getStatus());

        // צבע סטטוס
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
    public int getItemCount() {
        return appointments.size();
    }

    public void updateAppointments(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvStatus, tvDateTime, tvOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvOptions = itemView.findViewById(R.id.tv_options);
        }
    }
}