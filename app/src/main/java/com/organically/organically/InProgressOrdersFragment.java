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
 */
public class InProgressOrdersFragment extends Fragment {
RecyclerView inProgressOrdersRecyclerView;
List<PostedOrdersModel> inProgressPostOrdersModelList;
FirebaseFirestore inProgressOrderDB;
LoadingDialog loadingDialog;

    public InProgressOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_in_progress_orders, container, false);

        inProgressOrdersRecyclerView=view.findViewById(R.id.in_progress_orders_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inProgressOrdersRecyclerView.setLayoutManager(linearLayoutManager);
        loadingDialog=new LoadingDialog(getActivity());

        inProgressPostOrdersModelList=new ArrayList<PostedOrdersModel>();

        inProgressOrderDB=FirebaseFirestore.getInstance();
        SharedPreferences pref=getActivity().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        String aadharNumber=pref.getString("aadharCardNumber",null);
        if(aadharNumber != null){
            loadingDialog.showDialog();
            inProgressOrderDB.collection("Customers").document(aadharNumber).collection("Orders").whereEqualTo("index","1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            inProgressPostOrdersModelList.add(new PostedOrdersModel(snapshot.getString("title"), snapshot.getString("itemName"), snapshot.getString("qty"),
                                    snapshot.getString("areaPincode"), snapshot.getString("startDate"),1,snapshot.getString("customerID"),snapshot.getString("orderId"),snapshot.getString("endDate"),snapshot.getString("unitValue")));
                        }
                        PostedOrderAdapter adapter=new PostedOrderAdapter(inProgressPostOrdersModelList);
                        adapter.notifyDataSetChanged();
                        inProgressOrdersRecyclerView.setAdapter(adapter);
                        loadingDialog.hideDialog();
                    }

                }
            } );

        }




        return view;
    }
}