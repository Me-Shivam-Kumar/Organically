package com.organically.organically;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostedOrdersListFragment extends Fragment {
    TextView openTV,inProgressTV;

    public PostedOrdersListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posted_orders_list, container, false);

        openTV=view.findViewById(R.id.open_TV);
        inProgressTV=view.findViewById(R.id.inProgress_TV);

        getFragmentManager().beginTransaction().replace(R.id.listAllPostedOrder_container,new OpenPostedOrdersFragment()).commit();
        openTV.setTextColor(Color.parseColor("#03A9F4"));
        openTV.setBackgroundColor(Color.parseColor("#FFFFFF"));
        inProgressTV.setTextColor(Color.parseColor("#78909C"));
        inProgressTV.setBackgroundColor(Color.parseColor("#ECEFF1"));

        openTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.listAllPostedOrder_container,new OpenPostedOrdersFragment()).commit();
                openTV.setTextColor(Color.parseColor("#03A9F4"));
                openTV.setBackgroundColor(Color.parseColor("#FFFFFF"));
                inProgressTV.setTextColor(Color.parseColor("#78909C"));
                inProgressTV.setBackgroundColor(Color.parseColor("#ECEFF1"));
            }
        });
        inProgressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.listAllPostedOrder_container,new InProgressOrdersFragment()).commit();
                inProgressTV.setTextColor(Color.parseColor("#03A9F4"));
                inProgressTV.setBackgroundColor(Color.parseColor("#FFFFFF"));
                openTV.setTextColor(Color.parseColor("#78909C"));
                openTV.setBackgroundColor(Color.parseColor("#ECEFF1"));
            }
        });






        return view;
    }

}