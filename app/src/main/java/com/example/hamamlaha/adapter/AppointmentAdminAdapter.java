package com.example.hamamlaha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.service.DatabaseService;

import java.util.List;

public class AppointmentAdminAdapter extends RecyclerView.Adapter<AppointmentAdminAdapter.AppointmentViewHolder> {

    public interface OnAppointmentActionListener {
        void onApprove(Appointment appointment);
        void onCancel(Appointment appointment);
    }

    private final Context context;
    private List<Appointment> appointments;
    private final OnAppointmentActionListener listener;

    public AppointmentAdminAdapter(Context context, List<Appointment> appointments, OnAppointmentActionListener listener) {
        this.context = context;
        this.appointments = appointments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_admin, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void updateAppointments(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCategory;
        private final TextView tvStatus;
        private final TextView tvDateTime;
        private final TextView tvOptions;
        private final Button btnApprove;
        private final Button btnCancel;
        private final ImageButton btnDelete;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvOptions = itemView.findViewById(R.id.tv_options);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Appointment appointment) {
            tvCategory.setText(appointment.getCategory() != null ? appointment.getCategory().name() : "");
            tvDateTime.setText(appointment.getDate() + " | " + appointment.getTime());
            tvOptions.setText(appointment.getStatus());

            switch (appointment.getStatus()) {
                case "APPROVED":
                    tvStatus.setText("מאושר ✓");
                    tvStatus.setBackgroundResource(R.drawable.bg_status);
                    tvStatus.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    androidx.core.content.ContextCompat.getColor(context, R.color.green_500)));
                    btnApprove.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    break;
                case "CANCELED":
                    tvStatus.setText("בוטל ✗");
                    tvStatus.setBackgroundResource(R.drawable.bg_status);
                    tvStatus.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    androidx.core.content.ContextCompat.getColor(context, R.color.red_400)));
                    btnApprove.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    break;
                default:
                    tvStatus.setText("ממתין");
                    tvStatus.setBackgroundResource(R.drawable.bg_status);
                    tvStatus.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    android.graphics.Color.parseColor("#FF9800")));
                    btnApprove.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    break;
            }

            // כפתור אישור
            btnApprove.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("אישור תור")
                        .setMessage("האם אתה בטוח שברצונך לאשר את התור?")
                        .setPositiveButton("כן, אשר", (dialog, which) -> listener.onApprove(appointment))
                        .setNegativeButton("לא", null)
                        .show();
            });

            // כפתור ביטול
            btnCancel.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("ביטול תור")
                        .setMessage("האם אתה בטוח שברצונך לבטל את התור?")
                        .setPositiveButton("כן, בטל", (dialog, which) -> listener.onCancel(appointment))
                        .setNegativeButton("לא", null)
                        .show();
            });

            // ✅ כפתור פח - מוחק את התור לגמרי מהרשימה ומהפיירבייס
            btnDelete.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("מחיקת תור")
                        .setMessage("האם אתה בטוח שברצונך למחוק את התור לגמרי?")
                        .setPositiveButton("כן, מחק", (dialog, which) -> {
                            DatabaseService.getInstance().deleteAppointment(
                                    appointment.getAppointmentId(),
                                    new DatabaseService.DatabaseCallback<Void>() {
                                        @Override
                                        public void onCompleted(Void v) {
                                            int pos = appointments.indexOf(appointment);
                                            if (pos >= 0) {
                                                appointments.remove(pos);
                                                notifyItemRemoved(pos);
                                            }
                                        }

                                        @Override
                                        public void onFailed(Exception e) {
                                        }
                                    });
                        })
                        .setNegativeButton("לא", null)
                        .show();
            });
        }
    }
}