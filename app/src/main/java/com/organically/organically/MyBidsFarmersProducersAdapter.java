package com.organically.organically;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBidsFarmersProducersAdapter extends RecyclerView.Adapter<MyBidsFarmersProducersAdapter.ViewHolder>{
    List<MyBidsFarmersProducersModel> myBidsFarmersProducersModelList;
    FirebaseFirestore dB;

    public MyBidsFarmersProducersAdapter(List<MyBidsFarmersProducersModel> myBidsFarmersProducersModelList) {
        this.myBidsFarmersProducersModelList = myBidsFarmersProducersModelList;
    }

    @NonNull
    @Override
    public MyBidsFarmersProducersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_my_bids_farmers_producers,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBidsFarmersProducersAdapter.ViewHolder holder, int position) {
        holder.setData(myBidsFarmersProducersModelList.get(position).getOrderTitle()
        ,myBidsFarmersProducersModelList.get(position).getOrderItemName(),myBidsFarmersProducersModelList.get(position).getOrderQty(),
                myBidsFarmersProducersModelList.get(position).getAreaOrPincode(),myBidsFarmersProducersModelList.get(position).getOrderDuration(),
                myBidsFarmersProducersModelList.get(position).getMybidPricePerUnit(),myBidsFarmersProducersModelList.get(position).getMybidDescription(),myBidsFarmersProducersModelList.get(position).getCustomerId(),myBidsFarmersProducersModelList.get(position).getOrderId());


    }

    @Override
    public int getItemCount() {
        return myBidsFarmersProducersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,itemNameTV,qtyTV,areaPincodeTV,durationTV,bidpriceperunitTV,biddescriptionTV;
        Button editBid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV=itemView.findViewById(R.id.posted_orders_title);
            itemNameTV=itemView.findViewById(R.id.item_name_postedOrders);
            qtyTV=itemView.findViewById(R.id.qty_postedOrders);
            areaPincodeTV=itemView.findViewById(R.id.areaPincode_postedOrders);
            durationTV=itemView.findViewById(R.id.duration_postedOrders);
            biddescriptionTV=itemView.findViewById(R.id.description_mybidFarmersProducers);
            bidpriceperunitTV=itemView.findViewById(R.id.priceperunit_mybidFarmersProducers);
            editBid=itemView.findViewById(R.id.edit_bid_btn);

        }
        private  void setData(String titleText,String itemNameText,String qtyText,String areaPincodeText,String durationText,String pricePerUnitText,String biddescriptionText,String customerId,String orderId){
            titleTV.setText(titleText);
            itemNameTV.setText(itemNameText);
            qtyTV.setText(qtyText);
            areaPincodeTV.setText(areaPincodeText);
            durationTV.setText(durationText);
            bidpriceperunitTV.setText(pricePerUnitText+" Rs.");
            biddescriptionTV.setText(biddescriptionText);
            final Dialog placeABidDialog = new Dialog(itemView.getContext());
            placeABidDialog.setContentView(R.layout.dialog_box_place_a_bid);
            placeABidDialog.setCancelable(false);
            placeABidDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView placeBidOrderTitle = placeABidDialog.findViewById(R.id.placeBid_order_title);
            EditText placeBidPricePerUnit = placeABidDialog.findViewById(R.id.placeBid_pricePerUnit);
            EditText placeBidDescription = placeABidDialog.findViewById(R.id.placeBid_description);
            ImageView placeBidCancelBtn = placeABidDialog.findViewById(R.id.placeBid_cancelBtn);
            Button placeBidCheckBtn = placeABidDialog.findViewById(R.id.placeBid_checkBtn);
            placeBidCheckBtn.setText("Edit");

            SharedPreferences pref=itemView.getContext().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
            String aadharId=pref.getString("aadharCardNumber",null );

            editBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeABidDialog.show();
                    placeBidPricePerUnit.setText(pricePerUnitText);
                    placeBidDescription.setText(biddescriptionText);
                    placeBidOrderTitle.setText(titleText);
                }
            });
            dB=FirebaseFirestore.getInstance();
            placeBidCheckBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Map<String, Object> bid = new HashMap<>();
                    if(pricePerUnitText.equals(placeBidPricePerUnit.getText()) && biddescriptionText.equals(placeBidDescription.getText())){
                        Toast.makeText(itemView.getContext(),"No Changes Found",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(!TextUtils.isEmpty(placeBidDescription.getText())){
                            if(!TextUtils.isEmpty(placeBidPricePerUnit.getText())){
                                String price=placeBidPricePerUnit.getText().toString();
                                String description=placeBidDescription.getText().toString();
                                bid.put("bidPricePerUnit",price);
                                bid.put("description",description);
                                dB.collection("Customers").document(customerId).collection("Orders").document(orderId).collection("Bids").document(aadharId).set(bid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(itemView.getContext(), "Bid Updated succesfully", Toast.LENGTH_SHORT).show();
                                            placeBidPricePerUnit.setText("");
                                            placeBidDescription.setText("");
                                            placeABidDialog.dismiss();
                                            bidpriceperunitTV.setText(price+" Rs.");
                                            biddescriptionTV.setText(description);
                                        }

                                    }
                                });
                                dB.collection("Farmers_Producers").document(aadharId).collection("Bids").document(orderId).set(bid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
