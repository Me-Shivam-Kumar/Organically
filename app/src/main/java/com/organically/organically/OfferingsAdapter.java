package com.organically.organically;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.organically.organically.ViewAllOfferings.refreshItem;

public class OfferingsAdapter extends RecyclerView.Adapter<OfferingsAdapter.viewHolder>{
    List<OfferingsModel> offeringsModelList;
    private int preSelectedPosition=-1;
    FirebaseFirestore db,dB,db1;
    List<String> offeringNames=new ArrayList();
    String names="";
    LoadingDialog loadingDialog;

    public OfferingsAdapter(List<OfferingsModel> offeringsModelList) {
        this.offeringsModelList = offeringsModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerings_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(offeringsModelList.get(position).getOfferingName(),offeringsModelList.get(position).getOfferingPricePerUnit(),offeringsModelList.get(position).getDuration(),offeringsModelList.get(position).getTotalQty(),position,
                offeringsModelList.get(position).getStartDate(),offeringsModelList.get(position).getEndDate(),offeringsModelList.get(position).getUnit(),offeringsModelList.get(position).getRequestor(),
                offeringsModelList.get(position).getFarmerAadharCardNumber(),offeringsModelList.get(position).getCustomerAadharCardNumber());

    }

    @Override
    public int getItemCount() {
        return offeringsModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView offeringNameTV,offeringPricePerUnitTV,durationTV,totalQtyTV;
        private ImageView icon;
        private LinearLayout optionsContainer;
        private ConstraintLayout offeringConstraintsLayout;
        private TextView editOffering,removeOffering;
        Button hireBtnList;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            offeringNameTV=itemView.findViewById(R.id.offering_name);
            offeringPricePerUnitTV=itemView.findViewById(R.id.offering_price_per_unit);
            durationTV=itemView.findViewById(R.id.offering_duration);
            totalQtyTV=itemView.findViewById(R.id.offering_total_qty);
            icon=itemView.findViewById(R.id.icon);
            optionsContainer=itemView.findViewById(R.id.options_container);
            offeringConstraintsLayout=itemView.findViewById(R.id.offering_item_constraintLayout);
            editOffering=optionsContainer.findViewById(R.id.edit_offering);
            removeOffering=optionsContainer.findViewById(R.id.remove_offering);
            hireBtnList=itemView.findViewById(R.id.hire_btn);

        }
        public void setData(String name,String pricePerUnit,String time,String total_qty,int position,String startDateText,String endDateText,String unitText,int requestor,String farmerAadharCardNumber,String customerAadharCardNumber){
            offeringNameTV.setText(name);
            offeringPricePerUnitTV.setText(pricePerUnit+" Per "+unitText);
            durationTV.setText(startDateText+" to "+endDateText);
            totalQtyTV.setText(total_qty+" "+unitText);

            db=FirebaseFirestore.getInstance();
            db1=FirebaseFirestore.getInstance();
            dB=FirebaseFirestore.getInstance();


            SharedPreferences pref=itemView.getContext().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
            String aadharId=pref.getString("aadharCardNumber",null );

            final Dialog editOfferingDialog = new Dialog(itemView.getContext());
            editOfferingDialog.setContentView(R.layout.add_new_offering);
            editOfferingDialog.setCancelable(false);
            editOfferingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(requestor==0){
                icon.setVisibility(View.VISIBLE);
                hireBtnList.setVisibility(View.GONE);
            }else{
                icon.setVisibility(View.GONE);
                hireBtnList.setVisibility(View.VISIBLE);
                offeringConstraintsLayout.setClickable(false);
            }

            final Dialog hireDialogBox = new Dialog(itemView.getContext());
            hireDialogBox.setContentView(R.layout.hire_farmer_dialog);
            hireDialogBox.setCancelable(true);
            hireDialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button hireBtn=hireDialogBox.findViewById(R.id.hire_btn_dialog_box);
            EditText orderTitle = hireDialogBox.findViewById(R.id.order_title);
            EditText orderItemName = hireDialogBox.findViewById(R.id.order_itemName);
            EditText orderqty = hireDialogBox.findViewById(R.id.orderd_qty);
            EditText areaorpincode = hireDialogBox.findViewById(R.id.pincode);
            TextView startDateTv = hireDialogBox.findViewById(R.id.start_date_textView);
            TextView endDateTv = hireDialogBox.findViewById(R.id.end_date_textView);
            TextView unit = hireDialogBox.findViewById(R.id.unit);
            Spinner unitSpinner1 = hireDialogBox.findViewById(R.id.unit_spinner_postOrder);

            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            orderItemName.setText(name);

            unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unit.setText(unitSpinner1.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            startDateTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            String date = day + "/" + month + "/" + year;
                            startDateTv.setText(date);

                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });

            endDateTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            String date = day + "/" + month + "/" + year;
                            endDateTv.setText(date);

                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            hireBtnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hireDialogBox.show();
                }
            });
            hireBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> order=new HashMap<>();
                    Map<String,Object> orderPut=new HashMap<>();
                    if(!TextUtils.isEmpty(orderTitle.getText())){
                        if(!TextUtils.isEmpty(orderItemName.getText())){
                            if(!TextUtils.isEmpty(orderqty.getText())){
                                if(!TextUtils.isEmpty(areaorpincode.getText())){
                                    String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
                                    String orderId=date_n+orderItemName.getText().toString();
                                    order.put("orderId",orderId);
                                    order.put("pricePerUnitOffered",pricePerUnit);
                                    order.put("qtyOrderdByCustomer",orderqty.getText().toString());
                                    order.put("status","1");
                                    order.put("unit",unitText);
                                    order.put("itemName",orderItemName.getText().toString());
                                    order.put("duration",startDateTv.getText().toString()+" to "+endDateTv.getText().toString());
                                    order.put("customerId",customerAadharCardNumber);
                                    db1.collection("Farmers_Producers").document(farmerAadharCardNumber).collection("OrdersGot").document(customerAadharCardNumber+" "+orderId).set(order,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            hireBtn.setEnabled(false);
                                            hireBtn.setText("Hired");
                                            hireBtnList.setEnabled(false);
                                        }
                                    });
                                    orderPut.put("title",orderTitle.getText().toString());
                                    orderPut.put("itemName",orderItemName.getText().toString());
                                    orderPut.put("qty",orderqty.getText().toString());
                                    orderPut.put("unitValue",unit.getText().toString());
                                    orderPut.put("areaPincode",areaorpincode.getText().toString());
                                    orderPut.put("startDate",startDateTv.getText().toString());
                                    orderPut.put("endDate",endDateTv.getText().toString());
                                    orderPut.put("index","1");
                                    orderPut.put("customerID",customerAadharCardNumber);
                                    orderPut.put("orderId",orderId);
                                    dB.collection("Customers").document(customerAadharCardNumber).collection("Orders").document(date_n+orderItemName.getText().toString()).set(orderPut, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(itemView.getContext(),"Order Added Succesfully",Toast.LENGTH_SHORT).show();
                                                orderTitle.setText("");
                                                orderItemName.setText("");
                                                orderqty.setText("");
                                                areaorpincode.setText("");
                                                startDateTv.setText("Start Date");
                                                endDateTv.setText("End Date");
                                                hireDialogBox.dismiss();

                                            }
                                        }
                                    });




                                }
                            }
                        }
                    }
                }
            });

            TextInputEditText productName=editOfferingDialog.findViewById(R.id.product_name);
            TextInputEditText pricePerUnitTIET=editOfferingDialog.findViewById(R.id.price_per_unit);
            TextInputEditText totalProductQty=editOfferingDialog.findViewById(R.id.total_product_qty);
            Spinner unitSpinner=editOfferingDialog.findViewById(R.id.unit_spinner_postOrder);
            TextView startDateTV=editOfferingDialog.findViewById(R.id.start_date_textView);
            TextView endDateTV=editOfferingDialog.findViewById(R.id.end_date_textView);
            TextView dialogBoxTitle=editOfferingDialog.findViewById(R.id.offering_bialod_box_title);
            TextView totalQuantityUnit=editOfferingDialog.findViewById(R.id.total_quatity_unit);
            TextView pricePerUnitUnit=editOfferingDialog.findViewById(R.id.price_per_unit_unit);
            TextView cancelTextView =editOfferingDialog.findViewById(R.id.cancel_textView);
            TextView addTextView =editOfferingDialog.findViewById(R.id.add_textView);

            optionsContainer.setVisibility(View.GONE);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionsContainer.setVisibility(View.VISIBLE);
                    refreshItem(preSelectedPosition,preSelectedPosition);
                    preSelectedPosition=position;
                    editOffering.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            editOfferingDialog.show();
                            dialogBoxTitle.setText("Edit Offering");
                            addTextView.setText("Edit");
                            productName.setText(name);
                            pricePerUnitTIET.setText(pricePerUnit);
                            totalProductQty.setText(total_qty);
                            totalQuantityUnit.setText(unitText);
                            pricePerUnitUnit.setText("Per "+unitText);
                            startDateTV.setText(startDateText);
                            endDateTV.setText(endDateText);

                        }
                    });
                    unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            totalQuantityUnit.setText(unitSpinner.getSelectedItem().toString());
                            pricePerUnitUnit.setText("Per "+unitSpinner.getSelectedItem().toString());

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
                            DatePickerDialog datePickerDialog=new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
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
                            DatePickerDialog datePickerDialog=new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
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
                    addTextView.findViewById(R.id.add_textView).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Map<String,Object> offering=new HashMap<>();
                            if(TextUtils.isEmpty(productName.getText().toString())){
                                Toast.makeText(itemView.getContext(),"Empty Product Name Field not Allowed",Toast.LENGTH_SHORT).show();
                            }else{
                                String product_name=productName.getText().toString();
                                offering.put("productName",product_name);
                                if(TextUtils.isEmpty(totalProductQty.getText().toString())){
                                    Toast.makeText(itemView.getContext(),"Please Enter Toatl product Quantity",Toast.LENGTH_SHORT).show();
                                }else {
                                    String totalProuctQty=totalProductQty.getText().toString();
                                    offering.put("totalProuctQty",totalProuctQty);
                                    String unit=totalQuantityUnit.getText().toString();
                                    offering.put("totalQuantityUnit",unit);
                                    if(TextUtils.isEmpty(pricePerUnitTIET.getText().toString())){
                                        Toast.makeText(itemView.getContext(),"Please Enter price of  1"+pricePerUnitUnit.getText()+" "+ productName.getText(),Toast.LENGTH_SHORT).show();
                                    }else{
                                        String pricePer1Unit=pricePerUnitTIET.getText().toString();
                                        offering.put("pricePerUnit",pricePer1Unit);
                                        String startDateUpdated=startDateTV.getText().toString();
                                        offering.put("startDate",startDateUpdated);
                                        String endDateUpdated=endDateTV.getText().toString();
                                        offering.put("endDate",endDateUpdated);
                                        db.collection("Farmers_Producers").document(aadharId).collection("Offerings").document(productName.getText().toString()).set(offering, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    editOfferingDialog.dismiss();
                                                    optionsContainer.setVisibility(View.GONE);
                                                    offeringNameTV.setText(product_name);
                                                    offeringPricePerUnitTV.setText(pricePer1Unit+" Per "+unit);
                                                    durationTV.setText(startDateUpdated+" to "+endDateUpdated);
                                                    totalQtyTV.setText(totalProuctQty+" "+unit);


                                                }
                                            }
                                        });
                                    }
                                }
                            }



                        }
                    });

                    cancelTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editOfferingDialog.dismiss();
                        }
                    });
                    removeOffering.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            db.collection("Farmers_Producers").document(aadharId).collection("Offerings").document(name).delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                                @Override
                                public void onSuccess(Void aVoid) {
                                    offeringsModelList.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(itemView.getContext(),"Removed SuccessFully",Toast.LENGTH_SHORT).show();
                                    optionsContainer.setVisibility(View.GONE);
                                    CollectionReference offeringsRef=db.collection("Farmers_Producers").document(aadharId).collection("Offerings");
                                    offeringsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                                                String id=snapshot.getId();
                                                offeringNames.add(id);

                                            }
                                            for(int y=0;y<offeringNames.size();y++){
                                                if(y!=offeringNames.size()-1){
                                                    names+=offeringNames.get(y)+" ,";
                                                }else{
                                                    names+=offeringNames.get(y);
                                                }
                                            }
                                            Map<String,Object> offering_names=new HashMap<>();
                                            offering_names.put("offerings",names);
                                            db.collection("Farmers_Producers").document(aadharId).collection("FarmerProducerProfile").document("profile").update(offering_names).addOnCompleteListener(new OnCompleteListener<Void>(){
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(itemView.getContext(),"Added",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            });
            if(requestor==0){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition=-1;
                    }
                });
            }




        }

    }
}
