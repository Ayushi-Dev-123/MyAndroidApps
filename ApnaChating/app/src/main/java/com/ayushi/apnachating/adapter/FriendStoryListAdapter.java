package com.ayushi.apnachating.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendStoryListAdapter extends RecyclerView.Adapter<FriendStoryListAdapter.FriendStoryListViewHolder> {
    Context context;
    ArrayList<User> al;
    OnRecyclerViewClick listener;

    public FriendStoryListAdapter(Context context, ArrayList<User>al){
        this.al = al;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendStoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.friend_story_item_list,parent,false);
        return new FriendStoryListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendStoryListViewHolder holder, int position) {
        User user = al.get(position);
        Picasso.get().load(user.getImage()).into(holder.civProfile);
        holder.tvName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class FriendStoryListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civProfile;
        TextView tvName;
        public FriendStoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civProfile);
            tvName = itemView.findViewById(R.id.tvText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = al.get(position);
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(user,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerViewClick {
        void onItemClick(User user, int position);
    }
    public void setOnitemClickListner(OnRecyclerViewClick listner){
        this.listener = listner;
    }
}
