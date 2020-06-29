package networking;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */

    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.timeout);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);

        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.nointernet);
        }
        return context.getResources().getString(R.string.generic_error);

    }

    private static String handleServerError(Object error, Context context) {

        VolleyError er = (VolleyError) error;
        NetworkResponse response = er.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 400:
                case 412:
                    try {
                        JSONObject jsonObject = new JSONObject(new String(response.data));
                        if (jsonObject.has("message")) {
                            return jsonObject.getString("message");
                        } else if (jsonObject.has("Status")) {
                            return jsonObject.getString("Status");
                        }
                        return "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return ((VolleyError) error).getMessage();
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        LinkedTreeMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            if(result.get("error").equalsIgnoreCase("invalid_token")){
                                return "AuthFailureError";
                            }
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return ((VolleyError) error).getMessage();

                default:
                    return context.getResources().getString(R.string.generic_error);
            }
        }

        return context.getResources().getString(R.string.generic_error);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }
}