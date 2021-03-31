package com.example.telephonicserivce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.telephonicserivce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    TelephonyManager manager;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PermissionChecker.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},111);

        manager.listen(new GetPhoneState(),PhoneStateListener.LISTEN_CALL_STATE);

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String deviceId = manager.getDeviceId();
                    String simNo = manager.getSimSerialNumber();
                    String simName = manager.getSimOperatorName();
                    String networkoperator = manager.getNetworkOperatorName();
                    int networkType = manager.getNetworkType();
                    String netType = "";
                    if(networkType == TelephonyManager.NETWORK_TYPE_LTE)
                       netType = "LTE";
                    else  if(networkType == TelephonyManager.NETWORK_TYPE_EDGE);
                        netType = "EDGE";

                    String simState = "";
                    int ss = manager.getSimState();
                    if(ss == TelephonyManager.SIM_STATE_READY)
                         simState = "Ready";
                    else if(ss == TelephonyManager.SIM_STATE_ABSENT)
                        simState = "Absent";
                    else if(ss == TelephonyManager.SIM_STATE_UNKNOWN)
                        simState = "Unknown";

                    String model = Build.MODEL;
                    String manufacturer = Build.MANUFACTURER;
                    String brand = Build.BRAND;

                    String info = "Device Id : " + deviceId +
                            "\nSim No. : " + simNo +
                            "\nSim Name : " + simName +
                            "\nNetwork Type : " + netType +
                            "\nNetwork Name : " + networkoperator +
                            "\nSim State : " + simState +
                            "\nModel : " + model +
                            "\nManuFacturar : " + manufacturer +
                            "\nBrand : " + brand;
                    binding.tvText.setText(info);
                }
                catch (Exception e){
                    Log.e("Tag","===>"+e);
                }
            }
        });
    }
    class GetPhoneState extends PhoneStateListener{
        public void onCallStateChanged(int state, String phoneNumber){
            super.onCallStateChanged(state,phoneNumber);
            if(state == TelephonyManager.CALL_STATE_IDLE)
                Toast.makeText(MainActivity.this, "IDEL", Toast.LENGTH_SHORT).show();
            else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
                Toast.makeText(MainActivity.this, "OFFHOOK", Toast.LENGTH_SHORT).show();
            else if(state == TelephonyManager.CALL_STATE_RINGING)
                Toast.makeText(MainActivity.this, "RINGING", Toast.LENGTH_SHORT).show();
        }
    }
}