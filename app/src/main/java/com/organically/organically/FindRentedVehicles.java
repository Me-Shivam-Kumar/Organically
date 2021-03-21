package com.organically.organically;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class FindRentedVehicles extends Fragment {
    List<RentVehicleModelClass> rentVehicleModelClassList;
    RecyclerView rentVehicelRecyclerView;
    FirebaseFirestore dB;
    String aadharId;
    RentVehicleAdapter adapter;


    public FindRentedVehicles() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_find_rented_vehicles, container, false);
        rentVehicelRecyclerView=view.findViewById(R.id.find_rented_vehicle_RV);
        rentVehicleModelClassList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rentVehicelRecyclerView.setLayoutManager(layoutManager);

        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        aadharId=pref.getString("aadharCardNumber",null );
        dB=FirebaseFirestore.getInstance();
        dB.collectionGroup("RentVehicle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        String farmerId=snapshot.getString("farmerId");
                        if(!farmerId.equals(aadharId)){
                            rentVehicleModelClassList.add(new RentVehicleModelClass(snapshot.getString("vehicleImage"),snapshot.getString("vehicleType"),snapshot.getString("vehicleCompanyName"),snapshot.getString("vehicleModel"),snapshot.getString("vehicleRent")
                                    ,snapshot.getString("farmerId"),snapshot.getString("farmerName"),"0"));

                        }else{

                        }
                    }
                    adapter=new RentVehicleAdapter(rentVehicleModelClassList);
                    rentVehicelRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });







        return view;
    }
}