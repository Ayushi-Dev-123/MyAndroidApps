package com.ayushi.apnachating.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupAdapter extends FirebaseRecyclerAdapter<User, CreateGroupAdapter.CreateGroupViewHolder> {

    ArrayList<User> selectUserList;

    public CreateGroupAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        selectUserList = new ArrayList<User>();
    }

    @Override
    protected void onBindViewHolder(@NonNull final CreateGroupViewHolder holder, int i, @NonNull final User user) {
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);
        holder.tvStatus.setText(user.getStatus());
        holder.tvName.setText(user.getName());
        if(user.isChecked()){
            holder.civSelect.setVisibility(View.VISIBLE);
        }
        else {
            holder.civSelect.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setChecked(!user.isChecked());
                    if(user.isChecked()){
                        holder.civSelect.setVisibility(View.VISIBLE);
                        selectUserList.add(user);
                    }
                    else{
                        holder.civSelect.setVisibility(View.GONE);
                        selectUserList.remove(user);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CreateGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_group_item_list,parent,false);
        return new CreateGroupViewHolder(v);
    }

    public class CreateGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        CircleImageView civSelect,civProfile;
        public CreateGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civGroupIcon);
            civSelect = itemView.findViewById(R.id.civSelectContact);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
    public ArrayList<User> getSelectUserList(){
        return selectUserList;
    }
}
