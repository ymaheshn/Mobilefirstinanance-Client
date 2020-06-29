package Utilities;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gufran khan on 23-06-2018.
 */

public class UtilityMethods {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static final String KYC_DATE_FORMAT = "YYYY-MM-dd";

    public static Date getDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateFormat(String dateFormat) {
        return getDateFormat(dateFormat, "MM-dd-yyyy");
    }

    public static String getDateFormat(String dateFormat, String outputPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
        try {
            Date date = simpleDateFormat.parse(dateFormat);
            String format = new SimpleDateFormat(outputPattern).format(date);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Date getDateFromString(String dateFormat, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date date = simpleDateFormat.parse(dateFormat);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert mConnectivityManager != null;
        return mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || mConnectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static boolean emailValidate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean phoneNumberValidate(String phoneNumber) {

        return phoneNumber.length() == 10;

    }

    public static String getDateFormat() {
        String pattern = "YYYY-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    /**
     * Checks whether the gps is enabled or not
     *
     * @param context context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
         /*In few devices Location/GPS method has 3 modes- HighAccuracy, BatterySaving and DeviceOnly.
         If BatterySaving mode is on - device rely only on Wifi/Network instead of gps to estimate current location.
                                       In this case only LocationManager.NETWORK_PROVIDER returns true, if location is enabled
         If DeviceOnly mode is on - We can see location icon in notification bar
                                    In this case only LocationManager.GPS_PROVIDER returns true, if location is enabled
         In HighAccuracy mode - Either network or gps provider returns true, if location is enabled.
         */
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}

