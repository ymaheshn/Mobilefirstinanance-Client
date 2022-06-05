package bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.List;

import loans.model.LinkedProfilesResponse;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewModel> {

    private Context mContext;
    private List<BluetoothDevice> bluetoothDevices;
    private IOnItemClickListener iOnItemClickListener;

    public DeviceListAdapter(Context mContext, List<BluetoothDevice> bluetoothDevices, IOnItemClickListener iOnItemClickListener) {
        this.bluetoothDevices = bluetoothDevices;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
        holder.textDeviceName.setText(bluetoothDevice.getName());
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public void addData(BluetoothDevice device) {
        if (device != null) {
            bluetoothDevices.add(device);
            notifyDataSetChanged();
        }
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView textDeviceName;

        public ViewModel(View itemView) {
            super(itemView);
            textDeviceName = itemView.findViewById(R.id.text_device_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnItemClickListener.onItemClicked(bluetoothDevices.get(getLayoutPosition()));
                }
            });
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(BluetoothDevice bluetoothDevice);
    }
}


