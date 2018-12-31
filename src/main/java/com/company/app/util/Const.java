package com.company.app.util;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * Holds static constant definitions
 *
 * @since 12/3/2018 9:11 AM
 *
 */
public abstract class Const {

    public static final Joiner EMPTY_JOINER = Joiner.on(Const.EMPTY_STRING);
    public static final String COMMA = ",";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String MDYYYY = "M/d/yyyy";
    public static final String YYYY_MM_DD_HH_MM_SS_SSSSSS = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    public static final DateTimeFormatter DATE_FMT_YYYYMMDD = DateTimeFormatter.ofPattern(YYYYMMDD);
    public static final DateTimeFormatter DATE_FMT_MDYYYY = DateTimeFormatter.ofPattern(MDYYYY);
    public static final DateTimeFormatter DATE_FMT_TIMESTAMP = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSSSSS);
    public static final Splitter CSV_SPLITTER = Splitter.on(COMMA).trimResults();
    //
    public static final String EMPTY_STRING = "";
    public static final String SUCCESSMSG = "successMsg";
    public static final String FAILMSG = "failMsg";
    public static final String PIPE = "|";
    public static final String NULL = "_NULL_";
    public static final String YES_FLAG = "Y";
    public static final String NO_FLAG = "N";
    //
    public static final MathContext AMT_MATH_CTX = new MathContext(2, RoundingMode.HALF_UP);
    public static final MathContext RATE_MATH_CTX = new MathContext(8, RoundingMode.HALF_UP);
    public static final MathContext TEN_MATH_CTX = new MathContext(10, RoundingMode.HALF_UP);
    public static final MathContext CALC_MATH_CTX = new MathContext(16, RoundingMode.HALF_UP);
    //
    public static final String FILE_HEADER_NAMES_STR = "id, name, amount, date, customerName, transactionType";
    private static final String OBJECT_FIELD_NAMES_STR = StringUtils.replace(FILE_HEADER_NAMES_STR, "date",
            "dateFmt1Str");
    public static final String[] FILE_FIELD_NAMES_ARR = CSV_SPLITTER.splitToList(FILE_HEADER_NAMES_STR)
            .toArray(new String[0]);
    public static final String[] OBJECT_FIELD_NAMES_ARR = CSV_SPLITTER.splitToList(OBJECT_FIELD_NAMES_STR)
            .toArray(new String[0]);

    private Const() {
    }

    public static final class OutMap {

        public static final String STATUS_KEY = "STATUS.SERVICE";
        public static final String MESSAGE_KEY = "MESSAGE.SERVICE";
        public static final String ERROR = "ERROR";
        public static final String OK = "OK";
        public static final String WARNING = "WARNING";

        private OutMap() {
        }
    }
}
