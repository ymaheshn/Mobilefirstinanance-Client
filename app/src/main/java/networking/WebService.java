package networking;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gufran khan on 22-06-2018.
 */

public class WebService {

    private static final int MY_SOCKET_TIMEOUT_MS = 5 * 60 * 1000;

    private static WebService webService = null;

    public static WebService getInstance() {
        if (webService == null) {
            webService = new WebService();
        }
        return webService;
    }

    public void apiPostJsonRequest(final String url, final JSONArray params, final OnServiceResponseListener onServiceResponseListener) {
        JsonRequest<JSONObject> jsonRequest = new JsonRequest<JSONObject>(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley success", response.toString());
                onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
                String message = VolleyErrorHelper
                        .getMessage(error, MyApplication.getInstance());
                if (TextUtils.isEmpty(message)) {
                    message = "Oops!! Something went wrong";
                }
                onServiceResponseListener.onApiCallResponseFailure(message);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    public void apiGetRequestCallJSON(final String url, final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        Log.d("url", "url :" + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Volley success", response.toString());
                    onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
                },
                error -> {
                    Log.d("Volley error", error.toString());
                    String message = VolleyErrorHelper
                            .getMessage(error, MyApplication.getInstance());
                    if (TextUtils.isEmpty(message)) {
                        message = "Oops!! Something went wrong";
                    }
                    onServiceResponseListener.onApiCallResponseFailure(message);
                });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    public void apiGetRequestCall(final String url, final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        // Request a string response from the provided URL.
        Log.d("url", "url :" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Volley success", response);
                    onServiceResponseListener.onApiCallResponseSuccess(url, response);
                }, error -> {

            Log.d("Volley error", error.toString());
            String message = VolleyErrorHelper
                    .getMessage(error, MyApplication.getInstance());
            if (TextUtils.isEmpty(message)) {
                message = "Oops!! Something went wrong";
            }
            onServiceResponseListener.onApiCallResponseFailure(message);
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public void apiPostRequestCall(final String url, final Map<String, String> params, final OnServiceResponseListener onServiceResponseListener) {
        apiPostRequestCallJSON(url, new JSONObject(params), onServiceResponseListener);
    }

    public void apiPostRequestCallJSON(final String url, final JSONObject params, final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                String message = VolleyErrorHelper
                        .getMessage(error, MyApplication.getInstance());
                if (TextUtils.isEmpty(message)) {
                    message = "";
                }
                onServiceResponseListener.onApiCallResponseFailure(message);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void apiPostRequestCallJSONArray2(final String url, final JSONArray params, final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, response -> {
            Log.i("VOLLEY", response.toString());
            onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
        }, error -> {
            Log.e("VOLLEY", error.toString());
            String message = VolleyErrorHelper
                    .getMessage(error, MyApplication.getInstance());
            if (TextUtils.isEmpty(message)) {
                message = "";
            }
            onServiceResponseListener.onApiCallResponseFailure(message);
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                try {
                    return params.toString().getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException uee) {
                    // error handling
                    return null;
                }
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

    }


    public void apiPostRequestCallJSONArray(final String url, final JSONArray params,
                                            final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            Log.i("VOLLEY", response.toString());
            onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                String message = VolleyErrorHelper
                        .getMessage(error, MyApplication.getInstance());
                if (TextUtils.isEmpty(message)) {
                    message = "";
                }
                onServiceResponseListener.onApiCallResponseFailure(message);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                try {
                    return params.toString().getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException uee) {
                    // error handling
                    return null;
                }
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void apiPutRequestCallJSON(final String url, final JSONObject params,
                                      final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                onServiceResponseListener.onApiCallResponseSuccess(url, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                String message = VolleyErrorHelper
                        .getMessage(error, MyApplication.getInstance());
                if (TextUtils.isEmpty(message)) {
                    message = "";
                }
                onServiceResponseListener.onApiCallResponseFailure(message);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void apiPutRequestCall(final String url, final Map<String, String> params,
                                  final OnServiceResponseListener onServiceResponseListener) {
        apiPutRequestCallJSON(url, new JSONObject(params), onServiceResponseListener);
    }


    public void apiMultipart(String url, Map<String, VolleyMultipartRequest.DataPart> params,
                             final OnServiceResponseListener onServiceResponseListener) {
        HttpsTrustManager.allowAllSSL();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                onServiceResponseListener.onApiCallResponseSuccess(url, resultResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                String message = VolleyErrorHelper
                        .getMessage(error, MyApplication.getInstance());
                if (TextUtils.isEmpty(message)) {
                    message = "";
                }
                onServiceResponseListener.onApiCallResponseFailure(message);
            }
        }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(multipartRequest);
    }

    public interface OnServiceResponseListener {
        void onApiCallResponseSuccess(String url, String object);

        void onApiCallResponseFailure(String errorMessage);
    }
}
