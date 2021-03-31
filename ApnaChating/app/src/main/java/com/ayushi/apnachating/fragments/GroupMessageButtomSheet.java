
package com.ayushi.apnachating.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.Message;
import com.ayushi.apnachating.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageButtomSheet extends BottomSheetDialogFragment {

    String currentUserId, groupId, currentUserImageUrl;
    StorageReference storageReference;
    DatabaseReference groupReference, userReference;

    public GroupMessageButtomSheet(String currentUserId, String groupId, String currentUserImageUrl) {
        this.currentUserId = currentUserId;
        this.groupId = groupId;
        this.currentUserImageUrl = currentUserImageUrl;

        userReference = FirebaseDatabase.getInstance().getReference("Users");
        groupReference = FirebaseDatabase.getInstance().getReference("Groups");
        storageReference = FirebaseStorage.getInstance().getReference("GroupDocuments");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.file_dialog,container,false);
        ImageView ivWordFile = v.findViewById(R.id.wordLogo);
        ImageView ivPdfFile = v.findViewById(R.id.pdfLogo);
        ImageView ivVideoFile = v.findViewById(R.id.videoLogo);

        ivPdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("application/pdf");
                startActivityForResult(Intent.createChooser(in,"send pdf file"),111);
            }
        });

        ivWordFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("application/msword");
                startActivityForResult(Intent.createChooser(in,"send word file"),222);
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == getActivity().RESULT_OK && data!=null && data.getData()!=null){
            Uri fileUri = data.getData();
            sendDocumentMessage(fileUri, "pdf");
        }
        if(requestCode == 222 && resultCode == getActivity().RESULT_OK && data!=null && data.getData()!=null){
            Uri fileUri = data.getData();
            sendDocumentMessage(fileUri,"docx");
        }
    }

    private void sendDocumentMessage(Uri fileUri, final String type) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait...");
        pd.setMessage("Sending...");
        pd.show();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String messageId = groupReference.push().getKey();
        StorageReference filePath = storageReference.child(messageId + "." + type);

        filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String fileUri = uri.toString();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyy");
                        String date = sd.format(calendar.getTime());

                        sd = new SimpleDateFormat("hh:mm a");
                        String time = sd.format(calendar.getTime());
                        long timestamp = calendar.getTimeInMillis();

                        final Message msg = new Message();
                        msg.setMessage(fileUri);
                        msg.setFrom(currentUserId);
                        msg.setDate(date);
                        msg.setTimestamp(timestamp);
                        msg.setTime(time);
                        msg.setMessageId(messageId);
                        msg.setType(type);
                        msg.setSenderIcon(currentUserImageUrl);

                        groupReference.child(groupId).child("messages").child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
                    }
                });
            }
        });
    }
}
