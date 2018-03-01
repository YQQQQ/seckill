package org.seckill.until;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUntil {

    public static String getStringFromDate(Date date){
        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        return dateStr;
    }
}
