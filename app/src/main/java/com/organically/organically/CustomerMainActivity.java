package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.customer_fragmentContainer,new PostedOrdersListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListner=new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch(item.getItemId()){
                case R.id.list_all_postedOrders:
                    selectedFragment= new PostedOrdersListFragment();
                    break;
                case R.id.search_farmers:
                    selectedFragment= new SearchProducerOrFarmerFragment();
                    break;
                case R.id.post_order:
                    selectedFragment= new PostOrderFragment();
                    break;
                case R.id.customer_profile:
                    selectedFragment= new CustomerProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_fragmentContainer,selectedFragment).commit();
            return true;
        }
    };
}