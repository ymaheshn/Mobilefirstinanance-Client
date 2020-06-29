package interfaces;

import android.content.Intent;

public interface IOnDataRetrievedFromGallery {
    void onDataRetrieved(int requestCode, int resultCode, Intent data);
}
