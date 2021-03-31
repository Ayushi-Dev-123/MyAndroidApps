package com.example.navigationdrawer1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChangePasswordFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changepassword,container,false);
        EditText etOld = v.findViewById(R.id.Old);
        EditText etConfirm = v.findViewById(R.id.etConfirm);
        EditText etNew = v.findViewById(R.id.New);
        Button btnSave = v.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
