package com.organically.organically;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RentVehicleFragment extends Fragment {
    TextView findRentedVehicel,rentVehicel,yourRentedVehicel;



    public RentVehicleFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rent_vehicle, container, false);
        findRentedVehicel=view.findViewById(R.id.find_rented_TV);
        rentVehicel=view.findViewById(R.id.rent_vehivel_TV);
        yourRentedVehicel=view.findViewById(R.id.your_rented_vehicle);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rent_vehicle_recyclerView,new FindRentedVehicles()).commit();
        findRentedVehicel.setTextColor(Color.parseColor("#03A9F4"));
        findRentedVehicel.setBackgroundColor(Color.parseColor("#FFFFFF"));
        rentVehicel.setTextColor(Color.parseColor("#78909C"));
        rentVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
        yourRentedVehicel.setTextColor(Color.parseColor("#78909C"));
        yourRentedVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));

        rentVehicel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.rent_vehicle_recyclerView,new RentAVehicleFragment()).commit();
                rentVehicel.setTextColor(Color.parseColor("#03A9F4"));
                rentVehicel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                findRentedVehicel.setTextColor(Color.parseColor("#78909C"));
                findRentedVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
                yourRentedVehicel.setTextColor(Color.parseColor("#78909C"));
                yourRentedVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
            }
        });
        findRentedVehicel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.rent_vehicle_recyclerView,new FindRentedVehicles()).commit();
                findRentedVehicel.setTextColor(Color.parseColor("#03A9F4"));
                findRentedVehicel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                rentVehicel.setTextColor(Color.parseColor("#78909C"));
                rentVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
                yourRentedVehicel.setTextColor(Color.parseColor("#78909C"));
                yourRentedVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
            }
        });
        yourRentedVehicel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.rent_vehicle_recyclerView,new MyOpenToRentVehicle()).commit();
                yourRentedVehicel.setTextColor(Color.parseColor("#03A9F4"));
                yourRentedVehicel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                rentVehicel.setTextColor(Color.parseColor("#78909C"));
                rentVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
                findRentedVehicel.setTextColor(Color.parseColor("#78909C"));
                findRentedVehicel.setBackgroundColor(Color.parseColor("#ECEFF1"));
            }
        });
        return view;





    }

}