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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageButtonSheet extends BottomSheetDialogFragment {

    String currentUserId, recevierUserId;
    StorageReference storageReference;
    DatabaseReference messageReference;

    public ChatMessageButtonSheet(String currentUserId, String recevierUserId){
        this.currentUserId = currentUserId;
        this.recevierUserId = recevierUserId;

        storageReference = FirebaseStorage.getInstance().getReference("Document Messages");
        messageReference = FirebaseDatabase.getInstance().getReference("Messages");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.file_dialog,container,false);
        ImageView ivPdfFile = v.findViewById(R.id.pdfLogo);
        ImageView ivWordFile = v.findViewById(R.id.wordLogo);
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

        ivVideoFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("video/*");
                startActivityForResult(Intent.createChooser(in,"select video"),333);
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
        if(requestCode == 333 && resultCode == getActivity().RESULT_OK && data!=null && data.getData()!=null){
            Uri fileUri = data.getData();
            sendDocumentMessage(fileUri,"mp4");
        }

    }

    private void sendDocumentMessage(Uri fileUri, final String type) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait...");
        pd.setMessage("Sending...");
        pd.show();

        final String messageId = messageReference.push().getKey();
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

                        final Message msg = new Message(date,time,type,currentUserId,recevierUserId,messageId,fileUri,calendar.getTimeInMillis());

                        messageReference.child(currentUserId).child(recevierUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                messageReference.child(recevierUserId).child(currentUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        if(!task.isSuccessful()){
                                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
