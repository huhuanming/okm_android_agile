package com.okm_android_agile.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    //获取年月日日期
    public static String getYearMonthDay(String utctime)
    {
        return formatDate(timestampToLocalDate(utctime));
    }

    //根据传入的UTC时间获取天数,无毫秒
    public static long getLocalDistDatas(String utcTimestart,String utcTimeend)
    {
        return getDistDates(timestampToLocalyear(utcTimestart),timestampToLocalyear(utcTimeend));
    }

    /**
     * UTC转为本地时间
     * @param timestamp
     * @return Data
     */

    public static Date timestampToLocalyear(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        try {
            date = sdf.parse(timestamp);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    //根据传入的UTC时间获取星期几，无毫秒
    public static String getLocalweek(String utcTime)
    {
        return getWeek(timestampToLocalDate(utcTime));
    }
    /**
     * 返回两个日期相差的天数
     * @param startDate
     * @param endDate
     * @return
     * @throws java.text.ParseException
     */
    public static long getDistDates(Date startDate,Date endDate)
    {
        long totalDate = 0;

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart))/(1000*60*60*24);
        return totalDate;
    }

    //根据日期取得星期几
    public static String getWeek(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }

    public static String getMonthTime(String utctime)
    {
        return formatDataTime(timestampToLocalDate(utctime));
    }

    public static String formatDataTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        return sdf.format(date);
    }

    //获取时间
    public static String getTime(String utctime)
    {
        return formatTime(timestampToLocalDate(utctime));
    }

    public static String formatTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * UTC转为本地时间
     * @param timestamp
     * @return Data
     */

    public static Date timestampToLocalDate(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = new Date();
		try {
			date = sdf.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

    public static Date timestampToLocalDateWithMillisecond(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        try {
            date = sdf.parse(timestamp);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取指定时间的UTC时间,指定时间不包含毫秒
     *
     *
     *
     * @param timestamp 传入服务器返回的时间
     * @return String 	返回UTC时间
     */
    public static String getUTCTimestamp(String timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = timestampToLocalDate(timestamp);
        String ts = sdf.format(date);
        return ts;
    }

    /**
     * 获取指定时间的UTC时间,指定时间包含毫秒
     *
     *
     *
     * @param timestamp 传入服务器返回的时间
     * @return String 	返回UTC时间
     */
    public static String getUTCTimestampWithMillisecond(String timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = timestampToLocalDateWithMillisecond(timestamp);
        String ts = sdf.format(date);
        return ts;
    }

    /**
     * 获取当前的UTC时间
     *
     *
     *
     * @return String 	返回当前UTC时间
     */
    public static String getUTCCurrentTimestampWithMillisecond()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();

        String timestamp = sdf.format(date);
        return timestamp;
    }

    /**
     * 获取当前的UTC时间
     *
     *
     *
     * @return String 	返回当前UTC时间
     */
    public static String getUTCCurrentTimestamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();

        String timestamp = sdf.format(date);
        return timestamp;
    }

    /**
     * 获取当前的UTC时间
     *
     *
     *
     * @return String 	返回当前UTC时间
     */
    public static String getUTCStartTimestamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = timestampToLocalDate("1970-01-01T00:00:00Z");
        String timestamp = sdf.format(date);
        return timestamp;
    }

    public static boolean compareUTCTimestamp(String beforeTimestamp, String afterTimestamp)
    {
        Date beforeDate = timestampToLocalDateWithMillisecond(beforeTimestamp);
        Date afterDate = timestampToLocalDateWithMillisecond(afterTimestamp);
        long a = beforeDate.getTime();
        long b = afterDate.getTime();
        return beforeDate.getTime() > afterDate.getTime();
    }

    /**
     * 根据传入的时间计算距离该时间是几分钟前，几小时前，还是显示具体时间
	 * 
     *
     *
     * @param timestamp UTC时间
     * @return String 	根据传入的的时间距离当前时间的时长，返回不同的字符串
     */
	public static String timestampToDeatil(String timestamp){
		Date date = DateUtils.timestampToLocalDate(timestamp);
		String detailString = new String();
		long  distance = System.currentTimeMillis() - date.getTime();
		if(distance > 0 && distance < 60000)
		{
			detailString = formatSecondDate(distance);
		}
		else if (distance > 60000 && distance < 3600000) {
	    	detailString = formatMinuteDate(distance);
	    }else if(distance >= 3600000 && distance < 86400000){
	        detailString = formatDayDate(distance);
	    }
	    else{
	        detailString = formatDeatilDate(date);
	    }
		return detailString;
	}

    /**
     * 根据传入的时间计算距离该时间是几分钟前，几小时前，还是显示具体时间
     *
     *
     *
     * @param timestamp UTC时间
     * @return String 	根据传入的的时间距离当前时间的时长，返回不同的字符串
     * 针对的是后面有毫秒的
     */
    public static String StimestampToDeatil(String timestamp){
        Date date = DateUtils.timestampToLocalDateWithMillisecond(timestamp);
        String detailString = new String();
        long  distance = System.currentTimeMillis() - date.getTime();
        if(distance > 0 && distance < 60000)
        {
            detailString = formatSecondDate(distance);
        }
        else if (distance > 60000 && distance < 3600000) {
            detailString = formatMinuteDate(distance);
        }else if(distance >= 3600000 && distance < 86400000){
            detailString = formatDayDate(distance);
        }
        else{
            detailString = formatDeatilDate(date);
        }
        return detailString;
    }
	
	
	public static String formatSecondDate(long distance) {
		return new StringBuilder().append(distance / 1000)
								  .append("秒前")
								  .toString();
	}
	public static String formatMinuteDate(long distance) {
		return new StringBuilder().append(distance / 60000)
								  .append("分钟前")
								  .toString();
	}
	
	public static String formatDayDate(long distance) {
		return new StringBuilder().append(distance / 3600000)
								  .append("小时前")
								  .toString();
	}
	
	public static String formatDeatilDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return sdf.format(date);
	}

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }



    /**
     * 根据传入的时间计算距离该时间是几分钟前，几小时前，还是显示具体时间
     *
     *
     *
     * @param timestamp UTC时间
     * @return String 	根据传入的的时间距离当前时间的时长，返回不同的字符串
     */
    public static String timestampToDeatilDateWithMillisecond(String timestamp){
        Date date = DateUtils.timestampToLocalDateWithMillisecond(timestamp);

        return formatDeatilDate(date);
    }
}
