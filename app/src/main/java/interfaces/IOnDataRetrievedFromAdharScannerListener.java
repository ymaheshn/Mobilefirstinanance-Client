package interfaces;

import android.content.Intent;

/**
 * Created by gufran khan on 16-06-2018.
 */

public interface IOnDataRetrievedFromAdharScannerListener {
    void onAdharDataRetrieved(int requestCode, int resultCode, Intent data);
}
