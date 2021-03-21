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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
public class MyOpenToRentVehicle extends Fragment {
    String aadharId;
    List<RentVehicleModelClass> rentVehicleModelClassList;
    RecyclerView rentVehicelRecyclerView;
    FirebaseFirestore dB;
    int count=0;
    LoadingDialog loadingDialog;
    private static RentVehicleAdapter adapter;

    public MyOpenToRentVehicle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_open_to_rent_vehicle, container, false);

        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        aadharId=pref.getString("aadharCardNumber",null );

        loadingDialog=new LoadingDialog(getActivity());

        rentVehicelRecyclerView=view.findViewById(R.id.my_open_to_rent_vehicle_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rentVehicelRecyclerView.setLayoutManager(layoutManager);

        rentVehicleModelClassList=new ArrayList<RentVehicleModelClass>();
        dB=FirebaseFirestore.getInstance();

        loadingDialog.showDialog();
       dB.collection("Farmers_Producers").document(aadharId).collection("RentVehicle").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                   loadingDialog.hideDialog();
                   rentVehicleModelClassList.add(new RentVehicleModelClass
                           (snapshot.getString("vehicleImage"),
                           snapshot.getString("vehicleType"),
                           snapshot.getString("vehicleCompanyName"),
                           snapshot.getString("vehicleModel"),
                           snapshot.getString("vehicleRent"),snapshot.getString("farmerName"),
                           snapshot.getString("farmerId"),"1"));
               }
               adapter=new RentVehicleAdapter(rentVehicleModelClassList);
               if(rentVehicleModelClassList.size()==0){
                   loadingDialog.hideDialog();
               }
               rentVehicelRecyclerView.setAdapter(adapter);
               adapter.notifyDataSetChanged();
           }
       });


        return view;
    }
    public static void refreshItemRentedVehicle(int deselect,int select){
        adapter.notifyItemChanged(deselect);
        adapter.notifyItemChanged(select);
    }
}
