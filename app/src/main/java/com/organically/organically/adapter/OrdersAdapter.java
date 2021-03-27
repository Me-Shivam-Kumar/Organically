package com.organically.organically.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.organically.organically.OrderDetailsActivity;
import com.organically.organically.R;
import com.organically.organically.model.OrdersModel;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder> {
    private List<OrdersModel> ordersModelList;

    public OrdersAdapter(List<OrdersModel> ordersModelList) {
        this.ordersModelList = ordersModelList;
    }
    @NonNull
    @Override
    public OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_layout,parent,false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewHolder holder, int position) {
        holder.setData(ordersModelList.get(position).getOrderItem(),ordersModelList.get(position).getOrderQty()
        ,ordersModelList.get(position).getDuration(),ordersModelList.get(position).getStatus(),ordersModelList.get(position).getUnit(),
                ordersModelList.get(position).getCustomerId(),ordersModelList.get(position).getPricePerUnitOffered());

    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
       private TextView customer_name,phone_number,item_ordered,item_qty,amount,duration,status;
       private ConstraintLayout orderItemLayoutCL;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item_ordered=itemView.findViewById(R.id.item_ordered);
            item_qty=itemView.findViewById(R.id.item_qty);
            duration=itemView.findViewById(R.id.duration_TV);
            status=itemView.findViewById(R.id.status_TV);
            orderItemLayoutCL=itemView.findViewById(R.id.orders_item_layout_CL);

        }
        public void setData(String item_name,String itemQty,String durationPeriod,String statusInfo,String unit,String customerId,String pricePerUnitOffered){
            item_ordered.setText(item_name);
            item_qty.setText(itemQty+" "+unit);
            duration.setText(durationPeriod);
            if(statusInfo.equals("1")){
                status.setTextColor(Color.parseColor("#35BD09"));
                status.setText("Active");
            }else{
                status.setTextColor(Color.parseColor("#DC143C"));
            }
            orderItemLayoutCL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    intent.putExtra("itemName",item_name);
                    intent.putExtra("duration",durationPeriod);
                    intent.putExtra("qty",itemQty);
                    intent.putExtra("unit",unit);
                    intent.putExtra("status",statusInfo);
                    intent.putExtra("customerId",customerId);
                    intent.putExtra("pricePerUnitOffered",pricePerUnitOffered);
                    intent.putExtra("requestor","0");
                    itemView.getContext().startActivity(intent);
                }
            });


        }

    }
}
