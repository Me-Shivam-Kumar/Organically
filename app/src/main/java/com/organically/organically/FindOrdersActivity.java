package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class  FindOrdersActivity extends AppCompatActivity {
RecyclerView findOrdersRecyclerView;
FirebaseFirestore allOrdersDB;
List<PostedOrdersModel> ordersList;
    PostedOrderAdapter adapter;
    LoadingDialog2 loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findOrdersRecyclerView=findViewById(R.id.find_orders_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(FindOrdersActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findOrdersRecyclerView.setLayoutManager(layoutManager);
        loadingDialog=new LoadingDialog2(FindOrdersActivity.this);

        ordersList=new ArrayList<PostedOrdersModel>();

        allOrdersDB=FirebaseFirestore.getInstance();
        loadingDialog.showDialog();
        allOrdersDB.collectionGroup("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    loadingDialog.hideDialog();
                    for(DocumentSnapshot snapshot:task.getResult()){
                        String index=snapshot.getString("index");
                        if(index.equals("0")){
                            ordersList.add(new PostedOrdersModel(snapshot.getString("title"), snapshot.getString("itemName"), snapshot.getString("qty"),
                                    snapshot.getString("areaPincode"), snapshot.getString("startDate"),2,snapshot.getString("customerID"),snapshot.getString("orderId"),
                                    snapshot.getString("endDate"),snapshot.getString("unitValue")));
                        }



                    }
                    adapter=new PostedOrderAdapter(ordersList);
                    adapter.notifyDataSetChanged();
                    findOrdersRecyclerView.setAdapter(adapter);
                }

            }
        });






    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_orders_search_menu,menu);
        MenuItem searchMenuItem=menu.findItem(R.id.search_orders);
        SearchView searchView= (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return super.onCreateOptionsMenu(menu);
    }
}