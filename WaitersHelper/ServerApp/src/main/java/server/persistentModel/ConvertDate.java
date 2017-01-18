package server.persistentModel;

import org.joda.time.LocalDate;
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

    public static LocalDate toJoda(java.time.LocalDate localDateJava) {
        return localDateJava == null ? null : new LocalDate(localDateJava.getYear(),
                localDateJava.getMonthValue(),
                localDateJava.getDayOfMonth());
    }

    public static java.time.LocalDate toJavaFromJoda(LocalDate localDate) {
        return localDate == null ? null : java.time.LocalDate.of(localDate.getYear(),
                localDate.getMonthOfYear(),
                localDate.getDayOfMonth());
    }
}
