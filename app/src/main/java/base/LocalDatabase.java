package base;

import com.prowesspride.api.Setup;

public class LocalDatabase {

    private static LocalDatabase instance;
    private Setup setup;

    private LocalDatabase() {

    }

    public static LocalDatabase getInstance() {
        if (instance == null) {
            instance = new LocalDatabase();
        }
        return instance;
    }


    public void setBluetoothSetup(Setup setup) {
        this.setup = setup;
    }

    public Setup getBluetoothSetup() {
        return setup;
    }
}
