package com.organically.organically;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFarmersProducerAdapter extends RecyclerView.Adapter<SearchFarmersProducerAdapter.ViewHolder> implements Filterable {
    List<SearchFarmersProducersModel> searchFarmersProducersModelList;
    List<SearchFarmersProducersModel> allSearchFarmersProducersModelList;
    StorageReference storageReference;

    public SearchFarmersProducerAdapter(List<SearchFarmersProducersModel> searchFarmersProducersModelList) {
        this.searchFarmersProducersModelList = searchFarmersProducersModelList;
        this.allSearchFarmersProducersModelList=new ArrayList<>(searchFarmersProducersModelList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_find_farmers,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(searchFarmersProducersModelList.get(position).getName(),searchFarmersProducersModelList.get(position).getAddress(),searchFarmersProducersModelList.get(position).getOfferings(),
                searchFarmersProducersModelList.get(position).getPic(),searchFarmersProducersModelList.get(position).getAadharNum());

    }

    @Override
    public int getItemCount() {
        return searchFarmersProducersModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchFarmersProducersModel> filteredList=new ArrayList<SearchFarmersProducersModel>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(allSearchFarmersProducersModelList);
            }else{
                for(SearchFarmersProducersModel farmerorProducer: allSearchFarmersProducersModelList){
                    if(farmerorProducer.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || farmerorProducer.getOfferings().toLowerCase().contains(constraint.toString().toLowerCase()) ){
                        filteredList.add(farmerorProducer);

                    }

                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchFarmersProducersModelList.clear();
            searchFarmersProducersModelList.addAll((Collection<? extends SearchFarmersProducersModel>) results.values);
            notifyDataSetChanged();

        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView nameTV,addressTV,offeringsTV;
        ConstraintLayout findFarmersProducersCL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.circleImageView_famerproducer);
            nameTV=itemView.findViewById(R.id.name_famerproducer);
            addressTV=itemView.findViewById(R.id.address_farmerproducer);
            offeringsTV=itemView.findViewById(R.id.offerings_farmersproducers);
            findFarmersProducersCL=itemView.findViewById(R.id.find_farmers_producer_CL);
        }
        public void setData(String nameText,String addressText,String offeringText,String picLink,String aadharNumText){
            Glide.with(itemView.getContext()).load(picLink).placeholder(R.drawable.ic_account_circle).into(profilePic);
            nameTV.setText(nameText);
            addressTV.setText(addressText);
            offeringsTV.setText(offeringText);
            findFarmersProducersCL.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),FarmerProducerDetailsActivity.class);
                    intent.putExtra("aadharId",aadharNumText);
                    intent.putExtra("name",nameText);
                    intent.putExtra("profilePic",picLink);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
