package com.ayushi.apnachating.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageAdapter extends ArrayAdapter {

    Context context;
    ArrayList<Message> al;
    String currentUserId;

    public GroupMessageAdapter(Context context, ArrayList<Message>al) {
        super(context,R.layout.chat_message_item_list, al);
        this.context = context;
        this.al = al;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(R.layout.group_chat_message_item_list,null);

        RelativeLayout leftRL = v.findViewById(R.id.leftRL);
        RelativeLayout rigthRL = v.findViewById(R.id.rightRL);
        TextView tvFriendMessage = v.findViewById(R.id.tvFriendMessage);
        TextView tvFriendMessageTime = v.findViewById(R.id.tvFriendMessageTime);
        TextView tvMyMessage = v.findViewById(R.id.tvMyMessage);
        TextView tvMyMessageTime = v.findViewById(R.id.tvMyMessageTime);
        ImageView ivLeft = v.findViewById(R.id.ivLeft);
        ImageView ivRight = v.findViewById(R.id.ivRight);
        CircleImageView civUser = v.findViewById(R.id.civUser);

        final Message message = al.get(position);
        if(message.getType().equals("text")) {
            if (currentUserId.equals(message.getFrom())) {
                leftRL.setVisibility(View.GONE);
                rigthRL.setVisibility(View.VISIBLE);
                tvMyMessageTime.setText(message.getDate() + " " + message.getTime());
                tvMyMessage.setText(message.getMessage());
            }
            else {
                rigthRL.setVisibility(View.GONE);
                leftRL.setVisibility(View.VISIBLE);
                tvFriendMessageTime.setText(message.getDate() + " " + message.getTime());
                tvFriendMessage.setText(message.getMessage());
                Picasso.get().load(message.getSenderIcon()).placeholder(R.drawable.chat_logo2).into(civUser);
            }
        }
        else if(message.getType().equals("image")){
            if(currentUserId.equals(message.getFrom())){
                leftRL.setVisibility(View.GONE);
                rigthRL.setVisibility(View.VISIBLE);
                ivRight.getLayoutParams().height = 500;
                ivRight.getLayoutParams().width = 500;
                tvMyMessageTime.setText(message.getDate()+" "+message.getTime());
                //tvMyMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.white)));
                tvMyMessageTime.setTextColor(context.getResources().getColor(R.color.white));
                Picasso.get().load(message.getMessage()).into(ivRight);
            }
            else{
                rigthRL.setVisibility(View.GONE);
                leftRL.setVisibility(View.VISIBLE);
                ivLeft.getLayoutParams().height = 500;
                ivLeft.getLayoutParams().width = 500;
                tvFriendMessage.setVisibility(View.GONE);
                tvFriendMessageTime.setText(message.getDate()+" "+message.getTime());
                tvFriendMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.white)));
                //tvFriendMessageTime.setTextColor(context.getResources().getColor(R.color.actionBarPurple));
                Picasso.get().load(message.getMessage()).into(ivLeft);
                Picasso.get().load(message.getSenderIcon()).placeholder(R.drawable.chat_logo2).into(civUser);
            }
        }
        else if(message.getType().equals("pdf") || message.getType().equals("docx")){
            if(currentUserId.equals(message.getFrom())){
                leftRL.setVisibility(View.GONE);
                rigthRL.setVisibility(View.VISIBLE);
                ivRight.getLayoutParams().height = 300;
                ivRight.getLayoutParams().width = 300;

                if(message.getType().equals("pdf"))
                    ivRight.setBackgroundResource(R.drawable.pdf_logo);
                else if(message.getType().equals("docx"))
                    ivRight.setBackgroundResource(R.drawable.word_doc_logo);

                tvMyMessageTime.setText(message.getDate()+" "+message.getTime());
                //tvMyMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.actionBarPurple)));
                tvMyMessageTime.setTextColor(context.getResources().getColor(R.color.white));

                ivRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getMessage()));
                        context.startActivity(in);
                    }
                });
            }
            else{
                rigthRL.setVisibility(View.GONE);
                leftRL.setVisibility(View.VISIBLE);
                ivLeft.getLayoutParams().height = 300;
                ivLeft.getLayoutParams().width = 300;
                tvFriendMessage.setVisibility(View.GONE);

                if(message.getType().equals("pdf"))
                    ivLeft.setBackgroundResource(R.drawable.pdf_logo);
                else if(message.getType().equals("docx"))
                    ivLeft.setBackgroundResource(R.drawable.word_doc_logo);

                tvFriendMessageTime.setText(message.getDate()+" "+message.getTime());
                tvFriendMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.white)));
                //tvFriendMessageTime.setTextColor(context.getResources().getColor(R.color.white));
                Picasso.get().load(message.getSenderIcon()).placeholder(R.drawable.chat_logo2).into(civUser);
                ivLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getMessage()));
                        context.startActivity(in);
                    }
                });
            }
        }
        else if(message.getType().equals("mp4")){
            if(currentUserId.equals(message.getType())){
                leftRL.setVisibility(View.GONE);
                rigthRL.setVisibility(View.VISIBLE);
                ivRight.getLayoutParams().height = 500;
                ivRight.getLayoutParams().width = 500;
                tvMyMessageTime.setText(message.getDate()+" "+message.getTime());
                //tvMyMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.white)));
                tvMyMessageTime.setTextColor(context.getResources().getColor(R.color.white));
                Picasso.get().load(message.getMessage()).into(ivRight);

            }
            else{
                rigthRL.setVisibility(View.GONE);
                leftRL.setVisibility(View.VISIBLE);
                ivLeft.getLayoutParams().height = 500;
                ivLeft.getLayoutParams().width = 500;
                tvFriendMessage.setVisibility(View.GONE);
                tvFriendMessageTime.setText(message.getDate()+" "+message.getTime());
                tvFriendMessageTime.setBackground(new ColorDrawable(context.getResources().getColor(R.color.white)));
                //tvFriendMessageTime.setTextColor(context.getResources().getColor(R.color.actionBarPurple));
                Picasso.get().load(message.getMessage()).into(ivLeft);
            }
        }
        return v;
    }
}
