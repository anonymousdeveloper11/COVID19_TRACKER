package com.example.covid19tracker;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterStat extends Filter {

    private AdapterStat adapterStat;
    private ArrayList<ModelStat> filterList;

    public FilterStat(AdapterStat adapterStat, ArrayList<ModelStat> filterList) {
        this.adapterStat = adapterStat;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //heck constraint validity
        if(constraint!=null & constraint.length()>0){
            //change the upper case
            constraint = constraint.toString().toUpperCase();
            //store our filter record
            ArrayList<ModelStat> filterModels =new ArrayList<>();
            for(int i =0; i<filterList.size(); i++){
                if(filterList.get(i).getCountry().toUpperCase().contains(constraint)){
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;

        }else {
            results.count= filterList.size();
            results.values = filterList;
        }
        return results;//don't forgot for return
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapterStat.statList = (ArrayList<ModelStat>) results.values;

        //refresh list
        adapterStat.notifyDataSetChanged();

    }
}
