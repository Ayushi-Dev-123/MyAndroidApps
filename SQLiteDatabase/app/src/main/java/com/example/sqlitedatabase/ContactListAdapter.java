package com.example.sqlitedatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitedatabase.databinding.AddcontactsBinding;
import com.example.sqlitedatabase.databinding.ContactlistItemBinding;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>
{
    Context context;
    ArrayList<Contact>al;
    public ContactListAdapter(Context context, ArrayList<Contact>al){
        this.context = context;
        this.al = al;
    }
    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactlistItemBinding binding = ContactlistItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ContactListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
        Contact c = al.get(position);
        holder.binding.tvName.setText(c.getName());
        holder.binding.tvMobile.setText(c.getMobile());
        holder.binding.tvEmail.setText(c.getEmail());
        holder.binding.tvAge.setText(""+c.getAge());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class ContactListViewHolder extends RecyclerView.ViewHolder{
        ContactlistItemBinding binding;
        public ContactListViewHolder(ContactlistItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            binding.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = getAdapterPosition();
                    final Contact c = al.get(position);
                    PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(),binding.ivMenu);
                    Menu menu = popupMenu.getMenu();
                    menu.add("Edit");
                    menu.add("Delete");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String title = item.getTitle().toString();
                            if(title.equals("Edit")){
                                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                final AddcontactsBinding contactsBinding = AddcontactsBinding.inflate(LayoutInflater.from(context));
                                ab.setView(contactsBinding.getRoot());
                                ab.setTitle("Update contact");
                                contactsBinding.etName.setText(c.getName());
                                contactsBinding.etMobile.setText(c.getMobile());
                                contactsBinding.etEmail.setText(c.getEmail());
                                contactsBinding.etAge.setText(""+c.getAge());

                                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String name = contactsBinding.etName.getText().toString();
                                        String email = contactsBinding.etEmail.getText().toString();
                                        String mobile = contactsBinding.etMobile.getText().toString();
                                        int age = Integer.parseInt(contactsBinding.etAge.getText().toString());

                                        c.setName(name);
                                        c.setMobile(mobile);
                                        c.setEmail(email);
                                        c.setAge(age);

                                        boolean status = ContactDAO.update(c,new DatabaseHelper(context));
                                        if(status){
                                            al.set(position,c);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Contact Updated", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                ab.setNegativeButton("Cancle",null);
                                ab.show();
                                // Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
                            }
                            else if(title.equals("Delete")){
                                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                ab.setTitle("Confirm");
                                ab.setMessage("Are you sure ? ");
                                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                       boolean status = ContactDAO.delete(c,new DatabaseHelper(context));
                                       if(status) {
                                           Toast.makeText(context, "Contact removed", Toast.LENGTH_SHORT).show();
                                           al.remove(position);
                                           notifyDataSetChanged();
                                       }
                                       else
                                           Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                ab.setNegativeButton("Cancle",null);
                                ab.show();
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }
}
