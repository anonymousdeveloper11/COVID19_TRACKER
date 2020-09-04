package com.example.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterStat extends RecyclerView.Adapter<AdapterStat.HolderStat> implements Filterable {

    private Context context;
    public ArrayList<ModelStat> statList, filterList;
    private  FilterStat filter;

    public AdapterStat(Context context, ArrayList<ModelStat> statList) {
        this.context = context;
        this.statList = statList;
        this.filterList=statList;
    }

    @NonNull
    @Override
    public HolderStat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_stat,parent,false);
        return new HolderStat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderStat holder, int position) {

        //get data
        ModelStat modelStat = statList.get(position);

        String country = modelStat.getCountry();
        String totalConfirmed = modelStat.getTotalConfirmed();
        String newConfirmed = modelStat.getNewConfirmed();
        String totalDeaths = modelStat.getTotalDeaths();
        String newDeaths = modelStat.getNewDeaths();
        String newRecovered = modelStat.getNewRecovered();
        String totalRecovered = modelStat.getTotalRecovered();

        //set data
        holder.todayRecoveredTv.setText(newRecovered);
        holder.recoveredTv.setText(totalRecovered);
        holder.deathsTv.setText(totalDeaths);
        holder.todayDeathsTv.setText(newDeaths);
        holder.casesTv.setText(totalConfirmed);
        holder.todayCasesTv.setText(newConfirmed);
        holder.countryTv.setText(country);


    }

    @Override
    public int getItemCount() {
        return statList.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new FilterStat(this, filterList);
        }
        return filter;
    }

    ///view holder
    class HolderStat extends RecyclerView.ViewHolder{

        //init ui view row_stat
        private  TextView countryTv, casesTv,todayCasesTv,deathsTv, todayDeathsTv, recoveredTv, todayRecoveredTv;

        public HolderStat(@NonNull View itemView) {
            super(itemView);

            //init Ui views
            countryTv = itemView.findViewById(R.id.countryTv);
            casesTv = itemView.findViewById(R.id.casesTv);
            todayCasesTv = itemView.findViewById(R.id.todayCasesTv);
            deathsTv = itemView.findViewById(R.id.deathsTv);
            todayDeathsTv = itemView.findViewById(R.id.todayDeathsTv);
            recoveredTv = itemView.findViewById(R.id.recoveredTv);
            todayRecoveredTv = itemView.findViewById(R.id.todayRecoveredTv);
        }
    }
}
