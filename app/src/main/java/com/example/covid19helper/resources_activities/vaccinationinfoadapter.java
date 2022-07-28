package com.example.covid19helper.resources_activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19helper.R;

import java.util.List;

public class vaccinationinfoadapter extends RecyclerView.Adapter<vaccinationinfoadapter.ViewHolder>{


    private LayoutInflater layoutInflater;
    private List<vaccinemodel> list_vaccine_center;

    public vaccinationinfoadapter(Context mcontext, List<vaccinemodel> list_vaccine_center) {
        this.layoutInflater = LayoutInflater.from(mcontext);
        this.list_vaccine_center = list_vaccine_center;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.vaccination_info_item,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Vaccinationcenter.setText(list_vaccine_center.get(position).getVaccinationcenter());
        holder.Vaccinationcenteraddress.setText(list_vaccine_center.get(position).getVaccinationcenteraddress());
        holder.VaccinationcenterTiming.setText(list_vaccine_center.get(position).getVaccinationTimings() + " - " +
                list_vaccine_center.get(position).getVaccinationCentertime());
        holder.VaccineName.setText(list_vaccine_center.get(position).getVaccinationName());
        holder.VaccinationcenterAvailable.setText(list_vaccine_center.get(position).getVaccineavailable());
        holder.VaccinationcenterCharges.setText(list_vaccine_center.get(position).getVaccinationCharges());
        holder.VaccinationcenterAge.setText(list_vaccine_center.get(position).getVaccinationage());


    }

    @Override
    public int getItemCount() {
        return list_vaccine_center.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Vaccinationcenter;
        TextView Vaccinationcenteraddress;
        TextView VaccinationcenterTiming;
        TextView VaccineName;
        TextView VaccinationcenterCharges;
        TextView VaccinationcenterAge;
        TextView VaccinationcenterAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            VaccinationcenterAge = itemView.findViewById(R.id.vaccinationage);
            VaccinationcenterAvailable = itemView.findViewById(R.id.isAvailable);
            VaccinationcenterCharges = itemView.findViewById(R.id.vaccineCharges);
            VaccineName = itemView.findViewById(R.id.vaccineName);
            VaccinationcenterTiming = itemView.findViewById(R.id.vaccineTimings);
            Vaccinationcenter = itemView.findViewById(R.id.vaccinationcenter);
            Vaccinationcenteraddress = itemView.findViewById(R.id.vaccinelocation);


        }
    }
}
