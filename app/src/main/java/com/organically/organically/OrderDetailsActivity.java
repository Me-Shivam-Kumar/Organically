package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderDetailsActivity extends AppCompatActivity {

    View orderDetailsLayout;
    ImageView pointRigth;
    FirebaseFirestore dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        dB=FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.order_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderDetailsLayout=findViewById(R.id.include);
        orderDetailsLayout.setBackgroundColor(getResources().getColor(R.color.white));
        TextView itemName=orderDetailsLayout.findViewById(R.id.item_ordered);
        TextView duratioTV=orderDetailsLayout.findViewById(R.id.duration_TV);
        TextView statusTV=orderDetailsLayout.findViewById(R.id.status_TV);
        TextView itemQty=orderDetailsLayout.findViewById(R.id.item_qty);

        TextView itemPrice=findViewById(R.id.item_price);
        TextView qtyOrderd=findViewById(R.id.qtyOrderedTv);
        TextView totalPrice=findViewById(R.id.total_price);

        TextView fullName=findViewById(R.id.fullname);
        TextView address=findViewById(R.id.address);
        TextView pinCode=findViewById(R.id.pincode);

        pointRigth=findViewById(R.id.point_right);
        pointRigth.setVisibility(View.GONE);

        String requestor=getIntent().getStringExtra("requestor");
        if(requestor.equals("1")){

            String customerId=getIntent().getStringExtra("customerId");
            String orderId=getIntent().getStringExtra("orderId");
            dB.collectionGroup("OrdersGot").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            String customerIdFetched=snapshot.getString("customerId");
                            String orderIdfetched=snapshot.getString("orderId");
                            if(customerId.equals(customerIdFetched)&& orderId.equals(orderIdfetched)){
                                String duration=snapshot.getString("duration");
                                String unit=snapshot.getString("unit");
                                String status=snapshot.getString("status");
                                String qtyOrderdByCustomers=snapshot.getString("qtyOrderdByCustomer");
                                String offeredPrice=snapshot.getString("pricePerUnitOffered");
                                String itemNameText=snapshot.getString("itemName");


                                itemName.setText(itemNameText);
                                duratioTV.setText(duration);
                                itemQty.setText(qtyOrderdByCustomers+" "+unit);
                                if(status.equals("1")){
                                    statusTV.setText("Active");
                                }

                                itemPrice.setText(offeredPrice+" Rs.");
                                qtyOrderd.setText(qtyOrderdByCustomers+" "+unit);
                                int price=Integer.parseInt(offeredPrice);
                                int amount=Integer.parseInt(qtyOrderdByCustomers);
                                int total=price*amount;
                                totalPrice.setText("Rs."+Integer.toString(total)+"/-");



                                dB.collection("Customers").document(customerId).collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(DocumentSnapshot snapshot:task.getResult()){
                                                address.setText(snapshot.getString("address"));
                                                pinCode.setText(snapshot.getString("pincode"));
                                            }
                                        }
                                    }
                                });

                                break;
                            }else{
                                return;
                            }

                        }
                    }
                }
            });

        }
        else{
            String itemNameText=getIntent().getStringExtra("itemName");
            String durationText=getIntent().getStringExtra("duration");
            String qtyText=getIntent().getStringExtra("qty");
            String unit=getIntent().getStringExtra("unit");
            String status=getIntent().getStringExtra("status");
            String priceOfferedPerUnit=getIntent().getStringExtra("pricePerUnitOffered");
            String customerId=getIntent().getStringExtra("customerId");

            itemName.setText(itemNameText);
            duratioTV.setText(durationText);
            itemQty.setText(qtyText+" "+unit);
            if(status.equals("1")){
                statusTV.setText("Active");
            }

            itemPrice.setText(priceOfferedPerUnit+" Rs.");
            qtyOrderd.setText(qtyText+" "+unit);
            int price=Integer.parseInt(priceOfferedPerUnit);
            int amount=Integer.parseInt(qtyText);
            int total=price*amount;
            totalPrice.setText("Rs."+Integer.toString(total)+"/-");

            pointRigth=findViewById(R.id.point_right);
            pointRigth.setVisibility(View.GONE);

            dB.collection("Customers").document(customerId).collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            address.setText(snapshot.getString("address"));
                            pinCode.setText(snapshot.getString("pincode"));
                        }
                    }
                }
            });
        }



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}