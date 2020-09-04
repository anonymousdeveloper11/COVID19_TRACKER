package com.example.covid19tracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StatsFragment extends Fragment {
    private static final String STATS_URL= "https://api.covid19api.com/summary";


    Context context;
    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton sortBtn;
    private RecyclerView statsRv;

    ArrayList<ModelStat> statList;
    AdapterStat adapterStat;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        searchEt =view.findViewById(R.id.searchEt);
        sortBtn = view.findViewById(R.id.sortBtn);
        statsRv = view.findViewById(R.id.statsRv);
        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        loadStatsData();

        //popup menu to show  sorting options
        final PopupMenu popupMenu = new PopupMenu(context,sortBtn);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Ascending");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Descending");//1st parm is id of item, 2nd parm is position in list of items and third parm is title

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //handle item clicks
                int id = item.getItemId();
                if(id==0){
                    Collections.sort(statList, new SortStatCountryAsc());
                    adapterStat.notifyDataSetChanged();
                }else if(id==1){
                    Collections.sort(statList, new SortStatCountryDec());
                    adapterStat.notifyDataSetChanged();
                }
                return false;
            }
        });



        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //called as and when user type or remove letter
                try {
                    adapterStat.getFilter().filter(charSequence);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenu.show();
            }
        });
        return  view;




    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatsData();

    }

    private  void loadStatsData(){
        progressBar.setVisibility(View.VISIBLE);

        //api call
        StringRequest  stringRequest = new StringRequest(Request.Method.GET, STATS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //get response
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //failed getting response
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        //add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void handleResponse(String response) {
        statList = new ArrayList<>();

        statList.clear();

        try {
            //we have json object as response
            JSONObject jsonObject = new JSONObject(response);
            //add then we have array of records
            JSONArray jsonArray = jsonObject.getJSONArray("Countries");
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyy hh:mm a");
            Gson gson = gsonBuilder.create();

            //start getting data
            for(int i=0; i<jsonArray.length(); i++){
                ModelStat modelStat = gson.fromJson(jsonArray.getJSONObject(i).toString(),ModelStat.class);
                statList.add(modelStat);

            }

            //setup adapter
            adapterStat = new AdapterStat(context,statList);
            statsRv.setAdapter(adapterStat);//set adapter to recyclerView

            progressBar.setVisibility(View.GONE);
        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //sort countries as ascending orders
    public  class SortStatCountryAsc implements Comparator<ModelStat>{

        @Override
        public int compare(ModelStat left, ModelStat right) {
            return left.getCountry().compareTo(right.getCountry());
        }
    }

    //sort countries as descending orders
    public  class SortStatCountryDec implements  Comparator<ModelStat>{

        @Override
        public int compare(ModelStat left, ModelStat right) {
            return right.getCountry().compareTo(left.getCountry());
        }
    }
}