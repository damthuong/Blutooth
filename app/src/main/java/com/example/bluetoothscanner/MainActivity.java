package com.example.bluetoothscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity<DatabaseReferrence> extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    private boolean mScanning;

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    Button btnScan;
    ListView listViewLE;

   // List<BluetoothDevice> listBluetoothDevice;
    ArrayAdapter adapterLeScanResult;

    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    private static final int REQUEST_ENABLE_BT = 0;
    ArrayList<String> listBluetooth;
    Button btnOnOff;
    Button btnLs;
    Button btnSupport;
    ImageView mBlueIv;

    private static final int PERMISSION_REQUEST_LOCATION = 0;

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//       // Log.d("StateInfor", "onCreate");
        // Check if BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this,
                    "BLUETOOTH_LE not supported in this device!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        getBluetoothAdapterAndLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,
                    "bluetoothManager.getAdapter()==null",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mBlueIv = (ImageView) findViewById(R.id.mBlueIv);
        mBlueIv.setImageResource(R.drawable.ic_action_on);

        btnOnOff = (Button) findViewById(R.id.btnOnOff);
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBluetoothAdapter.isEnabled()){
                    //showToast("Bluetooth is already off");
                    showToast("Turning On Bluetooth....");
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                }

                else if (mBluetoothAdapter.isEnabled()){
                    // showToast("Bluetooth is already on");
                    mBluetoothAdapter.disable();
                    showToast("Turing Bluetooth off");
                    mBlueIv.setImageResource(R.drawable.ic_action_off);
                }
            }
        });

        btnScan = (Button)findViewById(R.id.scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBluetoothAdapter.isEnabled()){
                    //showToast("Bluetooth is already off");
                    showToast("Turning On Bluetooth....");
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                }
                if(mBluetoothAdapter.isEnabled()){
                    Log.d("test", "startScan");
                    showLocation();
                    //        scanLeDevice(true);
                }
            }
        });

        btnLs = (Button) findViewById(R.id.btnHt);
        btnLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Lich_su_quet.class);
                Integer i, j;
                String data = "";
                i = 1;
                j = listBluetooth.size();

                while(i < j) {
                    data = data + listBluetooth.get(i) + "\n";
                    i += 1;
                }
                intent.putExtra("value", data);

                startActivity(intent);
            }
        });

        btnSupport = (Button) findViewById(R.id.btnSp);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Support_Activity.class);
                startActivity(intent);
            }
        });

        listViewLE = (ListView)findViewById(R.id.lelist);
        listBluetooth = new ArrayList<>();
        adapterLeScanResult = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listBluetooth);
        listViewLE.setAdapter(adapterLeScanResult);
       // listViewLE.setOnItemClickListener(scanResultOnItemClickListener);

        mHandler = new Handler();

        listViewLE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //i trả về vị trí click trong listview
                showToast((" " + listBluetooth.get(i)));

            }
        });

        /*listViewLE.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("Long click");

                protected void onListItemClick(ListView l, View v, int position, long id) {
                    super.onListItemClick(l, v, position, id);

                    Object obj = this.getListAdapter().getItem(position);
                    String value= obj.toString();

                    Intent intent= new Intent(CurrrentClass.this,NextClass.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                }
                return false;
            }
        });*/

        Log.d("vongdoi", "open app");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        getBluetoothAdapterAndLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            showToast("bluetoothManager.getAdapter()==null");
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getBluetoothAdapterAndLeScanner(){
        // Get BluetoothAdapter and BluetoothLeScanner.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        mScanning = false;
    }

    /*
    to call startScan (ScanCallback callback),
    Requires BLUETOOTH_ADMIN permission.
    Must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get results.
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //listBluetoothDevice.clear();
            listBluetooth.clear();
            listBluetooth.add("Các thiết bị Bluetooth xung quanh: ");
            listViewLE.invalidateViews();

            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothLeScanner.stopScan(scanCallback);
                    listViewLE.invalidateViews();

                    showToast("Scan timeout");
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    mScanning = false;
                    btnScan.setEnabled(true);
                }
            }, SCAN_PERIOD);

            mBluetoothLeScanner.startScan(scanCallback);
            mBlueIv.setImageResource(R.drawable.ic_action_scan);
            mScanning = true;
            btnScan.setEnabled(false);
        } else {
            mBluetoothLeScanner.stopScan(scanCallback);
            mScanning = false;
            btnScan.setEnabled(true);
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            addBluetoothDevice(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for(ScanResult result : results){
                addBluetoothDevice(result.getDevice());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            showToast("onScanFailed: " + String.valueOf(errorCode));
        }

        private void addBluetoothDevice(BluetoothDevice device){
            if(!listBluetooth.contains(device.getName() + " " + device.getAddress())){
                listBluetooth.add(device.getName() + " " + device.getAddress());
                listViewLE.invalidateViews();
            }
        }
    };

    private void showLocation(){
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    R.string.location_permission_available,
                    Snackbar.LENGTH_SHORT).show();
            //startCamera();
            scanLeDevice(true);
        } else {
            // Permission is missing and must be requested.
            setPermissionRequestLocation();
        }
        // END_INCLUDE(startCamera)
    }

    private void setPermissionRequestLocation() {
        // Bắt đầu hỏi quyền truy cập vị trí
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            // Hiển thị thông báo yêu cầu cấp quyền.
            Snackbar.make(mLayout, R.string.location_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
            }).show();
        } else {
            // Quyền truy cập chưa được cấp, hỏi trực tiêp người dùng.
            Snackbar.make(mLayout, R.string.location_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    protected void onDestroy() {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        //broadcastIntent.setClass(this, .class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        //    unregisterReceiver(myreceiver);
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}