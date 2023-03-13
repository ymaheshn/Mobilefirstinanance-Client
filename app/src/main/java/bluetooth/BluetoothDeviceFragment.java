package bluetooth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.odedtech.mff.client.R;
import com.prowesspride.api.Printer_GEN;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import bluetooth.evolute.BluetoothComm;
import bluetooth.evolute.BluetoothPair;
import loans.model.ContractCodes;
import loans.model.LoanContractCodes;
import networking.MyApplication;

@SuppressLint("ValidFragment")
public class BluetoothDeviceFragment extends DialogFragment {

    private int recipetId;
    private DeviceConnected deviceConnected;
    private ContractCodes contractCodes;
    private LoanContractCodes contractCodes1;
    private int collectedAmount;
    private MyApplication myApplication;

    /**
     * CONST: device type bluetooth 2.1
     */
    public static final int DEVICE_TYPE_BREDR = 0x01;
    /**
     * CONST: device type bluetooth 4.0 ble
     */
    public static final int DEVICE_TYPE_BLE = 0x02;
    /**
     * CONST: device type bluetooth double mode
     */
    public static final int DEVICE_TYPE_DUMO = 0x03;
    public final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";

    public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothDevice mBDevice = null;
    private boolean _discoveryFinished;
    /**
     * Storage the found bluetooth devices
     * format:<MAC, <Key,Val>>;Key=[RSSI/NAME/COD(class od device)/BOND/UUID]
     */
    private Hashtable<String, Hashtable<String, String>> mhtFDS = null;
    private int iCurrDev;
    private ArrayList<HashMap<String, Object>> malListItem;
    private SimpleAdapter msaListItemAdapter;
    private ListView mlvList;
    private InputStream input;
    private OutputStream outstream;
    private Printer_GEN prnGen;
    private String sDevicetype;
    private String type;
    private Integer iRetVal;
    private boolean mblBonded;
    private int interest;
    private int pricipal;
    private int total;
    private Context context;
    private String eventType;

    public interface DeviceConnected {
        void deviceConnected(boolean isConnected);

        void printSuccessfully();
    }


    public BluetoothDeviceFragment() {

    }

    /*This is for Payments*/
    @SuppressLint("ValidFragment")
    public BluetoothDeviceFragment(DeviceConnected deviceConnected, ContractCodes contractCodes, int collectedAmount,
                                   int interest, int pricipal, int total, String eventType, int recipetId, String type, Context context) {
        this.recipetId = recipetId;
        this.deviceConnected = deviceConnected;
        this.contractCodes = contractCodes;
        this.collectedAmount = collectedAmount;
        this.interest = interest;
        this.pricipal = pricipal;
        this.total = total;
        this.eventType = eventType;
        this.type = type;
        this.context = context;
    }

    /*This is for Accounts */
    @SuppressLint("ValidFragment")
    public BluetoothDeviceFragment(DeviceConnected deviceConnected, LoanContractCodes contractCodes, int collectedAmount,
                                   int interest, int pricipal, int total, String eventType, int recipetId, String type, Context context) {
        this.recipetId = recipetId;
        this.deviceConnected = deviceConnected;
        this.contractCodes1 = contractCodes;
        this.collectedAmount = collectedAmount;
        this.interest = interest;
        this.pricipal = pricipal;
        this.total = total;
        this.eventType = eventType;
        this.type = type;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bluetooth_device_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myApplication = (MyApplication) requireActivity().getApplication();

        mlvList = view.findViewById(R.id.rv_devices_list);
        mlvList.setOnItemClickListener((parent, view1, position, id) -> {
            String sMAC = ((TextView) view1.findViewById(R.id.device_item_ble_mac)).getText().toString();
            mBDevice = mBT.getRemoteDevice(sMAC);
            if (mBDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                // It is not paired and we need to pair and connect
                new PairTask().execute(mBDevice.getAddress());
            } else {
                new ConnSocketTask().execute(mBDevice.getAddress());
            }
        });
        if (!myApplication.connection) {
            new StartBluetoothDeviceTask().execute();
        } else {
            genGetSerialNo genSerial = new genGetSerialNo();
            genSerial.execute(0);
        }
    }

    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    mblBonded = true;
                else
                    mblBonded = false;
            }
        }
    };

    /*   This method shows the PairTask  PairTask operation */
    private class PairTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: the pairing is successful
         */
        static private final int RET_BOND_OK = 0x00;
        /**
         * Constants: Pairing failed
         */
        static private final int RET_BOND_FAIL = 0x01;
        /**
         * Constants: Pairing waiting time (15 seconds)
         */
        static private final int iTIMEOUT = 1000 * 15;
        private ProgressDialog mpd;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {
            this.mpd = new ProgressDialog(getActivity());
            this.mpd.setMessage("Pairing..");
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
            getActivity().registerReceiver(_mPairingRequest, new IntentFilter(BluetoothPair.PAIRING_REQUEST));
            getActivity().registerReceiver(_mPairingRequest, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }

        /* Task of PairTask performing in the background*/
        @Override
        protected Integer doInBackground(String... arg0) {
            final int iStepTime = 150;
            int iWait = iTIMEOUT;
            try {
                mBDevice = mBT.getRemoteDevice(arg0[0]);//arg0[0] is MAC address
                BluetoothPair.createBond(mBDevice);
                mblBonded = false;
            } catch (Exception e1) {
                Log.d(getString(R.string.app_name), "create Bond failed!");
                e1.printStackTrace();
                return RET_BOND_FAIL;
            }
            while (!mblBonded && iWait > 0) {
                SystemClock.sleep(iStepTime);
                iWait -= iStepTime;
            }
            return (int) ((iWait > 0) ? RET_BOND_OK : RET_BOND_FAIL);
        }

        /* This displays the status messages of PairTask in the dialog box */
        @Override
        public void onPostExecute(Integer result) {
            this.mpd.dismiss();
            getActivity().unregisterReceiver(_mPairingRequest);
            if (RET_BOND_OK == result) {
                Toast.makeText(getActivity(), "Pairing Successful", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Toast.makeText(getActivity(), "Pairing Un-Successful, try again.", Toast.LENGTH_SHORT).show();
                    BluetoothPair.removeBond(mBDevice);
                } catch (Exception e) {
                    Log.d(getString(R.string.app_name), "removeBond failed!");
                    e.printStackTrace();
                }
            }
        }
    }


    private ProgressDialog progressDialog;

    public void showLoading() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), (int) (size.y * 0.4));
        window.setGravity(Gravity.CENTER);
    }

    public void addDevice(BluetoothDevice device) {
    }


    // Turn on Bluetooth of the device
    private class StartBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        private static final int miWATI_TIME = 15;
        private static final int miSLEEP_TIME = 150;
        // private static final Integer TODO = ;
        private ProgressDialog mpd;

        @Override
        public void onPreExecute() {
            mpd = new ProgressDialog(getActivity());
            mpd.setMessage("Bluetooth is turned on..");
            mpd.setCancelable(false);
            mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            boolean blBleStatusBefore = mBT.isEnabled();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME * 1000;
            /* BT isEnable */
            if (!mBT.isEnabled()) {
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while (iWait > 0) {
                    if (!mBT.isEnabled())
                        iWait -= miSLEEP_TIME;
                    else
                        break;
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0)
                    return RET_BLUETOOTH_START_FAIL;
            }
            return RET_BULETOOTH_IS_START;
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {
            if (mpd.isShowing())
                mpd.dismiss();
            if (RET_BLUETOOTH_START_FAIL == result) {
                // Turning ON Bluetooth failed
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bluetooth turn on failed");
                builder.setPositiveButton(R.string.zxing_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBT.disable();
                        dismiss();
                    }
                });
                builder.create().show();
            } else if (RET_BULETOOTH_IS_START == result) {
//                Intent intent = new Intent(getActivity(), Act_BTDiscovery.class);
//                Act_Main.this.startActivityForResult(intent, REQUEST_DISCOVERY);
                // start scannning and add the bluetooth devices to the
                new scanDeviceTask().execute();
            }
        }
    }

    private class scanDeviceTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: Bluetooth is not turned on
         */
        private static final int RET_BLUETOOTH_NOT_START = 0x0001;
        /**
         * Constant: the device search complete
         */
        private static final int RET_SCAN_DEVICE_FINISHED = 0x0002;
        /**
         * Wait a Bluetooth device starts the maximum time (in S)
         */
        private static final int miWATI_TIME = 10;
        /**
         * Every thread sleep time (in ms)
         */
        private static final int miSLEEP_TIME = 150;
        /**
         * Process waits prompt box
         */
        private ProgressDialog mpd = null;


        @Override
        public void onPreExecute() {
			/*if (wifiManager.isWifiEnabled()) {
				Log.e("Bt", "wifi is on..");
				 bWificheck=true;
				wifiManager.setWifiEnabled(false);
			} */
            this.mpd = new ProgressDialog(getActivity());
            this.mpd.setMessage("Searching for Bluetooth devices,please wait");
            this.mpd.setCancelable(true);
            this.mpd.setCanceledOnTouchOutside(true);
            this.mpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    _discoveryFinished = true;
                }
            });
            this.mpd.show();
            startSearch();
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (!mBT.isEnabled())
                return RET_BLUETOOTH_NOT_START;

            int iWait = miWATI_TIME * 3000;
            //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
            while (iWait > 0) {
                if (_discoveryFinished)
                    return RET_SCAN_DEVICE_FINISHED;
                else
                    iWait -= miSLEEP_TIME;
                SystemClock.sleep(miSLEEP_TIME);
                ;
            }
            return RET_SCAN_DEVICE_FINISHED;
        }

        @Override
        public void onProgressUpdate(String... progress) {
        }

        @Override
        public void onPostExecute(Integer result) {

            if (this.mpd.isShowing())
                this.mpd.dismiss();

            if (mBT.isDiscovering())
                mBT.cancelDiscovery();

		/*	if (bWificheck) {
				Log.e(TAG,"Bt wifi is on.."+bWificheck);
				wifiManager.setWifiEnabled(true);
			} */

            if (RET_SCAN_DEVICE_FINISHED == result) {

            } else if (RET_BLUETOOTH_NOT_START == result) {
                Toast.makeText(getActivity(), "Bluetooth is not enabled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Show devices list */
    protected void showDevices() {
        if (null == this.malListItem)
            this.malListItem = new ArrayList<HashMap<String, Object>>();

        if (null == this.msaListItemAdapter) {
            //Generate adapter Item and dynamic arrays corresponding element
            this.msaListItemAdapter = new SimpleAdapter(getActivity(), malListItem,//Data Sources
                    R.layout.list_view_item_devices,//ListItem's XML implementation
                    //Child corresponding dynamic arrays and ImageItem
                    new String[]{"NAME", "MAC", "COD", "RSSI", "DEVICE_TYPE", "BOND"},
                    //A ImageView ImageItem XML file inside, two TextView ID
                    new int[]{R.id.device_item_ble_name,
                            R.id.device_item_ble_mac,
                            R.id.device_item_ble_cod,
                            R.id.device_item_ble_rssi,
                            R.id.device_item_ble_device_type,
                            R.id.device_item_ble_bond
                    }
            );
            this.mlvList.setAdapter(this.msaListItemAdapter);
        }


        this.malListItem.clear();//Clear history entries
        Enumeration<String> e = this.mhtFDS.keys();

        while (e.hasMoreElements()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String sKey = e.nextElement();
            map.put("MAC", sKey);
            String name = !TextUtils.isEmpty(this.mhtFDS.get(sKey).get("NAME")) ?
                    this.mhtFDS.get(sKey).get("NAME") : sKey;
            if (!TextUtils.isEmpty(name) && name.equalsIgnoreCase("NULL")) {
                return;
            }
            map.put("NAME", name);
            map.put("RSSI", "RSSI");
            map.put("COD", "COD");
            map.put("BOND", this.mhtFDS.get(sKey).get("BOND"));
            map.put("DEVICE_TYPE", "DEVICE");
            this.malListItem.add(map);
        }
        this.msaListItemAdapter.notifyDataSetChanged();
    }

    private String toDeviceTypeString(String sDeviceTypeId) {
        Pattern pt = Pattern.compile("^[-\\+]?[\\d]+$");
        if (pt.matcher(sDeviceTypeId).matches()) {
            switch (Integer.valueOf(sDeviceTypeId)) {
                case DEVICE_TYPE_BREDR:
                    return "BR/EDR Bluetooth";
                case DEVICE_TYPE_BLE:
                    return "Bluetooth Energy is Low";
                case DEVICE_TYPE_DUMO:
                    return "Duplex mode Bluetooth";
                default:
                    return "BR/EDR Bluetooth";
            }
        } else
            return sDeviceTypeId;
    }

    private void startSearch() {
        _discoveryFinished = false;


        if (null == mhtFDS)
            this.mhtFDS = new Hashtable<String, Hashtable<String, String>>();
        else
            this.mhtFDS.clear();

        /* Register Receiver*/
        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(_finshedReceiver, discoveryFilter);
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(_foundReceiver, foundFilter);
        mBT.startDiscovery();//start scan
        //the first scan clear show list
    }

    private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, final Intent intent) {
            new Thread() {
                public void run() {
                    Log.e("Prow App ", "_foundReceiver : " + iCurrDev);
                    iCurrDev++;
                    /* bluetooth device profiles*/
                    Hashtable<String, String> htDeviceInfo = new Hashtable<String, String>();

                    Log.d(getString(R.string.app_name), ">>Scan for Bluetooth devices");

                    /* get the search results */
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    /* create found device profiles to htDeviceInfo*/
                    Bundle b = intent.getExtras();
                    htDeviceInfo.put("RSSI", String.valueOf(b.get(BluetoothDevice.EXTRA_RSSI)));
                    if (null == device.getName())
                        htDeviceInfo.put("NAME", device.getAddress());
                    else
                        htDeviceInfo.put("NAME", device.getName());

                    htDeviceInfo.put("COD", String.valueOf(b.get(BluetoothDevice.EXTRA_CLASS)));
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                        htDeviceInfo.put("BOND", "Bonded");
                    else
                        htDeviceInfo.put("BOND", "Nothing");

                    String sDeviceType = String.valueOf(b.get(EXTRA_DEVICE_TYPE));
                    if (!sDeviceType.equals("null"))
                        htDeviceInfo.put("DEVICE_TYPE", sDeviceType);
                    else
                        htDeviceInfo.put("DEVICE_TYPE", "-1");

                    /*adding scan to the device profiles*/
                    mhtFDS.put(device.getAddress(), htDeviceInfo);

                    /*Refresh show list*/
                    myHandler.obtainMessage(987).sendToTarget();
                }
            }.start();
        }
    };

    //////////////////
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 987:
                    showDevices();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * Bluetooth scanning is finished processing.(broadcast listener)
     */
    private BroadcastReceiver _finshedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _discoveryFinished = true; //set scan is finished
            getActivity().unregisterReceiver(_foundReceiver);
            getActivity().unregisterReceiver(_finshedReceiver);

            if (intent.getAction().equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Set<BluetoothDevice> bondedDevices = mBT.getBondedDevices();
                Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
                while (iterator.hasNext()) {
                    BluetoothDevice device = iterator.next();
                    /* bluetooth device profiles*/
                    Hashtable<String, String> htDeviceInfo = new Hashtable<String, String>();

                    Log.d(getString(R.string.app_name), ">>Scan for Bluetooth devices");

//                    /* get the search results */
//                    /* create found device profiles to htDeviceInfo*/
//                    htDeviceInfo.put("RSSI", String.valueOf(b.get(BluetoothDevice.EXTRA_RSSI)));
//                    if (null == device.getName())
//                        htDeviceInfo.put("NAME", "Null");
//                    else
                    htDeviceInfo.put("NAME", device.getName());
//
//                    htDeviceInfo.put("COD", String.valueOf(b.get(BluetoothDevice.EXTRA_CLASS)));
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                        htDeviceInfo.put("BOND", "Bonded");
                    else
                        htDeviceInfo.put("BOND", "Nothing");
//
//                    String sDeviceType = String.valueOf(b.get(EXTRA_DEVICE_TYPE));
//                    if (!sDeviceType.equals("null"))
//                        htDeviceInfo.put("DEVICE_TYPE", sDeviceType);
//                    else
//                        htDeviceInfo.put("DEVICE_TYPE", "-1");

                    /*adding scan to the device profiles*/
                    mhtFDS.put(device.getAddress(), htDeviceInfo);
                }
            }

            if (null != mhtFDS && mhtFDS.size() > 0) {
                Toast.makeText(getActivity(),
                        "Select the device you need to connect",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),
                        "Devices not found",
                        Toast.LENGTH_LONG).show();
            }
            myHandler.obtainMessage(987).sendToTarget();
        }
    };


    /*   This method shows the   PairTask operation */
    private class ConnSocketTask extends AsyncTask<String, String, Integer> {
        /**
         * Process waits prompt box
         */
        private ProgressDialog mpd = null;
        /**
         * Constants: connection fails
         */
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {
            this.mpd = new ProgressDialog(getActivity());
            this.mpd.setMessage("Connecting..");
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
        }

        /* Task of  performing in the background*/
        @Override
        protected Integer doInBackground(String... arg0) {
            if (myApplication.createConn(arg0[0])) {
                SystemClock.sleep(2000);
                return CONN_SUCCESS;
            } else {
                return CONN_FAIL;
            }
        }

        /* This displays the status messages of in the dialog box */
        @Override
        public void onPostExecute(Integer result) {
            this.mpd.dismiss();

            if (CONN_SUCCESS == result) {
                //wifiManager.setWifiEnabled(true);//To Enable wifi in mobile
                // connection is successfull so start printing
                if (myApplication.connection) {
                    genGetSerialNo genSerial = new genGetSerialNo();
                    genSerial.execute(0);
                }
            } else {
                Toast.makeText(getActivity(), "Connection Failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class genGetSerialNo extends AsyncTask<Integer, Integer, String> {

        /**
         * Process waits prompt box
         */
        private ProgressDialog mpd = null;
        /**
         * Constants: connection fails
         */
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;

        /**
         * Thread start initialization
         */

        @Override
        protected void onPreExecute() {
            this.mpd = new ProgressDialog(getActivity());
            this.mpd.setMessage("Please wait...");
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();

        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                try {
                    input = BluetoothComm.misIn;
                    outstream = BluetoothComm.mosOut;
                    prnGen = new Printer_GEN(MyApplication.setup, outstream, input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sDevicetype = prnGen.sGetSerialNumber();
                Toast.makeText(getActivity(), "Serial No. is " + sDevicetype, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return sDevicetype;
        }

        @Override
        protected void onPostExecute(String result) {
            this.mpd.dismiss();
            // Start printing the format
            new EnterTextAsyc().execute();
        }
    }

    public static final int DEVICE_NOTCONNECTED = -100;

    /* This method shows the EnterTextAsyc AsynTask operation */
    public class EnterTextAsyc extends AsyncTask<Integer, Integer, Integer> {
        private ProgressDialog mpd;

        /* displays the progress dialog untill background task is completed */
        @Override
        protected void onPreExecute() {

            mpd = new ProgressDialog(getActivity());
            mpd.setMessage("Printing..");
            mpd.setCancelable(false);
            mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            super.onPreExecute();
        }

        /* Task of EnterTextAsyc performing in the background */
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                prnGen.iFlushBuf();
                String entityName = PreferenceConnector.readString(requireActivity(), getString(R.string.entityname), "");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, entityName);
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Receipt No: " + recipetId);
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Date:" + UtilityMethods.getDateFormat());
                if (type.equalsIgnoreCase("1")) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Name: " + contractCodes.name);

                    // TODO: Dont miuss this
                    // ptr.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "LAST NAME:" + ll.getLastName());
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "CC: " + contractCodes.contractUUID);
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Client ID: " + contractCodes.identifier);
                } else {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Name: " + contractCodes1.getName());

                    // TODO: Dont miuss this
                    // ptr.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "LAST NAME:" + ll.getLastName());
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "CC: " + contractCodes1.getContractUUID());
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Client ID: " + contractCodes1.getIdentifier());
                }

                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Principal:       " + pricipal + ".00");
                if (eventType.equals("nullFP")) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Fee:             " + eventType.substring(4));
                } else if (eventType.equals("nullIP")) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Interest:         " + eventType.substring(4));
                } else if (eventType.equals("nullPR")) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Principal:         " + eventType.substring(4));
                } else if (eventType.equals("nullIED")) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Loan Amount:         " + eventType.substring(4));
                } else if (eventType == null) {
                    prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Interest:         " + interest + ".00");
                }

                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Total Amount:    " + total + ".00");
//                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Collected Amount:" + collectedAmount + ".0");
//                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "Collected Amount:" + collectedAmount + "0");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "       THANK YOU       ");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "-----------------------");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "\n");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "\n");
                prnGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "\n");

                iRetVal = prnGen.iStartPrinting(1);
            } catch (NullPointerException e) {
                iRetVal = DEVICE_NOTCONNECTED;
                return iRetVal;
            }
            return iRetVal;
        }

        /* This displays the status messages of EnterTextAsyc in the dialog box */
        @Override
        protected void onPostExecute(Integer result) {
            mpd.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                ptrHandler.obtainMessage(1, "Device not connected")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.SUCCESS) {
                ptrHandler.obtainMessage(1, "Print Successfull")
                        .sendToTarget();
                dismiss();
                deviceConnected.printSuccessfully();
            } else if (iRetVal == Printer_GEN.PLATEN_OPEN) {
                ptrHandler.obtainMessage(1, "Platen open").sendToTarget();
            } else if (iRetVal == Printer_GEN.PAPER_OUT) {
                ptrHandler.obtainMessage(1, "Paper out").sendToTarget();
            } else if (iRetVal == Printer_GEN.IMPROPER_VOLTAGE) {
                ptrHandler.obtainMessage(1, "Printer at improper voltage")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.FAILURE) {
                ptrHandler.obtainMessage(1, "Print failed").sendToTarget();
            } else if (iRetVal == Printer_GEN.PARAM_ERROR) {
                ptrHandler.obtainMessage(1, "Parameter error").sendToTarget();
            } else if (iRetVal == Printer_GEN.NO_RESPONSE) {
                ptrHandler.obtainMessage(1, "No response from Pride device")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.DEMO_VERSION) {
                ptrHandler.obtainMessage(1, "Library in demo version")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.INVALID_DEVICE_ID) {
                ptrHandler.obtainMessage(1,
                                "Connected  device is not authenticated.")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.NOT_ACTIVATED) {
                ptrHandler.obtainMessage(1, "Library not activated")
                        .sendToTarget();
            } else if (iRetVal == Printer_GEN.NOT_SUPPORTED) {
                ptrHandler.obtainMessage(1, "Not Supported").sendToTarget();
            } else {
                ptrHandler.obtainMessage(1, "Unknown Response from Device")
                        .sendToTarget();
            }
            super.onPostExecute(result);
        }
    }

    /* Handler to display UI response messages */
    Handler ptrHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    //   Toast.makeText(context.getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    String str1 = (String) msg.obj;
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG)
                            .show();
                    break;
                case 3:
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG)
                            .show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

}
