package com.organically.organically.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.organically.organically.R;
import com.organically.organically.model.ViewBidsModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ViewBidsAdapter extends RecyclerView.Adapter<ViewBidsAdapter.viewHolder>{
    List<ViewBidsModel> viewBidsModelList;
    FirebaseFirestore profileDB;
    FirebaseFirestore inProgressDB;
    String famerProducerName;
    String aadharNumber;


    public ViewBidsAdapter(List<ViewBidsModel> viewBidsModelList) {
        this.viewBidsModelList = viewBidsModelList;


    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_view_bids,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(viewBidsModelList.get(position).getBidsproducerPricePerUnit(),viewBidsModelList.get(position).getBidsDescription(),viewBidsModelList.get(position).getFarmerProducerId(),viewBidsModelList.get(position).getOrderId(),
                viewBidsModelList.get(position).getItemName(),viewBidsModelList.get(position).getQtyRequiredByCustomer(),viewBidsModelList.get(position).getUnit(),viewBidsModelList.get(position).getDuration());


    }

    @Override
    public int getItemCount() {
        return viewBidsModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView producerProfileImage;
        TextView  producerNameTV,producerPricePerUnitTV,descriptionTV;
        Button hireBtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            producerProfileImage=itemView.findViewById(R.id.producerprofileimage_IV);
            producerNameTV=itemView.findViewById(R.id.producerName_TV);
            producerPricePerUnitTV=itemView.findViewById(R.id.producerPricePerUnit_TV);
            descriptionTV=itemView.findViewById(R.id.description_TV);
            hireBtn=itemView.findViewById(R.id.hire_btn);
            SharedPreferences pref=itemView.getContext().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
            aadharNumber=pref.getString("aadharCardNumber",null);

        }
        protected void setData(String price,String descriptionText,String farmerIdText,String orderId,String itemNameText,String qtyOrderedByCustomer,String unit,String durationText){
            profileDB=FirebaseFirestore.getInstance();
            inProgressDB=FirebaseFirestore.getInstance();
            producerPricePerUnitTV.setText(price);
            profileDB.collection("Farmers_Producers").document(farmerIdText).collection("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful()){
                       for(DocumentSnapshot snapshot:task.getResult()){
                           famerProducerName = snapshot.getString("name");
                           producerNameTV.setText(famerProducerName);
                           Glide.with(itemView.getContext()).load(snapshot.getString("profilePic")).into(producerProfileImage);

                       }
                   }
                }
            });
            descriptionTV.setText(descriptionText);


            hireBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Map<String,Object> inProgress=new HashMap<>();
                    inProgress.put("index","1");
                    inProgressDB.collection("Customers").document(aadharNumber).collection("Orders").document(orderId).update(inProgress).addOnSuccessListener(new OnSuccessListener<Void>(){

                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    Map<String,Object>order=new HashMap<>();
                    order.put("pricePerUnitOffered",price);
                    order.put("itemName",itemNameText);
                    order.put("qtyOrderdByCustomer",qtyOrderedByCustomer);
                    order.put("unit",unit);
                    order.put("customerId",aadharNumber);
                    order.put("orderId",orderId);
                    order.put("status","1");
                    order.put("duration",durationText);
                    inProgressDB.collection("Farmers_Producers").document(farmerIdText).collection("OrdersGot").
                            document(aadharNumber+" "+orderId).set(order, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(itemView.getContext(),"You Hired "+famerProducerName,Toast.LENGTH_SHORT).show();
                            hireBtn.setText("Hired");
                            hireBtn.setEnabled(false);

                        }
                    });

                }

            });




        }
    }
}
