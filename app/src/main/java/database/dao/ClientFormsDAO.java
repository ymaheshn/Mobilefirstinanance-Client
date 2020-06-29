package database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import database.db.DBOperations;

/**
 * Created by gufran khan on 23-07-2018.
 */

public class ClientFormsDAO {
    private static ClientFormsDAO clientFormsDAO = null;

    private ClientFormsDAO() {

    }

    public static ClientFormsDAO getInstance() {
        if (clientFormsDAO == null) {
            clientFormsDAO = new ClientFormsDAO();
        }
        return clientFormsDAO;
    }

    public boolean insertOrUpdateForm(SQLiteDatabase dbObject, JSONArray formJson) {
        try {
            for (int i = 0; i < formJson.length(); i++) {
                JSONObject obj = formJson.getJSONObject(i);
                String query = "INSERT OR REPLACE INTO " + DBOperations.TABLE_FORM_DATA + " (json_data) VALUES ('" + obj.toString() + "')";
                SQLiteStatement stmt = dbObject.compileStatement(query);
                stmt.executeInsert();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dbObject != null && dbObject.isOpen()) {
                dbObject.close();
            }
        }
        return false;
    }

    public String getOfflineForms(SQLiteDatabase dbObject) {
        JSONArray jsonArray = new JSONArray();
        String query = "select * from " + DBOperations.TABLE_FORM_DATA;

        Cursor res = dbObject.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(res.getString(res.getColumnIndex("json_data")));
            } catch (JSONException e) {
                Log.e("dbhelper jarry", ">>>>>>>>>>>>>>" + e.getLocalizedMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dbObject != null && dbObject.isOpen()) {
                    dbObject.close();
                }
            }
            jsonArray.put(jsonObject);
            res.moveToNext();
        }
        return jsonArray.toString();
    }

    public void deleteOldForms(SQLiteDatabase dbObject) {
        dbObject.execSQL("delete from " + DBOperations.TABLE_FORM_DATA);
    }
}
