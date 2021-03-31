package com.example.myapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int GALLERY_REQUEST_CODE = 1 ;
    Button btnCamera, btnDialer, btnGallary,btnGoogle, btnActionCall, btnOther;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getSupportActionBar().hide();

        initComponent();

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                != PermissionChecker.PERMISSION_GRANTED)


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(in, 111);
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent("com.example.myapp1.Other");
                startActivity(in);
            }
        });

        btnDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "tel:9826444850";
                Uri numberUri = Uri.parse(number);
                Intent in = new Intent(Intent.ACTION_DIAL,numberUri);
                startActivity(in);
            }
        });

        btnActionCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String number = "tel:9826444850";
                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(in);
                }
                catch(SecurityException e) {
                    Toast.makeText(MainActivity.this, "Please allow phone call permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(in.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Pick an image"), GALLERY_REQUEST_CODE);
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.com/";
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(in);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();

            img.setImageURI(imageData);
        }
        if(requestCode == 111) {
            Bundle bn = data.getExtras();
            if (bn != null) {
                Bitmap bmp = (Bitmap) bn.get("data");
                img.setImageBitmap(bmp);
            }
        }
        else {
            Toast.makeText(this, "Please capture an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponent() {
        btnCamera = findViewById(R.id.btnCamera);
        btnDialer = findViewById(R.id.btnDialer);
        btnGallary = findViewById(R.id.btnGallary);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnActionCall = findViewById(R.id.btnActionCall);
        img = findViewById(R.id.img);
        btnOther = findViewById(R.id.btnOther);
    }
}