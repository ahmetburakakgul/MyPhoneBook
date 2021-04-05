package com.example.telefonuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgBtnRehber,imgBtnGaleri,imgBtnTitresim,imgBtnFener,imgBtnMail;
    private Intent intent;
    private final int CAMERA_REQUEST_CODE=2;
    boolean hasCameraFlash = false;
    private boolean isFlashOn=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBtnRehber=(ImageButton)findViewById(R.id.imgBtnRehber);
        imgBtnRehber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(intent);
            }
        });

        imgBtnGaleri=(ImageButton)findViewById(R.id.imgBtnGaleri);
        imgBtnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,44);
            }
        });

        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        imgBtnFener=(ImageButton)findViewById(R.id.imgBtnFener);
        imgBtnFener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(Manifest.permission.CAMERA,CAMERA_REQUEST_CODE);
            }
        });

        imgBtnTitresim=(ImageButton)findViewById(R.id.imgBtnTitresim);
        imgBtnTitresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator titre = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                titre.vibrate(1000);
            }
        });

        imgBtnMail=(ImageButton)findViewById(R.id.imgBtnMail);
        imgBtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void flashLight() {
        if (hasCameraFlash) {
            if (isFlashOn) {
                flashLightOff();
                isFlashOn=false;
            } else {
                flashLightOn();
                isFlashOn=true;
            }
        } else {
            Toast.makeText(MainActivity.this, "Bu Cihaz Flash Desteklemiyor",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

    private void askPermission(String permission,int requestCode) {
        if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            // We Dont have permission
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);

        }else {
            // We already have permission do what you want
            flashLight();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    hasCameraFlash = getPackageManager().
                            hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                    Toast.makeText(this,"Kameraya İzzin Verildi",Toast.LENGTH_LONG).show();
                    flashLight();

                }else{
                    Toast.makeText(this,"Kamera İzni Kapatıldı",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}