package com.ayushi.apnachating.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.SendRequestActivity;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    Context context;
    public UserAdapter(@NonNull FirebaseRecyclerOptions<User> options,Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.findfriend_item_list, parent,false);
        return new UserViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int i, @NonNull final User user) {
        holder.tvUserName.setText(user.getName());
        holder.tvStatus.setText(user.getStatus());
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, SendRequestActivity.class);
                in.putExtra("user",user);
                context.startActivity(in);
            }
        });

        if(user.getState()!=null) {
            if (user.getState().equals("Online")) {
                holder.civOnOffState.setVisibility(View.VISIBLE);
                holder.tvLastSeen.setVisibility(View.INVISIBLE);
            } else if (user.getState().equals("Offline")) {
                holder.civOnOffState.setVisibility(View.INVISIBLE);
                holder.tvLastSeen.setVisibility(View.VISIBLE);
                holder.tvLastSeen.setText("Last Seen : " + user.getDate() + " " + user.getTime());
                holder.tvLastSeen.setTextColor(context.getResources().getColor(R.color.green));
            }
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civProfile,civOnOffState;
        TextView tvUserName, tvStatus,tvLastSeen;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civProfile);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvUserName = itemView.findViewById(R.id.tvName);
            tvLastSeen = itemView.findViewById(R.id.tvLastSeen);
            civOnOffState = itemView.findViewById(R.id.ivOnOffState);
        }
    }
}
