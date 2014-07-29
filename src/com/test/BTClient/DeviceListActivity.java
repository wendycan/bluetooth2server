/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.BTClient;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint("NewApi")
public class DeviceListActivity extends Activity {
    // ������
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // ����ʱ��ݱ�ǩ
    public static String EXTRA_DEVICE_ADDRESS = "�豸��ַ";

    // ��Ա��
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ��������ʾ����
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  //���ô�����ʾģʽΪ���ڷ�ʽ
        setContentView(R.layout.device_list);

        // �趨Ĭ�Ϸ���ֵΪȡ��
        setResult(Activity.RESULT_CANCELED);

        // �趨ɨ�谴����Ӧ
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // ��ʹ���豸�洢����
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // ����������豸�б�
        
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // �����²����豸�б�
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // ע����ղ��ҵ��豸action������
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // ע����ҽ���action������
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // �õ������������
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // �õ�����������豸�б�
        //Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // ���������豸���б?��ʾ 
       // if (pairedDevices.size() > 0) {
           // findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
        //    for (BluetoothDevice device : pairedDevices) {
       //         mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
       //     }
       // } else {
       //     String noDevices = "No devices have been paired";
       //     mPairedDevicesArrayAdapter.add(noDevices);
       // }
    }

    @SuppressLint("NewApi")
	@Override
    protected void onDestroy() {
        super.onDestroy();

        // �رշ������
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // ע��action������
        this.unregisterReceiver(mReceiver);
    }
    
    public void OnCancel(View v){
    	finish();
    }
    /**
     * ��ʼ������豸����
     */
    @SuppressLint("NewApi")
	private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // �ڴ�����ʾ��������Ϣ
        setProgressBarIndeterminateVisibility(true);
        setTitle("�����豸��...");

        // ��ʾ�����豸��δ����豸���б�
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // �ر��ٽ��еķ������
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        //�����¿�ʼ
        mBtAdapter.startDiscovery();
    }

    // ѡ���豸��Ӧ���� 
    @SuppressLint("NewApi")
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // ׼�������豸���رշ������
            mBtAdapter.cancelDiscovery();

            // �õ�mac��ַ
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // ���÷������
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // ���÷���ֵ���������
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // ���ҵ��豸���������action������
    @SuppressLint("NewApi")
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.ECLAIR)
		@SuppressLint("NewApi")
		@Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // ���ҵ��豸action
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // �õ������豸
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // ���������Ե����Թ��ѵõ���ʾ�����������ӵ��б��н�����ʾ
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }else{  //��ӵ�������豸�б�
                	mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // �������action
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("ѡ��Ҫ���ӵ��豸");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = "û���ҵ����豸";
                    mNewDevicesArrayAdapter.add(noDevices);
                }
             //   if(mPairedDevicesArrayAdapter.getCount() > 0)
              //  	findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            }
        }
    };


}
