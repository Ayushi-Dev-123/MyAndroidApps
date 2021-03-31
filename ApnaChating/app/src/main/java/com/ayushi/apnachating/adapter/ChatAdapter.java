package com.ayushi.apnachating.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends FirebaseRecyclerAdapter<User, ChatAdapter.ChatViewHolder> {

   // private final View.OnClickListener mOnClickListener = new TestClickedListner();
    Context context;
    DatabaseReference userReference, messageCounterReference, contactReference;
    String currentUserId;
    private OnRecyclerViewClick listner;

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        messageCounterReference = FirebaseDatabase.getInstance().getReference("MessageCounter");
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int i, @NonNull User user) {
        holder.tvName.setText(user.getName());
        holder.tvStatus.setText(user.getStatus());
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.ciProfile);

        if(user.getState()!=null) {
            if (user.getState().equals("Online")) {
                holder.civOnOffState.setVisibility(View.VISIBLE);
                holder.tvLastSeen.setVisibility(View.INVISIBLE);
            }
            else if (user.getState().equals("Offline")) {
                holder.civOnOffState.setVisibility(View.INVISIBLE);
                holder.tvLastSeen.setVisibility(View.VISIBLE);
                holder.tvLastSeen.setText("Last Seen : " + user.getDate() + " " + user.getTime());
                holder.tvLastSeen.setTextColor(context.getResources().getColor(R.color.green));
            }
        }

        messageCounterReference.child(currentUserId).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    long total = snapshot.getChildrenCount();
                    if(total > 0) {
                        holder.btnMessageCounter.setVisibility(View.VISIBLE);
                        holder.btnMessageCounter.setText("" + total);
                    }
                }
                else
                    holder.btnMessageCounter.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.findfriend_item_list,parent,false);
        return new ChatViewHolder(v);
    }

    public class TestClickedListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
          CircleImageView ciProfile, civOnOffState;
          TextView tvName, tvStatus, tvLastSeen;
          Button btnMessageCounter;
          public ChatViewHolder(@NonNull View itemView) {
              super(itemView);
              ciProfile = itemView.findViewById(R.id.civProfile);
              tvName = itemView.findViewById(R.id.tvName);
              tvStatus = itemView.findViewById(R.id.tvStatus);
              tvLastSeen = itemView.findViewById(R.id.tvLastSeen);
              civOnOffState = itemView.findViewById(R.id.ivOnOffState);
              btnMessageCounter = itemView.findViewById(R.id.btnMessageCount);

              itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int position = getAdapterPosition();
                      if(position != RecyclerView.NO_POSITION && listner != null){
                          listner.onItemClick(getItem(position),position);
                      }
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
