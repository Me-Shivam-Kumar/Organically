package com.organically.organically;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.organically.organically.adapter.SearchFarmersProducerAdapter;
import com.organically.organically.model.SearchFarmersProducersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProducerOrFarmerFragment extends Fragment {
    RecyclerView searchFarmersProducersRecyclerview;
    FirebaseFirestore farmersProducersDB;
    List<SearchFarmersProducersModel> searchFarmersProducersModelList;
    SearchView searchViewFindFarmersProducer;
    SearchFarmersProducerAdapter adapter;
    LoadingDialog2 loadingDialog2;


    public SearchProducerOrFarmerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_producer_or_farmer, container, false);
        searchFarmersProducersRecyclerview=view.findViewById(R.id.search_farmers_producer_recyclerview);
        searchViewFindFarmersProducer=view.findViewById(R.id.searchView_findFarmersProducers);

        searchViewFindFarmersProducer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchFarmersProducersRecyclerview.setLayoutManager(layoutManager);
        searchFarmersProducersModelList=new ArrayList<SearchFarmersProducersModel>();

        loadingDialog2=new LoadingDialog2(getActivity());

        farmersProducersDB=FirebaseFirestore.getInstance();
        loadingDialog2.showDialog();

        farmersProducersDB.collectionGroup("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        if(!TextUtils.isEmpty(snapshot.getString("offerings"))){
                            searchFarmersProducersModelList.add(new SearchFarmersProducersModel(snapshot.getString("name"),snapshot.getString("address"),snapshot.getString("offerings"),snapshot.getString("profilePic"),snapshot.getString("aadharCardNumber")));
                        }else{
                            continue;

                        }

                    }
                    adapter=new SearchFarmersProducerAdapter(searchFarmersProducersModelList);
                    adapter.notifyDataSetChanged();
                    searchFarmersProducersRecyclerview.setAdapter(adapter);
                    loadingDialog2.hideDialog();
                }else{
                    Toast.makeText(getContext(),"Unsuccesfull",Toast.LENGTH_SHORT).show();
                    loadingDialog2.hideDialog();
                }


            }

        });
        return view;


    }

}