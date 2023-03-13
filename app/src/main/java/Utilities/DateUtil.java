package Utilities;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static Integer[] thirtyonedayss = {26, 27, 28, 29, 30, 31, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private static Integer[] thirtyayss = {26, 27, 28, 29, 30, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private static Integer[] twentyEdightdayss = {26, 27, 28, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private static Integer[] twentyNinedayss = {26, 27, 28, 29, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};


    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static Date parse(String dateStr, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {

            return null;
        }
    }

    public static int getDifferenceDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        int diff = 0;
        diff = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        return diff + 1;
    }

    public static int daysBetween(Calendar startInclusive, Calendar endExclusive) {
        zeroTime(startInclusive);
        zeroTime(endExclusive);

        long diff = endExclusive.getTimeInMillis() - startInclusive.getTimeInMillis(); //result in millis
        return (int) TimeUnit.MILLISECONDS.toDays(diff);
    }

    public static void zeroTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static int getDaysBetweenDatessize(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates.size();
    }

    public static int monthsBetween(Calendar startInclusive, Calendar endExclusive) {
        int startMonth = startInclusive.get(Calendar.MONTH);
        int endMonth = endExclusive.get(Calendar.MONTH);

        int startYear = startInclusive.get(Calendar.YEAR);
        int endYear = endExclusive.get(Calendar.YEAR);

        int yearsDiff = endYear - startYear;

        return (endMonth - startMonth) + (yearsDiff * 12);
    }

    public static List<Integer> daysofMonth(Calendar calendar) {
        List<Integer> mItems = new ArrayList<>();

        int a = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 0; j != calendar.get(Calendar.DAY_OF_WEEK) -1 ; j++) {
            mItems.add(0);
        }
        switch (a) {
            case 28:
                Collections.addAll(mItems, twentyEdightdayss);
                break;
            case 29:
                Collections.addAll(mItems, twentyNinedayss);
                break;
            case 30:
                Collections.addAll(mItems, thirtyayss);
                break;
            case 31:
                Collections.addAll(mItems, thirtyonedayss);
                break;

        }
        return mItems;
    }


}
