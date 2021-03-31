package com.example.testsql;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsql.databinding.AddUserBinding;
import com.example.testsql.databinding.UserItemListBinding;

import java.util.ArrayList;

import static android.os.Build.ID;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {
    DatabaseHelper helper;
    ArrayList<User> userList;
    Context context;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemListBinding binding = UserItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new UserAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterViewHolder holder, int position) {
        User user = userList.get(position);
        holder.binding.tvage.setText(user.getAge());
        holder.binding.tvname.setText(user.getName());
        holder.binding.tvemail.setText(user.getEmail());
        holder.binding.tvmobile.setText(user.getMobile());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserAdapterViewHolder extends RecyclerView.ViewHolder {
        UserItemListBinding binding;

        public UserAdapterViewHolder(UserItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User u = userList.get(position);
                    PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), binding.iv);
                    Menu menu = popupMenu.getMenu();
                    menu.add("Edit");
                    menu.add("Delete");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String title = item.getTitle().toString();
                            if (title.equals("Edit")) {
                                AlertDialog ab = new AlertDialog.Builder(context).create();
                                AddUserBinding userBinding = AddUserBinding.inflate(LayoutInflater.from(context));
                                ab.setView(userBinding.getRoot());
                                userBinding.btnAddText.setText("Update");
                                userBinding.etname.setText(u.getName());
                                userBinding.etemail.setText(u.getEmail());
                                userBinding.etage.setText(u.getAge());
                                userBinding.etmobile.setText(u.getMobile());

                                userBinding.btnOkay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String mobile = userBinding.etmobile.getText().toString();
                                        String email = userBinding.etemail.getText().toString();
                                        String age = userBinding.etage.getText().toString();
                                        String name = userBinding.etname.getText().toString();

                                        helper = new DatabaseHelper(context);
                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        ContentValues values = new ContentValues();
                                        values.put("name", name);
                                        values.put("age", age);
                                        values.put("mobile", mobile);
                                        values.put("email", email);

                                        User user = new User();
                                        user.setName(name);
                                        user.setMobile(mobile);
                                        user.setEmail(email);
                                        user.setAge(age);
                                        int id = u.getId();
                                        user.setId(id);
                                        db.update("user", values,  " = ? " ,new String[]{String.valueOf(id)});
                                       // db.execSQL("UPDATE "+"user"+" SET name = "+"'"+s+"' "+ "WHERE salary = "+"'"+s1+"'");
                                       // db.update("user ", values, "where "+u.getId(), new String[]{String.valueOf(u.getId())});
                                        ab.dismiss();
                                    }
                                });
                                userBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ab.dismiss();
                                    }
                                });
                                ab.show();
                            }
                            if (title.equals("Delete")) {
                                // delete from contact where id = 4
                                helper = new DatabaseHelper(context);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                ab.setTitle("Confirm");
                                ab.setMessage("Are you sure ? ");
                                ab.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.delete("user","id = ?", new String[]{""+u.getId()});
                                        userList.remove(position);
                                        Toast.makeText(context, "user removed", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                        db.close();
                                    }
                                });
                                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
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
    }
}
