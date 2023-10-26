package com.example.feecollectionapplication.fee_details;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.feecollectionapplication.R;
import com.example.feecollectionapplication.full_details.FullDetails;
import com.example.feecollectionapplication.model.Student;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeeListAdapter extends RecyclerView.Adapter<FeeListAdapter.ViewHolder> {
    private final ArrayList<Student> list;

    public FeeListAdapter(ArrayList<Student> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public FeeListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fee_list_data_container, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull FeeListAdapter.ViewHolder holder, int position) {

        holder.studentID.setText(String.valueOf(list.get(position).getId()));
        holder.name.setText(list.get(position).getName());
        holder.stop.setText(list.get(position).getStop());
        holder.fees.setText(String.valueOf(list.get(position).getFees()));

        if (list.get(position).getFees()==1600){
            holder.cardView.setCardBackgroundColor(android.R.color.holo_green_dark);
        }

        String imageUrl = list.get(position).getImageUrl();
        Picasso.get().load(imageUrl).fit() // Picasso lib to download and add image to imageview
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.Image);

        /** listener to open the full details of student*/
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullDetails.class);
            intent.putExtra("id",holder.studentID.getText().toString());
            ContextCompat.startActivity(v.getContext(), intent, null);
        });

        holder.Image.setOnClickListener(v -> {
            final Dialog nagDialog = new Dialog(v.getContext(), android.R.style.Widget_DeviceDefault_ActionBar);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.preview_image);
            ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
            ivPreview.setBackgroundDrawable(holder.Image.getDrawable());
            nagDialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentID;
        public TextView name;
        public TextView stop;
        public TextView fees;

        public de.hdodenhof.circleimageview.CircleImageView Image;
        public CardView cardView;

        public  ViewHolder(View itemView) {
            super(itemView);

            this.studentID = itemView.findViewById(R.id.feeContID);
            this.name = itemView.findViewById(R.id.feeContName);
            this.stop = itemView.findViewById(R.id.feeContStop);
            this.fees = itemView.findViewById(R.id.feeContDues);

            this.Image = itemView.findViewById(R.id.feeContImage);
            this.cardView = itemView.findViewById(R.id.feeCardview);
        }
    }
}