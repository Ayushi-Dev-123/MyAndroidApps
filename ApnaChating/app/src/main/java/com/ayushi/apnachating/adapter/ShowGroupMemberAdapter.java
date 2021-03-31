package com.ayushi.apnachating.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.GroupInfoActivity;
import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowGroupMemberAdapter extends FirebaseRecyclerAdapter<User, ShowGroupMemberAdapter.CreateGroupViewHolder> {

    String currentUserId;
    private OnRecyclerViewClick listner;

    public ShowGroupMemberAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onBindViewHolder(@NonNull final CreateGroupViewHolder holder, int i, @NonNull final User user) {
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);
        holder.tvStatus.setText(user.getStatus());
        holder.tvName.setText(user.getName());

        if(currentUserId.equals(user.getUid())){
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(0,0);
            holder.itemView.setLayoutParams(params);
            holder.itemView.setVisibility(View.GONE);
        }
        else
            holder.itemView.setEnabled(true);
    }

    @NonNull
    @Override
    public CreateGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member_item_list,parent,false);
        return new CreateGroupViewHolder(v);
    }

    public class CreateGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        CircleImageView civProfile;
        public CreateGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = getItem(position);
                    if(position != RecyclerView.NO_POSITION && listner != null)
                        listner.onItemClick(user, position);
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        void onItemClick(User user, int position);
    }
    public void setOnItemClick(OnRecyclerViewClick listner){
        this.listner = listner;
    }
}
