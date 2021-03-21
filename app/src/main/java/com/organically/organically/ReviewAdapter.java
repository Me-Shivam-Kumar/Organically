package com.organically.organically;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    List<ReviewModelClass> reviewModelClassList;

    public ReviewAdapter(List<ReviewModelClass> reviewModelClassList) {
        this.reviewModelClassList = reviewModelClassList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.setData(reviewModelClassList.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        if(reviewModelClassList.size() <8){
            return reviewModelClassList.size();
        }else{
            return 8;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView review;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.review_TV);
        }
        private void setData(String reviewData){
            review.setText(reviewData);
        }
    }
}
