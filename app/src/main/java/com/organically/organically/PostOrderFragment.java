package com.organically.organically;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.

 */
public class PostOrderFragment extends Fragment {
    FirebaseFirestore postOrderDB;
    TextInputEditText orderTitle,orderItemName,orderedQTY,pincode;
    Spinner unitSpinner;
    TextView startDateTV,endDateTV,unit;
    Button postOrder;


    public PostOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_post_order, container, false);
        orderTitle=view.findViewById(R.id.order_title);
        orderItemName=view.findViewById(R.id.order_itemName);
        orderedQTY=view.findViewById(R.id.orderd_qty);
        pincode=view.findViewById(R.id.pincode);
        startDateTV=view.findViewById(R.id.start_date_textView);
        endDateTV=view.findViewById(R.id.end_date_textView);
        unitSpinner=view.findViewById(R.id.unit_spinner_postOrder);
        unit=view.findViewById(R.id.unit);
        postOrder=view.findViewById(R.id.post_order);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit.setText(unitSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);




        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        startDateTV.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        endDateTV.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        postOrderDB=FirebaseFirestore.getInstance();
        SharedPreferences pref=getActivity().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        String aadharNumber=pref.getString("aadharCardNumber",null);
        if(aadharNumber!=null){
            postOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> order=new HashMap<>();

                    if(TextUtils.isEmpty(orderTitle.getText().toString())){
                        Toast.makeText(getContext(),"Please Add Valid Title to your Order",Toast.LENGTH_SHORT).show();
                    }else{
                        String title=orderTitle.getText().toString();
                        order.put("title",title);
                        if(TextUtils.isEmpty(orderItemName.getText().toString())){
                            Toast.makeText(getContext(),"Empty Item Name is not Allowed",Toast.LENGTH_SHORT).show();
                        }else{
                            String itemName=orderItemName.getText().toString();
                            order.put("itemName",itemName);
                            if(TextUtils.isEmpty(orderedQTY.getText().toString())){
                                Toast.makeText(getContext(),"Please Enter Valid Order Quantity",Toast.LENGTH_SHORT).show();
                            }else{
                                String qty=orderedQTY.getText().toString();
                                String unitValue=unit.getText().toString();
                                order.put("qty",qty);
                                order.put("unitValue",unitValue);
                                if(TextUtils.isEmpty(pincode.getText().toString())){
                                    Toast.makeText(getContext(),"Please Enter Valid Pincode",Toast.LENGTH_SHORT).show();
                                }else{
                                    String areaPincode=pincode.getText().toString();
                                    order.put("areaPincode",areaPincode);
                                    String startDate=startDateTV.getText().toString();
                                    String endDate=endDateTV.getText().toString();
                                    order.put("startDate",startDate);
                                    order.put("endDate",endDate);
                                    order.put("index","0");
                                    order.put("customerID",aadharNumber);
                                    String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
                                    String orderId=date_n+orderItemName.getText().toString();
                                    order.put("orderId",orderId);
                                    postOrderDB.collection("Customers").document(aadharNumber).collection("Orders").document(date_n+orderItemName.getText().toString()).set(order, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(),"Order Added Succesfully",Toast.LENGTH_SHORT).show();
                                                orderTitle.setText("");
                                                orderItemName.setText("");
                                                orderedQTY.setText("");
                                                pincode.setText("");
                                                startDateTV.setText("Start Date");
                                                endDateTV.setText("End Date");

                                            }
                                        }
                                    });


                                }
                            }
                        }

                    }

                }
            });

        }


        return view;
    }
}