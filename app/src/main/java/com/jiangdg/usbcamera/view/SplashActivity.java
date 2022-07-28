package com.jiangdg.usbcamera.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tflite_yolov5_test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * permission checking
 * Created by jiangdongguo on 2019/6/27.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };
    private static final int REQUEST_CODE = 1;
    private List<String> mMissPermissions = new ArrayList<>();

    //ACTION_USB_PERMISSION
//    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
//    private Context mContext;
//    private UsbManager mUsbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //获取USB设备列表及定位到要申请权限的USB设备
//        mContext = getApplication();
//        mUsbManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> devices = mUsbManager.getDeviceList();
//        List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
//        for(UsbDevice device:devices.values()){
//            getUsbPermission(device);
//        }

        if (isVersionM()) {
            checkAndRequestPermissions();
        } else {
            startMainActivity();
        }
    }

//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        @SuppressLint("NewApi")
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    mContext.unregisterReceiver(mUsbReceiver);
//                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        //TODO 授权成功，操作USB设备
//                        startMainActivity();
//                    } else {
//                        //用户点击拒绝了
//                    }
//                }
//            }
//        }
//    };
//
//
//    private void getUsbPermission(UsbDevice mUSBDevice) {
//        System.out.println("开始申请USB权限====================");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
//        mContext.registerReceiver(mUsbReceiver, filter);
//        // 该代码执行后，系统弹出一个对话框/等待权限
//        mUsbManager.requestPermission(mUSBDevice, pendingIntent);
//    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void checkAndRequestPermissions() {
        mMissPermissions.clear();
        for (String permission : REQUIRED_PERMISSION_LIST) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissPermissions.add(permission);
            }
        }
        // check permissions has granted
        if (mMissPermissions.isEmpty()) {
            startMainActivity();
        } else {
            ActivityCompat.requestPermissions(this,
                    mMissPermissions.toArray(new String[mMissPermissions.size()]),
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mMissPermissions.remove(permissions[i]);
                }
            }
        }
        // Get permissions success or not
        if (mMissPermissions.isEmpty()) {
            startMainActivity();
        } else {
            Toast.makeText(SplashActivity.this, "get permissions failed,exiting...",Toast.LENGTH_SHORT).show();
            SplashActivity.this.finish();
        }
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, USBCameraActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);
    }

}

