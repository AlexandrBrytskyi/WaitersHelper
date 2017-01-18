package client.service.dateUtils;

import org.joda.time.LocalDateTime;

/**
 * User: huyti
 * Date: 15.05.2016
 */
public class ConvertDate {

    public static LocalDateTime toJoda(java.time.LocalDateTime localDateTimeJava) {
        return localDateTimeJava == null ? null : new LocalDateTime(localDateTimeJava.getYear(),
                localDateTimeJava.getMonthValue(),
                localDateTimeJava.getDayOfMonth(),
                localDateTimeJava.getHour(),
                localDateTimeJava.getMinute(),
                localDateTimeJava.getSecond());
    }

    public static java.time.LocalDateTime toJavaFromJoda(LocalDateTime localDateTime) {
        return localDateTime == null ? null : java.time.LocalDateTime.of(localDateTime.getYear(),
                localDateTime.getMonthOfYear(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHourOfDay(),
                localDateTime.getMinuteOfHour(),
                localDateTime.getSecondOfMinute());
    }
}
