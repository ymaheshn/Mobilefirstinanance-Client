package database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import onboard.ClientDataDTO;

import static database.db.DBOperations.TABLE_CLIENT_DATA;
import static database.db.DBOperations.TABLE_FORM_DATA;

/**
 * Created by gufran khan on 23-07-2018.
 */

public class ClientDataDAO {
    private static ClientDataDAO clientDataDAO = null;

    private ClientDataDAO() {

    }

    public static ClientDataDAO getInstance() {
        if (clientDataDAO == null) {
            clientDataDAO = new ClientDataDAO();
        }
        return clientDataDAO;
    }

    public void insertOrUpdateClientKyc(SQLiteDatabase dbObject, String kycData, int clientId) {
        try {
            if (clientId <= 0) {
                ContentValues values = new ContentValues();
                values.put("kyc_data", kycData);
                dbObject.insert(TABLE_FORM_DATA, null, values);
            } else {
                String strSQL = "UPDATE " + TABLE_FORM_DATA + " SET kyc_data = '" + kycData + "' WHERE client_id = " + clientId;
                dbObject.execSQL(strSQL);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dbObject != null && dbObject.isOpen()) {
                dbObject.close();
            }
        }
    }

    public ArrayList<ClientDataDTO> getAllClientsFromDB(SQLiteDatabase dbObject) {
        ArrayList<ClientDataDTO> clientDataDTOS = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_CLIENT_DATA;
            Cursor cursor = dbObject.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ClientDataDTO clientDataDTO = new ClientDataDTO();
                    clientDataDTO.formId = cursor.getInt(0);
                    clientDataDTO.kycData = cursor.getString(1);
                    clientDataDTOS.add(clientDataDTO);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dbObject != null && dbObject.isOpen()) {
                dbObject.close();
            }
        }
        return clientDataDTOS;
    }
}
