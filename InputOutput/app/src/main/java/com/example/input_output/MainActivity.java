package com.example.input_output;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.sax.TextElementListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    EditText etInput;
    TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputoutput);
        etInput = findViewById(R.id.textinput);
        tvOutput = findViewById(R.id.textoutput);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 tvOutput.setText("" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
