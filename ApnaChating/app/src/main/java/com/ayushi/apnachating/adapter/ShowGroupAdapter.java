package com.ayushi.apnachating.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.GroupInfoActivity;
import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.Group;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowGroupAdapter extends FirebaseRecyclerAdapter<Group, ShowGroupAdapter.ShowGroupViewHolder> {
    private OnRecyclerViewClick listener;
    String currentUserId;
    Context context;

    public ShowGroupAdapter(@NonNull FirebaseRecyclerOptions<Group> options,Context context) {
        super(options);
        this.context = context;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    protected void onBindViewHolder(@NonNull ShowGroupViewHolder holder, int i, @NonNull Group group) {
        Picasso.get().load(group.getIcon()).placeholder(R.drawable.chat_logo2).into(holder.civGroupIcon);
        holder.tvGroupName.setText(group.getGroupName());
        holder.tvGroupDescription.setText(group.getDescription());

        if(currentUserId.equals(group.getCreatedBy()))
           holder.ivPopUp.setVisibility(View.VISIBLE);
        else
            holder.ivPopUp.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ShowGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_list,parent,false);
        return new ShowGroupViewHolder(v);
    }

    public class ShowGroupViewHolder extends RecyclerView.ViewHolder{
        CircleImageView civGroupIcon;
        TextView tvGroupName, tvGroupDescription;
        ImageView ivPopUp;
        LinearLayout ll;
        public ShowGroupViewHolder(@NonNull final View itemView) {
            super(itemView);
            civGroupIcon = itemView.findViewById(R.id.civProfile);
            tvGroupDescription = itemView.findViewById(R.id.tvStatus);
            tvGroupName = itemView.findViewById(R.id.tvName);
            ivPopUp = itemView.findViewById(R.id.popUpMenu);
            ll = itemView.findViewById(R.id.ll);;

            ivPopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), ivPopUp);
                    Menu menu = popupMenu.getMenu();
                    menu.add("Group Info");
                    menu.add("Delete group");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String title = item.getTitle().toString();
                            int position = getAdapterPosition();
                            final Group group = getItem(position);

                            if(title.equals("Group Info")){
                                Intent in = new Intent(itemView.getContext(), GroupInfoActivity.class);
                                in.putExtra("group", group);
                                itemView.getContext().startActivity(in);
                            }
                            else if(title.equals("Delete group")){
                                AlertDialog.Builder ab = new AlertDialog.Builder(itemView.getContext());
                                ab.setTitle("Please Confirm");
                                ab.setMessage("If you delete this group then it will automatically remove all members ");
                                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final ProgressDialog pd = new ProgressDialog(itemView.getContext());
                                        pd.setMessage("Please wait while deleting group");
                                        pd.show();

                                        DatabaseReference groupReference = FirebaseDatabase.getInstance().getReference("Groups");
                                        groupReference.child(group.getGroupId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pd.dismiss();
                                                if(task.isSuccessful()){
                                                    Toast.makeText(itemView.getContext(),"Group Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(itemView.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                ab.show();
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                   // Group group = getItem(position);
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getItem(position), position);
                    }
                }
            });
        }
    }

    public interface OnRecyclerViewClick{
        void onItemClick(Group group, int position);
    }
    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
