package networking;

import android.app.Application;
import android.location.Location;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.prowesspride.api.Setup;

import bluetooth.evolute.BluetoothComm;

/**
 * Created by gufran khan on 22-06-2018.
 */

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;
    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack(null,
                    ClientSSLSocketFactory.getSocketFactory()));
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Bluetooth communication connection object
     */
    public BluetoothComm mBTcomm = null;
    public boolean connection = false;
    public static Setup setup = null;

    /**
     * Set up a Bluetooth connection
     *
     * @param sMac Bluetooth hardware address
     * @return Boolean
     */
    public boolean createConn(String sMac) {
        if (null == this.mBTcomm) {
            this.mBTcomm = new BluetoothComm(sMac);
            if (this.mBTcomm.createConn()) {
                connection = true;
                return true;
            } else {
                this.mBTcomm = null;
                connection = false;
                return false;
            }
        } else
            return true;
    }

    /**
     * Close and release the connection
     *
     * @return void
     */
    public void closeConn() {
        if (null != this.mBTcomm) {
            this.mBTcomm.closeConn();
            this.mBTcomm = null;
        }
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }
}
