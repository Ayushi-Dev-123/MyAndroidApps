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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends FirebaseRecyclerAdapter<User, ContactAdapter.ContactViewHolder> {

     Context context;
     DatabaseReference userReference;

     public ContactAdapter(@NonNull FirebaseRecyclerOptions<User> options,Context context) {
          super(options);
          this.context = context;
          userReference = FirebaseDatabase.getInstance().getReference("Users");
     }

     @NonNull
     @Override
     public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.findfriend_item_list,parent,false);
          return new ContactViewHolder(v);
     }

     @Override
     protected void onBindViewHolder(@NonNull ContactViewHolder holder, int i, @NonNull User user) {
          holder.tvStatus.setText(user.getStatus());
          holder.tvName.setText(user.getName());
          Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);

          if (user.getState() != null){
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

     public  class ContactViewHolder extends RecyclerView.ViewHolder{
          CircleImageView civProfile, civOnOffState;
          TextView tvName, tvStatus, tvLastSeen;
          public ContactViewHolder(@NonNull View itemView) {
               super(itemView);
               tvName = itemView.findViewById(R.id.tvName);
               tvStatus = itemView.findViewById(R.id.tvStatus);
               civProfile  = itemView.findViewById(R.id.civProfile);
               civOnOffState = itemView.findViewById(R.id.ivOnOffState);
               tvLastSeen = itemView.findViewById(R.id.tvLastSeen);
          }
     }
}
