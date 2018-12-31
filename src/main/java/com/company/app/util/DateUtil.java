package com.company.app.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Holds date utility static methods
 *
 *
 * @since 12/3/2018 9:11 AM
 *
 */
public abstract class DateUtil {

    public static final DateTimeFormatter DATE_FMT_MDYYYY = DateTimeFormatter.ofPattern(Const.MDYYYY);
    public static final DateTimeFormatter DATE_FMT_YYYYMMDD = DateTimeFormatter.ofPattern(Const.YYYYMMDD);
    public static final DateTimeFormatter DATE_FMT_TIMESTAMP = DateTimeFormatter
            .ofPattern(Const.YYYY_MM_DD_HH_MM_SS_SSSSSS);

    /**
     * Convert a java.time.LocalDate instance to java.util.Date
     *
     * @param inLocalDate java.time.LocalDate instance
     * @return corresponding java.util.Date instance
     */
    public static java.util.Date asDate(final LocalDate inLocalDate) {
        return Date.from(inLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert a java.time.LocalDate instance to java.sql.Date
     *
     * @param inLocalDate java.time.LocalDate instance
     * @return corresponding java.util.Date instance
     */
    public static java.sql.Date asSqlDate(final LocalDate inLocalDate) {
        return new java.sql.Date(
                java.util.Date.from(inLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * Convert a java.time.LocalDate instance to java.sql.Date
     *
     * @param date java.time.LocalDate instance
     * @return corresponding java.util.Date instance
     */
    public static java.sql.Date asSqlDate(final java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Convert a java.time.LocalDateTime instance to java.util.Date
     *
     * @param inLocalDateTime java.time.LocalDateTime instance
     * @return corresponding java.util.Date instance
     */
    public static java.util.Date asDate(final LocalDateTime inLocalDateTime) {
        return Date.from(inLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert a java.util.Date instance to java.time.LocalDate
     *
     * @param inDate java.util.Date instance
     * @return corresponding java.time.LocalDate instance
     */
    public static LocalDate asLocalDate(final java.util.Date inDate) {
        // return
        // inDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Instant.ofEpochMilli(inDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convert a java.util.Date instance to java.time.LocalDateTime
     *
     * @param inDate java.util.Date instance
     * @return corresponding java.time.LocalDateTime instance
     */
    public static LocalDateTime asLocalDateTime(final java.util.Date inDate) {
        return inDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
