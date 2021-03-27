package com.organically.organically.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.organically.organically.R;
import com.organically.organically.model.RentVehicleModelClass;

import java.util.List;

import static com.organically.organically.MyOpenToRentVehicle.refreshItemRentedVehicle;

public class RentVehicleAdapter extends RecyclerView.Adapter<RentVehicleAdapter.ViewHolder> {
    List<RentVehicleModelClass> rentVehicleModelClassList;
    FirebaseFirestore db;
    private int preSelectedPosition=-1;

    public RentVehicleAdapter(List<RentVehicleModelClass> rentVehicleModelClassList) {
        this.rentVehicleModelClassList = rentVehicleModelClassList;
    }

    @NonNull
    @Override
    public RentVehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_rent_vehicle,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentVehicleAdapter.ViewHolder holder, int position) {
        holder.setData(rentVehicleModelClassList.get(position).getPicLink(),rentVehicleModelClassList.get(position).getVehicleType(),
                rentVehicleModelClassList.get(position).getCompany(),rentVehicleModelClassList.get(position).getModel()
                ,rentVehicleModelClassList.get(position).getRent(),
                rentVehicleModelClassList.get(position).getFarmerId(),
                rentVehicleModelClassList.get(position).getFarmerName(),
                rentVehicleModelClassList.get(position).getRequestor(),
                position);

    }

    @Override
    public int getItemCount() {
        return rentVehicleModelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vehiclePhoto,optionsContainerIcon;
        TextView vehicleType,vehicleCompany,vehicleModel,ownerName,ownerMobNum,vehicleRent;
        LinearLayout farmerDetailsFarmerDetailsLayout,optionsContainer;
        ConstraintLayout constraintLayout;
        TextView editOpenToRentVehicle,removeOpenToRentVehicle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vehiclePhoto=itemView.findViewById(R.id.vehicle_imageView);
            vehicleType=itemView.findViewById(R.id.vehicleType_textView);
            vehicleCompany=itemView.findViewById(R.id.vehicelCompanyName_TV);
            vehicleModel=itemView.findViewById(R.id.vehicelModel_TV);
            vehicleRent=itemView.findViewById(R.id.vehicleRent_TV);
            ownerName=itemView.findViewById(R.id.ownerName_TV);
            ownerMobNum=itemView.findViewById(R.id.ownerMobNum_TV);
            farmerDetailsFarmerDetailsLayout=itemView.findViewById(R.id.farmer_deatils_linearLayout);
            optionsContainer=itemView.findViewById(R.id.options_container);
            optionsContainerIcon=itemView.findViewById(R.id.options_container_icon);
            constraintLayout=itemView.findViewById(R.id.constraintLayout_rentedVehicle);
            editOpenToRentVehicle=optionsContainer.findViewById(R.id.editOpenToRentVehicle);
            removeOpenToRentVehicle=optionsContainer.findViewById(R.id.removeOpenToRentVehicle);
        }
        private void setData(String picLink,String vehicleTypeText,String vehicleCompanyText,String vehicleModelText,String rentText,String farmerId,String farmerName,String requestor,int position){
            Glide.with(itemView.getContext()).load(picLink).placeholder(R.drawable.loading).into(vehiclePhoto);
            vehicleType.setText(vehicleTypeText);
            vehicleCompany.setText(vehicleCompanyText);
            vehicleModel.setText(vehicleModelText);
            vehicleRent.setText(rentText);
            db=FirebaseFirestore.getInstance();
            if(requestor.equals("0")){
                optionsContainer.setVisibility(View.GONE);
                farmerDetailsFarmerDetailsLayout.setVisibility(View.VISIBLE);
                db.collection("Farmers_Producers").document(farmerId).collection("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ownerName.setText(farmerName);
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ownerMobNum.setText(snapshot.getString("mobileNum"));
                        }
                    }
                });
            }else if(requestor.equals("1")){
                farmerDetailsFarmerDetailsLayout.setVisibility(View.INVISIBLE);
                optionsContainer.setVisibility(View.GONE);
                optionsContainerIcon.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        optionsContainer.setVisibility(View.VISIBLE);
                        optionsContainerIcon.setVisibility(View.VISIBLE);
                        refreshItemRentedVehicle(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition=position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItemRentedVehicle(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition=-1;
                    }
                });

            }
        }
    }
}
