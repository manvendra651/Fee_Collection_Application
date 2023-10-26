package com.example.feecollectionapplication.Registration;

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

public class RegistrationListAdapter extends RecyclerView.Adapter<RegistrationListAdapter.ViewHolder> {
    private final ArrayList<Student> list;
    String imageUrl;

    public RegistrationListAdapter(ArrayList<Student> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public RegistrationListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.registration_list_data_container, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RegistrationListAdapter.ViewHolder holder, int position) {
        String id =(list.get(position).getId());
        holder.studentID.setText(id);
        holder.name.setText(list.get(position).getName());
        holder.stop.setText(list.get(position).getStop());
        holder.number.setText(list.get(position).getPhone());

        imageUrl = list.get(position).getImageUrl();
        Picasso.get().load(imageUrl).fit() // Picasso lib to download and add image to imageview
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.Image);

        /** listener to open the full details of student*/
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullDetails.class);
            intent.putExtra("id",holder.studentID.getText().toString().trim());
            ContextCompat.startActivity(v.getContext(), intent, null);
        });

        // open large image by clicking on image
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
        public TextView number;

        public de.hdodenhof.circleimageview.CircleImageView Image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.studentID = itemView.findViewById(R.id.contID);
            this.name = itemView.findViewById(R.id.contName);
            this.stop = itemView.findViewById(R.id.contStop);
            this.number = itemView.findViewById(R.id.contNumber);

            this.Image = itemView.findViewById(R.id.contImage);
            this.cardView = itemView.findViewById(R.id.cardview);
        }
    }
}