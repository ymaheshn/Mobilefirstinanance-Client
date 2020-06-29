package interfaces;


import android.os.Bundle;

/**
 * Created by gufran khan on 10-06-2018.
 */

public interface IOnFragmentChangeListener {
    void onHeaderUpdate(int fragmentName, String title);

    void onFragmentChanged(int fragmentName, Bundle intentExtra);

    void setHeaderItemClickListener(IOnHeaderItemsClickListener iOnHeaderItemsClickListener);
}
