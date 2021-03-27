package com.organically.organically.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.organically.organically.OrderDetailsActivity;
import com.organically.organically.R;
import com.organically.organically.ViewBidsActivity;
import com.organically.organically.model.PostedOrdersModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostedOrderAdapter extends RecyclerView.Adapter<PostedOrderAdapter.viewHolder> implements Filterable {
    List<PostedOrdersModel> postedOrdersModelList;
    List<PostedOrdersModel> allOrderList;
    FirebaseFirestore bidDB, farmerBidDB, orderDB;
    String[] dates;

    public PostedOrderAdapter(List<PostedOrdersModel> postedOrdersModelList) {
        this.postedOrdersModelList = postedOrdersModelList;
        this.allOrderList = new ArrayList<PostedOrdersModel>(postedOrdersModelList);
    }

    @NonNull
    @Override
    public PostedOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_open_posted_orders, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedOrderAdapter.viewHolder holder, int position) {
        holder.setData(postedOrdersModelList.get(position).getTitle(), postedOrdersModelList.get(position).getItemName(), postedOrdersModelList.get(position).getQty(), postedOrdersModelList.get(position).getAreaPincode(), postedOrdersModelList.get(position).getStartDate(), postedOrdersModelList.get(position).getRequestor(), postedOrdersModelList.get(position).getCustomer_id(), postedOrdersModelList.get(position).getOrder_id(),postedOrdersModelList.get(position).getEndDate(),
                postedOrdersModelList.get(position).getQtyUnit(),position);


    }

    @Override
    public int getItemCount() {
        return postedOrdersModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PostedOrdersModel> filteredList = new ArrayList<PostedOrdersModel>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(allOrderList);
            } else {
                for (PostedOrdersModel order : allOrderList) {
                    if (order.getItemName().toLowerCase().contains(constraint.toString().toLowerCase()) || order.getAreaPincode().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(order);

                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            postedOrdersModelList.clear();
            postedOrdersModelList.addAll((Collection<? extends PostedOrdersModel>) results.values);
            notifyDataSetChanged();

        }
    };

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, itemName, itemQty, areaPincode, duration;
        Button viewBidsBtn, placeBid, editOrder,viewOrderDetails;
        ImageView deleteOrderBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.posted_orders_title);
            itemName = itemView.findViewById(R.id.item_name_postedOrders);
            itemQty = itemView.findViewById(R.id.qty_postedOrders);
            areaPincode = itemView.findViewById(R.id.areaPincode_postedOrders);
            duration = itemView.findViewById(R.id.duration_postedOrders);
            viewBidsBtn = itemView.findViewById(R.id.viewBids_btn);
            placeBid = itemView.findViewById(R.id.place_bid_btn);
            editOrder = itemView.findViewById(R.id.edit_order_btn);
            viewOrderDetails=itemView.findViewById(R.id.view_order_details_btn);
            deleteOrderBtn=itemView.findViewById(R.id.delete_order_btn);}

        private void setData(String titleText, String itemNameText, String itemQtyText, String areaPincodeText, String startDate, int requestor, String customerID, String orderId,String endDate,String unitValue,int position) {
            title.setText(titleText);
            itemName.setText(itemNameText);
            itemQty.setText(itemQtyText+" "+unitValue);
            areaPincode.setText(areaPincodeText);
            duration.setText(startDate+" to "+endDate);

            orderDB = FirebaseFirestore.getInstance();
            if (requestor == 0) {
                viewOrderDetails.setVisibility(View.GONE);
                placeBid.setVisibility(View.GONE);
                viewBidsBtn.setVisibility(View.VISIBLE);
                deleteOrderBtn.setVisibility(View.VISIBLE);
                editOrder.setVisibility(ViewGroup.VISIBLE);
                viewBidsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), ViewBidsActivity.class);
                        intent.putExtra("orderId",orderId);
                        intent.putExtra("itemName",itemNameText);
                        intent.putExtra("qtyOrderdByCustomer",itemQtyText);
                        intent.putExtra("unit",unitValue);
                        intent.putExtra("duration",startDate+" to "+endDate);
                        itemView.getContext().startActivity(intent);


                    }
                });

                final Dialog deleteConfirmDialogBox = new Dialog(itemView.getContext());
                deleteConfirmDialogBox.setContentView(R.layout.cofirm_delete_order_dialog);
                deleteConfirmDialogBox.setCancelable(true);
                deleteConfirmDialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView cancelTv=deleteConfirmDialogBox.findViewById(R.id.cancel_delete_btn_tv);
                TextView okTv=deleteConfirmDialogBox.findViewById(R.id.ok_delete_btn_tv);

                deleteOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteConfirmDialogBox.show();
                    }
                });
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteConfirmDialogBox.dismiss();
                    }
                });
                okTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        orderDB.collection("Customers").document(customerID).collection("Orders").document(orderId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                postedOrdersModelList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(itemView.getContext(),"Removed SuccessFully",Toast.LENGTH_SHORT).show();
                                deleteConfirmDialogBox.dismiss();
                            }
                        });

                    }
                });

                final Dialog editOrderDialogBox = new Dialog(itemView.getContext());
                editOrderDialogBox.setContentView(R.layout.edit_order_dialog_box);
                editOrderDialogBox.setCancelable(true);
                editOrderDialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                EditText orderTitle = editOrderDialogBox.findViewById(R.id.order_title);
                EditText orderItemName = editOrderDialogBox.findViewById(R.id.order_itemName);
                EditText orderqty = editOrderDialogBox.findViewById(R.id.orderd_qty);
                EditText areaorpincode = editOrderDialogBox.findViewById(R.id.pincode);
                TextView startDateTV = editOrderDialogBox.findViewById(R.id.start_date_textView);
                TextView endDateTV = editOrderDialogBox.findViewById(R.id.end_date_textView);
                TextView unit = editOrderDialogBox.findViewById(R.id.unit);
                Button editOrderBtn = editOrderDialogBox.findViewById(R.id.edit_order_btn);
                Spinner unitSpinner = editOrderDialogBox.findViewById(R.id.unit_spinner_postOrder);
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);


                editOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editOrderDialogBox.show();
                        orderTitle.setText(titleText);
                        orderqty.setText(itemQtyText);
                        unit.setText(unitValue);
                        startDateTV.setText(startDate);
                        endDateTV.setText(endDate);
                        orderItemName.setText(itemNameText);
                        areaorpincode.setText(areaPincodeText);
                        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                unit.setText(unitSpinner.getSelectedItem().toString());

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        startDateTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int day) {
                                        month = month + 1;
                                        String date = day + "/" + month + "/" + year;
                                        startDateTV.setText(date);

                                    }
                                }, year, month, day);
                                datePickerDialog.show();
                            }
                        });

                        endDateTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int day) {
                                        month = month + 1;
                                        String date = day + "/" + month + "/" + year;
                                        endDateTV.setText(date);

                                    }
                                }, year, month, day);
                                datePickerDialog.show();
                            }
                        });

                        editOrderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String,Object> order=new HashMap<>();
                                if(TextUtils.isEmpty(orderTitle.getText().toString())){
                                    Toast.makeText(itemView.getContext(),"Please Add Valid Title to your Order",Toast.LENGTH_SHORT).show();
                                }else{
                                    String ordertitle=orderTitle.getText().toString();
                                    order.put("title",ordertitle);
                                    if(TextUtils.isEmpty(orderItemName.getText().toString())){
                                        Toast.makeText(itemView.getContext(),"Empty Item Name is not Allowed",Toast.LENGTH_SHORT).show();
                                    }else{
                                        String itemNameTextUpdated=orderItemName.getText().toString();
                                        order.put("itemName",itemNameTextUpdated);
                                        if(TextUtils.isEmpty(orderqty.getText().toString())){
                                            Toast.makeText(itemView.getContext(),"Please Enter Valid Order Quantity",Toast.LENGTH_SHORT).show();
                                        }else{
                                            String qty=orderqty.getText().toString();
                                            String unitValueUpdated=unit.getText().toString();
                                            order.put("qty",qty);
                                            order.put("unitValue",unitValue);
                                            if(TextUtils.isEmpty(areaorpincode.getText().toString())){
                                                Toast.makeText(itemView.getContext(),"Please Enter Valid Pincode",Toast.LENGTH_SHORT).show();
                                            }else{
                                                String areaPincodeTextUpdated=areaorpincode.getText().toString();
                                                order.put("areaPincode",areaPincodeTextUpdated);
                                                String startDateUpdated=startDateTV.getText().toString();
                                                String endDateUpdated=endDateTV.getText().toString();
                                                order.put("startDate",startDateUpdated);
                                                order.put("endDate",endDateUpdated);
                                                order.put("index","0");
                                                order.put("customerID",customerID);
                                                orderDB.collection("Customers").document(customerID).collection("Orders").document(orderId).update(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(itemView.getContext(),"Order Updated Succesfully",Toast.LENGTH_SHORT).show();
                                                            orderTitle.setText("");
                                                            orderItemName.setText("");
                                                            orderqty.setText("");
                                                            areaorpincode.setText("");
                                                            startDateTV.setText("Start Date");
                                                            endDateTV.setText("End Date");
                                                            editOrderDialogBox.dismiss();
                                                            title.setText(ordertitle);
                                                            itemName.setText(itemNameTextUpdated);
                                                            itemQty.setText(qty+" "+unitValueUpdated);
                                                            areaPincode.setText(areaPincodeTextUpdated);
                                                            duration.setText(startDateUpdated+" to "+endDateUpdated);

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

                });
            }
            else if (requestor == 1) {
                viewOrderDetails.setVisibility(View.VISIBLE);
                placeBid.setVisibility(ViewGroup.GONE);
                viewBidsBtn.setVisibility(View.GONE);
                editOrder.setVisibility(ViewGroup.GONE);
                viewOrderDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                        intent.putExtra("requestor","1");
                        intent.putExtra("customerId",customerID);
                        intent.putExtra("orderId",orderId);
                        itemView.getContext().startActivity(intent);
                    }
                });


            }
            else if (requestor == 2) {
                viewOrderDetails.setVisibility(View.GONE);
                placeBid.setVisibility(View.VISIBLE);
                viewBidsBtn.setVisibility(View.GONE);
                editOrder.setVisibility(View.GONE);
                final Dialog placeABidDialog = new Dialog(itemView.getContext());
                placeABidDialog.setContentView(R.layout.dialog_box_place_a_bid);
                placeABidDialog.setCancelable(false);
                placeABidDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView placeBidOrderTitle = placeABidDialog.findViewById(R.id.placeBid_order_title);
                EditText placeBidPricePerUnit = placeABidDialog.findViewById(R.id.placeBid_pricePerUnit);
                EditText placeBidDescription = placeABidDialog.findViewById(R.id.placeBid_description);
                ImageView placeBidCancelBtn = placeABidDialog.findViewById(R.id.placeBid_cancelBtn);
                Button placeBidCheckBtn = placeABidDialog.findViewById(R.id.placeBid_checkBtn);


                placeBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeABidDialog.show();
                        placeBidOrderTitle.setText(titleText);
                    }
                });
                placeBidCheckBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> bid = new HashMap<>();
                        Map<String, Object> farmerBidContent = new HashMap<>();
                        farmerBidContent.put("title", titleText);
                        farmerBidContent.put("itemName", itemNameText);
                        farmerBidContent.put("qty", itemQtyText+" "+unitValue);
                        farmerBidContent.put("areaPincode", areaPincodeText);
                        farmerBidContent.put("durationOfOrder", startDate+" to "+endDate);
                        if (TextUtils.isEmpty(placeBidPricePerUnit.getText().toString())) {
                            Toast.makeText(itemView.getContext(), "Please Enter a Valid Price Per Unit", Toast.LENGTH_SHORT).show();
                        } else {
                            String pricePerUnit = placeBidPricePerUnit.getText().toString();
                            bid.put("bidPricePerUnit", pricePerUnit);
                            farmerBidContent.put("bidPricePerUnit", pricePerUnit);
                            bidDB = FirebaseFirestore.getInstance();
                            if (TextUtils.isEmpty(placeBidDescription.getText().toString())) {
                                Toast.makeText(itemView.getContext(), "Please Enter a Valid Price Per Unit", Toast.LENGTH_SHORT).show();
                            } else {
                                String description = placeBidDescription.getText().toString();
                                bid.put("description", description);
                                farmerBidContent.put("description", description);
                                farmerBidContent.put("customerId", customerID);
                                farmerBidContent.put("orderId", orderId);
                                SharedPreferences pref=itemView.getContext().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
                                String aadharNumber=pref.getString("aadharCardNumber",null);
                                if (aadharNumber != null) {
                                    bid.put("farmerProducerId", aadharNumber);
                                    bidDB.collection("Customers").document(customerID).collection("Orders").document(orderId).collection("Bids").document(aadharNumber).set(bid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(itemView.getContext(), "Bid placed succesfully", Toast.LENGTH_SHORT).show();
                                                placeBidPricePerUnit.setText("");
                                                placeBidDescription.setText("");
                                            }

                                        }
                                    });
                                    farmerBidDB = FirebaseFirestore.getInstance();
                                    farmerBidDB.collection("Farmers_Producers").document(aadharNumber).collection("Bids").document(orderId).set(farmerBidContent, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });

                                }

                            }

                        }
                    }
                });
                placeBidCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeABidDialog.dismiss();
                    }
                });
            }
        }
    }
}
