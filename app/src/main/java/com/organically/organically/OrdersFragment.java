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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 */
public class OrdersFragment extends Fragment {
    private List<OrdersModel> ordersModelList;
    private RecyclerView  ordersRecyclerView;
    FirebaseFirestore dB;
    String aadharId;
    String customer_id;
    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_orders, container, false);

        ordersModelList=new ArrayList<OrdersModel>();

        ordersRecyclerView=view.findViewById(R.id.orders_recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        aadharId=pref.getString("aadharCardNumber",null );

        dB=FirebaseFirestore.getInstance();
        CollectionReference ordersCollection=dB.collection("Farmers_Producers").document(aadharId).collection("OrdersGot");
       ordersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for(DocumentSnapshot snapshot:task.getResult()){
                       ordersModelList.add(new OrdersModel(snapshot.getString("itemName"),snapshot.getString("qtyOrderdByCustomer"),snapshot.getString("duration"),snapshot.getString("status"),
                               snapshot.getString("unit"),snapshot.getString("customerId"),snapshot.getString("pricePerUnitOffered")));
                   }

                   OrdersAdapter adapter=new OrdersAdapter(ordersModelList);
                   ordersRecyclerView.setAdapter(adapter);
                   adapter.notifyDataSetChanged();
               }
           }
       });



        return view;
    }
}