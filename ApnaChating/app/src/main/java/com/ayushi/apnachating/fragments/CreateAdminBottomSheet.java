package com.ayushi.apnachating.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.Group;
import com.ayushi.apnachating.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAdminBottomSheet extends BottomSheetDialogFragment {

    Button btnExit, btnCancle;
    TextView tvName, tvText;
    LinearLayout ll;
    CircleImageView civUser;
    User user;
    Group group;
    String currentUserId;
    DatabaseReference groupReference, memberReference;

    public CreateAdminBottomSheet() {
    }

    public CreateAdminBottomSheet(User user, DatabaseReference groupReference, DatabaseReference memberReference, Group group, String currentUserId) {
        this.user = user;
        this.group = group;
        this.currentUserId = currentUserId;
        this.groupReference = groupReference;
        this.memberReference = memberReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_admin,container,false);
        ll = v.findViewById(R.id.ll);
        tvText = v.findViewById(R.id.tvText);
        tvName = v.findViewById(R.id.tvUser);
        civUser = v.findViewById(R.id.civUser);
        btnCancle = v.findViewById(R.id.btnCancle);
        btnExit = v.findViewById(R.id.btnExit);
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(civUser);
        tvName.setText(user.getName());

        btnExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("createdBy",user.getUid());
                groupReference.child(group.getGroupId()).updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            memberReference.child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       groupReference.child(group.getGroupId()).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   dismiss();
                                                   getActivity().finish();
                                               }
                                               else
                                                   Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                                }
                            });
                        }
                        else
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
