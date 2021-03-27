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
import com.organically.organically.adapter.PostedOrderAdapter;
import com.organically.organically.model.PostedOrdersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass..
 */
public class OpenPostedOrdersFragment extends Fragment {
    RecyclerView openPostedOrdersRecyclerView;
    public List<PostedOrdersModel> postedOrdersModelList;
    FirebaseFirestore openPostedOrders;
    String aadharNumber;
    LoadingDialog loadingDialog;

    public OpenPostedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_open_posted_orders, container, false);

        openPostedOrdersRecyclerView=view.findViewById(R.id.open_posted_orders_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        openPostedOrdersRecyclerView.setLayoutManager(layoutManager);

        loadingDialog=new LoadingDialog(getActivity());


        postedOrdersModelList=new ArrayList<PostedOrdersModel>();

         openPostedOrders=FirebaseFirestore.getInstance();
        SharedPreferences pref=getActivity().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        aadharNumber=pref.getString("aadharCardNumber",null);
        if (aadharNumber != null) {
            loadingDialog.showDialog();
            openPostedOrders.collection("Customers").document(aadharNumber).collection("Orders").whereEqualTo("index","0").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot  : task.getResult()){
                            postedOrdersModelList.add(new PostedOrdersModel(snapshot.getString("title"), snapshot.getString("itemName"), snapshot.getString("qty"),
                                    snapshot.getString("areaPincode"), snapshot.getString("startDate"),0,snapshot.getString("customerID"),snapshot.getString("orderId"),
                                    snapshot.getString("endDate"),snapshot.getString("unitValue")));
                        }
                        PostedOrderAdapter adapter =new PostedOrderAdapter(postedOrdersModelList);
                        adapter.notifyDataSetChanged();
                        openPostedOrdersRecyclerView.setAdapter(adapter);
                        loadingDialog.hideDialog();
                    }
                }
            });

        }



        return view;
    }


}