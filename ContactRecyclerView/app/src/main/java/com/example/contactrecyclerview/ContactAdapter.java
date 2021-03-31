package com.example.contactrecyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {
    private static final int GALLERY_REQUEST_CODE = 1;
    Context context;
    ArrayAdapter<Contact>adapter;
    ArrayList<Contact> al;
    public ContactAdapter(Context context, ArrayList<Contact>al){
    //    super(context,R.layout.contactlist,al);
        this.al = al;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.contactlist, parent, false);
        return new ContactViewHolder(v);
    }

    //@Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, int position) {
        Contact c = al.get(position);
        final int itemIndex = position;
        holder.tvName.setText(c.getName());
        holder.tvNumber.setText(c.getNumber());
        holder.img1.setImageResource(c.getImageid());
        //holder.rv.setAdapter();
        holder.Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.Image1);
                Menu menu = popupMenu.getMenu();
                menu.add("Add");
                menu.add("Edit");
                menu.add("Delete");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String title = menuItem.getTitle().toString();
                        if(title.equals("Add")) {
                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                            ab.setTitle("Add New Contact");
                            View v = LayoutInflater.from(context).inflate(R.layout.add, null);
                            final EditText etName = v.findViewById(R.id.Name);
                            final EditText etNuumber = v.findViewById(R.id.Number);
                            //final Button btnSave = v.findViewById(R.id.btnSave);
                            final Button btnGallery = v.findViewById(R.id.btnGallary);
                            final Button btnCamera = v.findViewById(R.id.btnCamera);
                            final ImageView imag = v.findViewById(R.id.imag);
                            ab.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String name = etName.getText().toString();
                                    String number = etNuumber.getText().toString();
                                    /*/int image = imag.getImageAlpha(btnGallery)
                                    btnGallery.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent in = new Intent();
                                            in.setType("image/*");
                                            in.setAction(in.ACTION_GET_CONTENT);
                                            startActivityForResult(Intent.createChooser(in,"Pick an image"), GALLERY_REQUEST_CODE);
                                        }

                                        private void startActivityForResult(Intent pick_an_image, int galleryRequestCode) {
                                        }
                                    });
                                          */
                                    Contact c = new Contact(name,number);
                                    al.add(c);
                                     adapter:notifyDataSetChanged();
                                }
                            });
                            ab.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            ab.setView(v);
                            ab.show();
                        }
                        else if(title.equals("Edit")){
                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                            ab.setTitle("Edit Contact");
                            View v = LayoutInflater.from(context).inflate(R.layout.add, null);
                            final EditText etName = v.findViewById(R.id.Name);
                            final EditText etNuumber = v.findViewById(R.id.Number);
                            //v.etName.setText(v.setName());
                            //v.tvNumber.setText(v.getNumber());
                        }
                        else if(title.equals("Delete")){
                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                            ab.setTitle("Alert");
                            ab.setMessage("Are you sure ? ");
                            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final Contact obj = al.get(itemIndex);
                                    al.remove(itemIndex);
                                    notifyDataSetChanged();
                                    Snackbar sb;
                                    Snackbar.make(view,"Contact Deleted",Snackbar.LENGTH_LONG)
                                            .setAction("Undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    al.add(itemIndex, obj);
                                                    notifyDataSetChanged();
                                                }
                                            }).show();
                                }
                            });
                            ab.setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

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
    }

    public int getItemCount() {
        return al.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber;
        RecyclerView rv;
        ImageView img1, Image1;
        public ContactViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.Name);
            tvNumber = v.findViewById(R.id.Number);
            rv = v.findViewById(R.id.rv);
            img1 = v.findViewById(R.id.img1);
            Image1 = v.findViewById(R.id.image1);
        }
    }
}


