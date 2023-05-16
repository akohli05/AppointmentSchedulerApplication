package com.example.c195pa.utils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Class that converts the start and end times to UTC
 *
 * @author Aimy Kohli
 */
public class UTCTimeConverter {

    /**
     * Converts the start and end times to UTC for database storing
     * @param localDateTime The time to convert
     * @return The converted time
     */
    public static String convertToUTC(String localDateTime){
        Timestamp currentTimeStamp = Timestamp.valueOf(localDateTime);//Get the current timestamp
        LocalDateTime currentLocalDateTime = currentTimeStamp.toLocalDateTime();// Convert the timestamp to local date time

        ZonedDateTime localZoneDateTime = ZonedDateTime.of(currentLocalDateTime, ZoneId.systemDefault());//Get the default zone date time
        ZonedDateTime utcZoneDateTime = localZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));// Convert to the UTC zone date time
        LocalDateTime utcLocalDateTime = utcZoneDateTime.toLocalDateTime();//Convert to the local date time

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //Date time formatter pattern
        String utcTime = utcLocalDateTime.format(customFormatter); //Convert to string

        return utcTime;
    }

}
