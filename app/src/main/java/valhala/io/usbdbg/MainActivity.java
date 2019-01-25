package valhala.io.usbdbg;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements Runnable {

    private UsbManager manager;
    private HashMap<String, UsbDevice> deviceList;
    private Iterator<UsbDevice> deviceIterator;
    private UsbDevice device;
    private UsbInterface intf;
    private UsbEndpoint endpoint;
    private UsbDeviceConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        deviceList = manager.getDeviceList();
        deviceIterator = deviceList.values().iterator();

        while(deviceIterator.hasNext()) {
            device = deviceIterator.next();

            intf = device.getInterface(0);
            endpoint = intf.getEndpoint(0);


            con = manager.openDevice(device);
            if(null == con) {
                Log.e("FAIL: ", "Couldn't connect to device");
            }
            else {
                con.claimInterface(intf, true);
            }
        }

        setDevice(device);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void setDevice(UsbDevice device) {
        this.device = device;
        if(device.getInterfaceCount() != 1) {
            Log.e("ERROR: ", "Could not find interface!");
            return;
        }

        if(intf.getEndpointCount() != 1) {
            Log.e("ERROR: ", "Could not find endpoint!");
            return;
        }

        if(endpoint.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {
            Log.e("ERROR: ", "Endpoint is not type interrupt");
            return;
        }

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {



    }

}



