package com.example.calculator1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et1, et2;
    Button btnResult;
    CheckBox chkAdd, chkSubtract, chkMultiply, chkDivide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initCompoment();
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                double n1 = Double.parseDouble(s1);
                double n2 = Double.parseDouble(s2);
                double n3;
                String res = "";
                if(chkAdd.isChecked()) {
                    n3 = n1  + n2;
                    res = res + "Addition = " + n3 + "\n";
                }
                if(chkSubtract.isChecked()) {
                    n3 = n1 - n2;
                    res = res + "Subtraction = " + n3 + "\n";
                }
                if(chkMultiply.isChecked()) {
                    n3 = n1 * n2;
                    res = res + "Multiplication = " + n3 + "\n";
                }
                if(chkDivide.isChecked()) {
                    n3 = n1 / n2;
                    res = res + "Division = " + n3 + "\n";
                }
                Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initCompoment() {
        et1 = findViewById(R.id.etFirstNumber);
        et2 = findViewById(R.id.etSecondNumber);
        btnResult = findViewById(R.id.btnResult);
        chkAdd = findViewById(R.id.chkAdd);
        chkDivide = findViewById(R.id.chkDivide);
        chkMultiply = findViewById(R.id.chkMultiply);
        chkSubtract = findViewById(R.id.chkSubtract);
    }
}